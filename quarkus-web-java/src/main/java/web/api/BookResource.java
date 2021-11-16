package web.api;

import org.jboss.logmanager.Logger;
import web.business.BookRecord;
import web.business.Library;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private static final Logger LOG = Logger.getLogger(BookResource.class.getName());
    @Inject
    private Library library;

    @GET
    public Response list(@Context UriInfo uriInfo) {

        List<Book> bookResourceList = library.getAll().stream().map(bookRecord -> mapBookRecord2BookApi(uriInfo, bookRecord)).collect(Collectors.toList());

        Books booksResource = new Books();
        booksResource.books = bookResourceList;
        booksResource.links = Collections.singletonList(
                Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()).rel("self").type(MediaType.APPLICATION_JSON).build()
        );

        return Response
                .ok(booksResource)
                .build();
    }

    private Book mapBookRecord2BookApi(@Context UriInfo uriInfo, BookRecord bookRecord) {
        Book br = new Book();
        br.isbn = bookRecord.book.isbn;
        br.title = bookRecord.book.title;
        UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder().replacePath("/api/books").path(bookRecord.id.toString());
        br.links = Collections.singletonList(
                Link.fromUriBuilder(absolutePathBuilder).rel("self").type(MediaType.APPLICATION_JSON).build()
        );
        return br;
    }

    @GET
    @Path("{id}")
    public Response get(@Context UriInfo uriInfo, @PathParam("id") UUID id) {
        LOG.info("getBook uuid="+id.toString());
        return Response
                .ok(mapBookRecord2BookApi(uriInfo, library.get(id)))
                .build();
    }

    @POST
    public Response add(@Context UriInfo uriInfo, @Valid CreateBookRequest cbr) {
        BookRecord bookRecord = library.add(new web.business.Book(cbr.title, cbr.isbn));
        return Response
                .status(Response.Status.CREATED)
                .entity(mapBookRecord2BookApi(uriInfo, bookRecord))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        library.delete(id);
        return Response.noContent().build();
    }


}