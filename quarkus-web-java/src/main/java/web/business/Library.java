package web.business;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class Library {

    private Map<UUID, BookRecord> bookDatabase = new HashMap<>();

    public BookRecord add(Book book) {

        UUID id = UUID.randomUUID();
        BookRecord br = new BookRecord(id, book);
        bookDatabase.put(id, br);
        return br;
    }

    public List<BookRecord> getAll() {
        return new ArrayList<>(bookDatabase.values());
    }

    public void delete(UUID id) {
        if (!bookDatabase.containsKey(id)) {
            throw new BookRecordNotFoundException(id);
        }
        bookDatabase.remove(id);
    }

    public BookRecord get(UUID id) {
        if (!bookDatabase.containsKey(id)) {
            throw new BookRecordNotFoundException(id);
        }
        return bookDatabase.get(id);
    }
}