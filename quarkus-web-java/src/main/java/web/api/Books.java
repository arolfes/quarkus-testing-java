package web.api;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.ws.rs.core.Link;
import java.util.Collection;
import java.util.List;

@RegisterForReflection
public class Books {

    public Collection<Book> books;
    public List<Link> links;
}
