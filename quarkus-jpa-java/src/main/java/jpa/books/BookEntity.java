package jpa.books;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "BookRecord")
@Table(name = "book_records")
public class BookEntity {

    @Id
    public UUID id;
    public String title;
    public String isbn;

    public BookEntity() {
    }

    public BookEntity(UUID id, String title, String isbn) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
    }
}