package info.rori.lunchbox.api.v1;

import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Einstiegspunkt für die Lunchbox REST API in Version 1.
 */
public class MyApplication extends ResourceConfig {

    public static final String RESOURCE_PACKAGE = MyApplication.class.getPackage().getName() + ".resource";

    public MyApplication() {
        initRestApi();
        initSwagger();
    }

    private void initRestApi() {
        packages(RESOURCE_PACKAGE);

        // für JSON-Support via Jackson
        register(JacksonFeature.class);
    }

    private void initSwagger() {
        // Swagger-Resourcen anmelden
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);

        // Swagger konfigurieren
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Lunchbox API v1");
        beanConfig.setVersion("1.2.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api/v1");
        beanConfig.setResourcePackage(RESOURCE_PACKAGE);
        beanConfig.setScan(true);
    }
}
