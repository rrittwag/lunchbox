package info.rori.lunchbox.api.v1;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Einstiegspunkt für die Lunchbox REST API in Version 1.
 */
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        packages(getClass().getPackage().getName() + ".resource");
        register(JacksonFeature.class);
    }
}
