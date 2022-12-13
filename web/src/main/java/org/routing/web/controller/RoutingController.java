package org.routing.web.controller;

import org.routing.geometries.FeatureCollection;
import org.routing.web.configuration.RoutingConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("route")
public class RoutingController {

    private final RoutingConfiguration routingConfiguration;

    public RoutingController(RoutingConfiguration routingConfiguration) {
        this.routingConfiguration = routingConfiguration;
    }

    @GetMapping("")
    public FeatureCollection hello() {
        return routingConfiguration.getRoute();
    }
}
