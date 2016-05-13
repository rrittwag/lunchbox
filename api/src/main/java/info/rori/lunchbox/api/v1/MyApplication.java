package info.rori.lunchbox.api.v1;

import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResource;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
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
        register(ApiDeclarationProvider.class);
        register(ApiListingResourceJSON.class);
        register(ResourceListingProvider.class);

        // Swagger konfigurieren
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.2.0");
        beanConfig.setBasePath("http://localhost:8080/api/v1");
        beanConfig.setResourcePackage(RESOURCE_PACKAGE);

        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());
        beanConfig.setScan(true);
    }
}
