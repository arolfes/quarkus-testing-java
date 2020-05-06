package jpa.books;

import io.quarkus.test.common.QuarkusTestResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TransactionalQuarkusTest
public class BookRepositoryWithPostgresTest {

    @Container
    private static final PostgreSQLContainer db = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("quarkusjdbc")
            .withUsername("quarkus")
            .withPassword("changeme");

    @BeforeAll
    public static void setProps() {
        db.waitingFor(new HostPortWaitStrategy());
        db.start();
        System.setProperty("%test.quarkus.datasource.driver", db.getDriverClassName());
        System.clearProperty("%test.quarkus.hibernate-orm.dialect");
        System.setProperty("%test.quarkus.datasource.url", db.getJdbcUrl());
        System.setProperty("%test.quarkus.datasource.username", db.getUsername());
        System.setProperty("%test.quarkus.datasource.password", db.getPassword());
    }

    @AfterAll
    public static void removeProps() {
        System.setProperty("%test.quarkus.datasource.driver","org.testcontainers.jdbc.ContainerDatabaseDriver");
        System.setProperty("%test.quarkus.hibernate-orm.dialect","org.hibernate.dialect.PostgreSQL10Dialect");
        System.setProperty("%test.quarkus.datasource.url","jdbc:tc:postgresql:latest:///dbname");
        System.clearProperty("%test.quarkus.datasource.username");
        System.clearProperty("%test.quarkus.datasource.password");
        if (db.isCreated()) {
            db.close();
        }

    }

    @Inject
    private BookRepository bookRepository;

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
