/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            e.printStackTrace();
            LOG.errorf(e, "Failed to expose resource %s: %s", resource.getName(), e.getMessage());
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
            LOG.errorf(e, "Failed to list resources: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to list resources").build();
        }
    }
}
