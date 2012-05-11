package net.morrdusk.collector.onewire.scheduling;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

@Singleton
public class Quartz {

    private final Scheduler scheduler;

    @Inject
    public Quartz(final SchedulerFactory factory, final GuiceJobFactory jobFactory) throws SchedulerException {
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        scheduler.start();
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            // TODO fix
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
