package web;

import info.novatec.testit.logrecorder.api.LogRecord;
import info.novatec.testit.logrecorder.jul.junit5.RecordLoggers;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import web.api.BookResource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class BookResourceIntTest {

    @Test
    @DisplayName("Creating a book, getAllBooks, deleteBook, getAllBooks")
    @RecordLoggers(value = {BookResource.class})
    public void testPostBookAddToLibraryAndReturnBook(LogRecord log) {
        ExtractableResponse<io.restassured.response.Response> extract = given()
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
                .extract();
        String uriAsString = extract.body().jsonPath().get("links[0].uri");

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
                .body("books[0].links[0].uri", is(uriAsString))
        ;

        String uuid = uriAsString.substring(uriAsString.lastIndexOf('/') + 1);
        given()
                .when()
                .get("/api/books/{id}", uuid)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("title", is("Clean Code"))
                .body("isbn", is("9780132350884"))
                .body("links[0].rel", is("self"))
                .body("links[0].uri", is(uriAsString))
        ;
        Assertions.assertThat(log.getMessages()).containsExactly("getBook uuid="+uuid);
        given()
                .when()
                .delete("/api/books/{id}", uuid)
                .then()
                .log().all()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

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

}
