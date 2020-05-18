package web.business;

import java.util.UUID;

public class BookRecordNotFoundException extends RuntimeException {

    public UUID id;

    public BookRecordNotFoundException(UUID id) {
        super("Book [" + id + "] not found!");
        this.id = id;
    }

}