package in.hopscotch.dwguice.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import in.hopscotch.dwguice.representations.Milestone;

@Path("/ordertracking")
@Produces(MediaType.APPLICATION_JSON)
public interface OrderTrackingResource {
	
	@GET
	@Path("/track/{id}")
	public Response trackOrder(@PathParam("id") Integer id);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/milestone/add")
	public Response addMilestone(Milestone milestone);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/milestone/update")
	public Response updateMilestone(Milestone milestone);

}
