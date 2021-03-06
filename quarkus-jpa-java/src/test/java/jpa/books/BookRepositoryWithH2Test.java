package jpa.books;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TransactionalQuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class BookRepositoryWithH2Test {

    @Inject
    private BookRepository bookRepository;

    @BeforeAll
    public static void setProps() {
        System.setProperty("%test.quarkus.datasource.driver","org.h2.Driver");
        System.clearProperty("%test.quarkus.hibernate-orm.dialect");
        System.setProperty("%test.quarkus.datasource.url","jdbc:h2:tcp://localhost/mem:test");
    }

    @AfterAll
    public static void removeProps() {
        System.setProperty("%test.quarkus.datasource.driver","org.testcontainers.jdbc.ContainerDatabaseDriver");
        System.setProperty("%test.quarkus.hibernate-orm.dialect","org.hibernate.dialect.PostgreSQL10Dialect");
        System.setProperty("%test.quarkus.datasource.url","jdbc:tc:postgresql:latest:///dbname");
    }

    @BeforeEach
    public void clearDatabase() {
        bookRepository.deleteAll();
    }

    @Test
    public void entityCanBePersistAndFoundById() {
        UUID id = UUID.randomUUID();
        BookEntity entity = new BookEntity(id, "Clean Code", "9780132350884");
        bookRepository.persist(entity);
        BookEntity foundEntity = bookRepository.findById(id);
        assertThat(foundEntity).isEqualTo(entity);
    }

    @Test
    public void entityCanBeFoundByTitle() {

        BookEntity e1 = new BookEntity(UUID.randomUUID(), "Clean Code", "9780132350884");
        bookRepository.persist(e1);
        BookEntity e2 = new BookEntity(UUID.randomUUID(), "Clean Architecture", "9780134494166");
        bookRepository.persist(e2);
        BookEntity e3 = new BookEntity(UUID.randomUUID(), "Clean Code", "9780132350884");
        bookRepository.persist(e3);


        List<BookEntity> foundEntities = bookRepository.findByTitle("Clean Code");
        assertThat(foundEntities)
                .contains(e1, e3)
                .doesNotContainSequence(e2);
    }

}
