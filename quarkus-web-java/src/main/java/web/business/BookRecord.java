package web.business;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;
import java.util.UUID;

@RegisterForReflection
public class BookRecord {

    public UUID id;
    public Book book;

    public BookRecord() {
    }

    public BookRecord(UUID id, Book book) {
        this.id = id;
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRecord that = (BookRecord) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(book, that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book);
    }

    @Override
    public String toString() {
        return "BookRecord{" +
                "id='" + id + '\'' +
                ", " + book +
                '}';
    }
}