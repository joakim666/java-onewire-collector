package net.morrdusk.collector.onewire;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import net.morrdusk.collector.onewire.db.ReadingsDb;
import net.morrdusk.collector.onewire.scheduling.GuiceJobFactory;
import net.morrdusk.collector.onewire.scheduling.Quartz;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;

public class CollectorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SchedulerFactory.class).to(StdSchedulerFactory.class).in(Scopes.SINGLETON);
        bind(GuiceJobFactory.class).in(Scopes.SINGLETON);
        bind(Quartz.class).in(Scopes.SINGLETON);
        bind(String.class)
                .annotatedWith(Names.named("DB_DIR"))
                .toInstance("." + File.separator + "db");
        bind(ReadingsDb.class).in(Scopes.SINGLETON);
    }
}
