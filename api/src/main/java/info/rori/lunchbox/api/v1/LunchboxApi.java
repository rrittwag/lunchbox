package info.rori.lunchbox.api.v1;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

/**
 * Einstiegspunkt für die Lunchbox REST API in Version 1.
 */
public class LunchboxApi extends ResourceConfig {

    public static final String RESOURCE_PACKAGE = LunchboxApi.class.getPackage().getName();

    public LunchboxApi() {
        initRestApi();
        initSwagger();
    }

    private void initRestApi() {
        packages(RESOURCE_PACKAGE);
    }

    private void initSwagger() {
        register(OpenApiResource.class);
    }
}
