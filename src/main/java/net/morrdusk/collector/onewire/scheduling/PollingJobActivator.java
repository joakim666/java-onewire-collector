package net.morrdusk.collector.onewire.scheduling;

import com.google.inject.Inject;
import org.quartz.*;

public class PollingJobActivator {
    @Inject
    public PollingJobActivator(final Quartz quartz) throws SchedulerException {
        JobDetail pollingJob = JobBuilder.newJob(PollingJob.class).withIdentity("pollingJob").build();

        // every minute
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger").withSchedule(cronScheduleBuilder).build();

        quartz.getScheduler().scheduleJob(pollingJob, cronTrigger);
    }
}
