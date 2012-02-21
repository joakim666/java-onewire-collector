package net.morrdusk.collector.onewire.scheduling;

import com.google.inject.Inject;
import org.quartz.*;

public class UploadJobActivator {
    @Inject
    public UploadJobActivator(final Quartz quartz) throws SchedulerException {
        JobDetail uploadJob = JobBuilder.newJob(UploadJob.class).withIdentity("uploadJob").build();

        quartz.getScheduler().addJob(uploadJob, true);
    }

}
