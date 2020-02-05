package com.kellton.userservice.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

@Component
@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		List<Errors> errors = new ArrayList<Errors>();
		Response response = null;
	 if (exception instanceof UnauthorizedUserException) {

			UnauthorizedUserException unauthorizedUserException = (UnauthorizedUserException) exception;
			Errors errorMessage;
			 errorMessage = new Errors(Response.Status.UNAUTHORIZED.getStatusCode(), null , null ,
					"INVALID_TOKEN", unauthorizedUserException.getMessage());
			response = Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON)
					.entity(Collections.singletonMap("errors", Arrays.asList(errorMessage))).build();
		}
		return response;
	}

}
