package mongodb.books;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import mongodb.MongoDbTestResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(MongoDbTestResource.class)
public class BookRecordDocumentTest {

    @BeforeEach
    public void clearDatabase() {
        BookRecordDocument.deleteAll();
    }

    @Test
    public void documentCanBePersistedAndFound() {

        BookRecordDocument book = new BookRecordDocument("Clean Code", "9780132350884");
        book.persist();

        BookRecordDocument foundBook = BookRecordDocument.findById(book.id);
        assertThat(foundBook).isEqualToComparingFieldByField(book);
    }

    @Test
    public void booksCanBeFoundByTitle() {
        BookRecordDocument book1 = new BookRecordDocument("Clean Code", "9780132350884");
        book1.persist();
        BookRecordDocument book2 = new BookRecordDocument("Clean Architecture", "9780134494166");
        book2.persist();
        BookRecordDocument book3 = new BookRecordDocument("Clean Code", "9780132350884");
        book3.persist();

        List<BookRecordDocument> foundBooks = BookRecordDocument.findByTitle("Clean Code");
        assertThat(foundBooks).contains(book1, book3).doesNotContain(book2);

    }
}
