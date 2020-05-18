package web;

import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import web.api.CreateBookRequest;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class CreateBookRequestTest {

    private Validator validator = buildDefaultValidatorFactory().getValidator();

    @Inject
    private Jsonb mapper;

    @Test
    @DisplayName("can be deserialized from JSON")
    public void testDeserialize() {
        String json = "{\n" +
                "    \"title\": \"Clean Code\",\n" +
                "    \"isbn\": \"9780132350884\"\n" +
                "}";
        assertThat(mapper.fromJson(json, CreateBookRequest.class)).isEqualTo(
                new CreateBookRequest("Clean Code", "9780132350884"
                ));
    }

    @Test
    @DisplayName("'isbn' property allows 13 character ISBN")
    public void testIsbn13CharacterLong() {
        CreateBookRequest request = new CreateBookRequest("Clean Code", "9780132350884");
        assertThat(validator.validate(request)).isEmpty();
    }

    @ValueSource(strings = {"1234567890", "123456789012", "12345678901234"})
    @DisplayName("'isbn' property allows only 13 character ISBN")
    @ParameterizedTest
    public void testIsbnExpectOnly13Characters(String isbn) {
        CreateBookRequest request = new CreateBookRequest("My Book", isbn);
        validator.validate(request).stream().map(ConstraintViolation::getMessage).findFirst().ifPresentOrElse(
                msg -> assertThat(msg).containsIgnoringCase("Not a valid ISBN format, must be 13 numbers from 0 to 9"),
                () -> Assertions.fail("didn't receive an error message")
        );
    }

    @Test
    @DisplayName("title of bookrequest is required")
    public void testTitleLong() {
        CreateBookRequest request = new CreateBookRequest(null, "9780132350884");
        validator.validate(request).stream().map(ConstraintViolation::getMessage).findFirst().ifPresentOrElse(
                msg -> assertThat(msg).containsIgnoringCase("Title may not be blank"),
                () -> Assertions.fail("didn't receive an error message")
        );
    }

}
