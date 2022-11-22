package org.routing.web.controller;

import org.routing.geometries.FeatureCollection;
import org.routing.web.configuration.RoutingConfiguration;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/route")
public class RoutingController {

    @Inject
    RoutingConfiguration routingConfiguration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public FeatureCollection hello() {
        return routingConfiguration.getRoute();
    }
}
