package de.htwBerlin.ai.kbe;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import de.htwBerlin.ai.kbe.DependencyInjection.DependencyBinder;
import de.htwBerlin.ai.kbe.Params.Constants;


@ApplicationPath(Constants.ROOT_RESOURCE_PATH)
public class App extends ResourceConfig {

    public App() {
        register(new DependencyBinder());
        packages("de.htwBerlin.ai.kbe.services");


    }
}