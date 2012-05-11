import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import net.morrdusk.collector.onewire.CollectorModule;
import net.morrdusk.collector.onewire.db.ReadingsDb;
import net.morrdusk.collector.onewire.scheduling.PollingJobActivator;
import net.morrdusk.collector.onewire.scheduling.Quartz;
import net.morrdusk.collector.onewire.scheduling.UploadJobActivator;

public class Main {

    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("Usage java -jar jar-file <api key>");
            System.exit(1);
        }
        
        final Injector injector = Guice.createInjector(new CollectorModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(PollingJobActivator.class).asEagerSingleton();
                bind(UploadJobActivator.class).asEagerSingleton();
                bind(String.class)
                        .annotatedWith(Names.named("API_KEY"))
                        .toInstance(args[0]);
                bind(String.class)
                        .annotatedWith(Names.named("URL"))
                        .toInstance("http://localhost/sensor-web");
            }
        });
        
        addShutdownHook(injector);
    }

    private static void addShutdownHook(final Injector injector) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                injector.getInstance(Quartz.class).shutdown();
                injector.getInstance(ReadingsDb.class).shutdown();
            }
        });
    }
}
