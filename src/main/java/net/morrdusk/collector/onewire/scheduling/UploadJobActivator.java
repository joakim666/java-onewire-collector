package net.morrdusk.collector.onewire.scheduling;

import com.google.inject.Inject;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadJobActivator {
    private static final Logger LOG = LoggerFactory.getLogger(UploadJobActivator.class);

    @Inject
    public UploadJobActivator(final Quartz quartz) throws SchedulerException {
        LOG.info("Registering uploadJob");
        JobDetail uploadJob = JobBuilder.newJob(UploadJob.class).withIdentity("uploadJob").build();

        quartz.getScheduler().addJob(uploadJob, true);
    }

}
