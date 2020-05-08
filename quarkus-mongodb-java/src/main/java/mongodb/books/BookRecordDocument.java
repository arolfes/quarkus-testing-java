package mongodb.books;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.util.List;
import java.util.Objects;

@MongoEntity(collection = "book_records")
public class BookRecordDocument extends PanacheMongoEntity {

    public String title;
    public String isbn;

    public BookRecordDocument() {
    }

    public BookRecordDocument(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    public static List<BookRecordDocument> findByTitle(String title) {
        return list("title", title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRecordDocument that = (BookRecordDocument) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(isbn, that.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, isbn);
    }

    @Override
    public String toString() {
        return "BookRecordDocument{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}