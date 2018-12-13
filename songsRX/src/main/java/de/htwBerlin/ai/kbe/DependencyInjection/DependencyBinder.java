package de.htwBerlin.ai.kbe.DependencyInjection;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.htwBerlin.ai.kbe.storage.InMemorySongsPersistence;
import de.htwBerlin.ai.kbe.storage.ISongPersistence;
import de.htwBerlin.ai.kbe.storage.ITokenCreator;
import de.htwBerlin.ai.kbe.storage.TokenCreator;

public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {

        bind(InMemorySongsPersistence.class).to(ISongPersistence.class).in(Singleton.class);
        bind(TokenCreator.class).to(ITokenCreator.class).in(Singleton.class);

    }

}