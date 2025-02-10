package org.wanaku.server.quarkus.api.v1.resources;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.wanaku.api.types.ResourceReference;

@ApplicationScoped
@Path("/api/v1/resources")
public class ResourcesResource {
    private static final Logger LOG = Logger.getLogger(ResourcesResource.class);

    @Inject
    ResourcesBean resourcesBean;

    @Path("/expose")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response expose(ResourceReference resource) {
        try {
            resourcesBean.expose(resource);
            return Response.ok().build();
        } catch (Exception e) {
            LOG.errorf("Failed to expose resource %s: %s", resource.getName(), e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to expose resource").build();
        }
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        try {
            List<ResourceReference> list = resourcesBean.list();
            return Response.ok(list).build();
        } catch (Exception e) {
            LOG.errorf("Failed to list resources: %s", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to list resources").build();
        }
    }
}
