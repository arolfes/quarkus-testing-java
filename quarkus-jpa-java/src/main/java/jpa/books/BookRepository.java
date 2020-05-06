package jpa.books;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BookRepository implements PanacheRepositoryBase<BookEntity, UUID> {

    public List<BookEntity> findByTitle(String title) {
        return find("title", title).list();
    }

}
