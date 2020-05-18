package web.api;

import web.business.BookRecordNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BookRecordNotFoundExceptionHandler implements ExceptionMapper<BookRecordNotFoundException> {
    @Override
    public Response toResponse(BookRecordNotFoundException exception) {
        return Response.status(Status.NOT_FOUND).entity("{\"errorMessage\":\"" + exception.getMessage() + "\"}").build();
    }
}
