package in.hopscotch.dwguice.resource;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import in.hopscotch.dwguice.api.jms.MessageSender;
import in.hopscotch.dwguice.representations.Milestone;
import in.hopscotch.dwguice.representations.OrderTrackingResponse;

@Path("/ordertracking")
@Produces(MediaType.APPLICATION_JSON)
public class OrderTrackingResourceImpl {
	@Inject
	MessageSender timeLogMessageSender;
	
	@GET
	@Path("/track/{id}")
	@PermitAll
	public Response trackOrder(@PathParam("id") Integer id) {
		timeLogMessageSender.sendJson(id.toString() + " --- Message ----");
		return Response.ok(new OrderTrackingResponse()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/milestone/add")
	public Response addMilestone(Milestone milestone) {
		return Response.ok().build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/milestone/update")
	public Response updateMilestone(Milestone milestone) {
		return Response.ok().build();
	}

}
