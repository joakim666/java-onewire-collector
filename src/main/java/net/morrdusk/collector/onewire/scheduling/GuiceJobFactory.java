package net.morrdusk.collector.onewire.scheduling;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

public class GuiceJobFactory implements JobFactory {

    private final Injector guice;

    @Inject
    public GuiceJobFactory(final Injector guice) {
        this.guice = guice;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        final JobDetail jobDetail = bundle.getJobDetail();
        final Class<? extends Job> jobClass = jobDetail.getJobClass();
        return (Job) guice.getInstance(jobClass);
    }
}
