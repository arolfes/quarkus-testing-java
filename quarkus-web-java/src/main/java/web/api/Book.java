package web.api;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.ws.rs.core.Link;
import java.util.List;

@RegisterForReflection
public class Book {

    public String title;
    public String isbn;
    public List<Link> links;

}
