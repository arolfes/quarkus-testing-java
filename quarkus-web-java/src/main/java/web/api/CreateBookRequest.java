package web.api;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@RegisterForReflection
public class CreateBookRequest {

    @NotBlank(message = "Title may not be blank")
    public String title;

    @Pattern(regexp = "[0-9]{13}", message = "Not a valid ISBN format, must be 13 numbers from 0 to 9")
    @NotNull
    public String isbn;


    public CreateBookRequest() {
    }

    public CreateBookRequest(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateBookRequest that = (CreateBookRequest) o;
        return title.equals(that.title) &&
                isbn.equals(that.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, isbn);
    }

    @Override
    public String toString() {
        return "CreateBookRequest{" +
                "title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
