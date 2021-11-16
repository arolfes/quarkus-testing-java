package web;

import static io.restassured.RestAssured.given;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import info.novatec.testit.logrecorder.api.LogRecord;
import info.novatec.testit.logrecorder.jul.junit5.RecordLoggers;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import javax.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import web.api.BookResource;
import web.business.Book;
import web.business.BookRecord;
import web.business.BookRecordNotFoundException;
import web.business.Library;

@QuarkusTest
public class BookResourceTest {

    @InjectMock
    Library library;

    @BeforeEach
    public void clearLibrary() {
        reset(library);
    }

    @Test
    @DisplayName("GET /api/books - getting all books when there are none returns empty resource list")
    public void testGetBooksEmpty() {
        when(library.getAll()).thenReturn(Collections.emptyList());

        given()
                .when()
                .get("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("books", empty())
                .body("links[0].rel", is("self"))
                .body("links[0].uri", endsWith("/api/books"));

    }

    @Test
    @DisplayName("GET /api/books - getting all books returns their resource representation as a resource list")
    public void testGetBooks() {
        Book book = new Book("Clean Code", "9780132350884");
        UUID id = randomUUID();
        Book book2 = new Book("Clean Architecture", "9780134494166");
        UUID id2 = randomUUID();
        when(library.getAll()).thenReturn(Arrays.asList(new BookRecord(id, book), new BookRecord(id2, book2)));

        given()
                .when()
                .get("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("books[0].title", is("Clean Code"))
                .body("books[0].isbn", is("9780132350884"))
                .body("books[0].links[0].rel", is("self"))
                .body("books[0].links[0].uri", endsWith("/api/books/" + id.toString()))
                .body("books[1].title", is("Clean Architecture"))
                .body("books[1].isbn", is("9780134494166"))
                .body("books[1].links[0].rel", is("self"))
                .body("books[1].links[0].uri", endsWith("/api/books/" + id2.toString()))
                .body("links[0].rel", is("self"))
                .body("links[0].uri", endsWith("/api/books"));

    }

    @Test
    @DisplayName("GET /api/books/{id} - getting a book by its id returns resource representation")
    @RecordLoggers(value = {BookResource.class}, names = { "org.jboss.logmanager.Logger" })
    public void testGetBookById(LogRecord log) {
        Book book = new Book("Clean Code", "9780132350884");
        UUID id = randomUUID();
        when(library.get(id)).thenReturn(new BookRecord(id, book));
        given()
                .when()
                .get("/api/books/{id}", id)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("title", is("Clean Code"))
                .body("isbn", is("9780132350884"))
                .body("links[0].rel", is("self"))
                .body("links[0].uri", endsWith("/api/books/" + id.toString()))
        ;
        Assertions.assertThat(log.getMessages()).containsExactly("getBook uuid="+id.toString());
    }

    @Test
    @DisplayName("GET /api/books/{id} - getting an unknown book by its id responds with status 404")
    public void testGetUnknownBookId() {
        when(library.get(any(UUID.class))).thenAnswer(i -> {
            throw new BookRecordNotFoundException(i.getArgument(0));
        });

        UUID id = randomUUID();
        given()
                .when().get("/api/books/{id}", id)

                .then()
                .log().all()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .contentType(ContentType.JSON)
                .body("errorMessage", is("Book [" + id + "] not found!"));

    }

    @Test
    @DisplayName("POST /api/books - posting a book adds it to the library and returns resource representation")
    public void testPostBookAddToLibraryAndReturnBook() {
        UUID id = randomUUID();
        when(library.add(any(Book.class))).thenAnswer(i -> new BookRecord(id, i.getArgument(0)));
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(" { \"title\": \"Clean Code\", \"isbn\": \"9780132350884\" } ")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .contentType(ContentType.JSON)
                .body("title", is("Clean Code"))
                .body("isbn", is("9780132350884"))
                .body("links[0].rel", is("self"))
                .body("links[0].uri", endsWith("/api/books/" + id.toString()))
        ;
    }

    @Test
    @DisplayName("POST /api/books - posting a book with a malformed 'isbn' property responds with status 400")
    public void testPostBookMalformedIsbn() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(" { \"title\": \"Clean Code\", \"isbn\": \"0132350884\" } ")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @DisplayName("POST /api/books - posting a book with blank 'title' property responds with status 400")
    public void testPostBookBlankTitle() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(" { \"title\": \"\", \"isbn\": \"9780132350884\" } ")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @DisplayName("POST /api/books - posting a book with missing 'title' property responds with status 400")
    public void testPostBookMissingTitle() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(" { \"isbn\": \"9780132350884\" } ")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @DisplayName("POST /api/books - posting a book with missing 'isbn' property responds with status 400")
    public void testPostBookMissingIsbn() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(" { \"title\": \"Clean Code\" } ")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - deleting a book by its id returns status 204")
    public void testDeleteBook() {
        UUID id = randomUUID();
        given()
                .when()
                .delete("/api/books/{id}", id)
                .then()
                .log().all()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - deleting an unknown book by its id responds with status 404")
    public void testDeleteBookNotFound() {
        UUID id = randomUUID();
        doAnswer(i -> {
            throw new BookRecordNotFoundException(i.getArgument(0));
        }).when(library).delete(any(UUID.class));
        given()
                .when()
                .delete("/api/books/" + id.toString())
                .then()
                .log().all()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }


}