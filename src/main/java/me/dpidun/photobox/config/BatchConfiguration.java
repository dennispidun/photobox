package me.dpidun.photobox.config;

import me.dpidun.photobox.photo.PhotoRepository;
import me.dpidun.photobox.photo.imports.AvailabilityCheckImportStep;
import me.dpidun.photobox.photo.imports.ImportFailedJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Bean
    public JobLauncher getJobLauncher(JobRepository jobRepository, TaskExecutor taskExecutor) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public TaskExecutor getTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean
    public JobRepository getJobRepository(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean("importJob")
    public Job getImportJob(
            JobRepository jobRepository,
            AvailabilityCheckImportStep availabilityCheckImportStep,
            ImportFailedJobExecutionListener importFailedJobExecutionListener
    ) {
        SimpleJob job = new SimpleJob("import");
        job.setJobRepository(jobRepository);
        job.addStep(availabilityCheckImportStep);
        job.registerJobExecutionListener(importFailedJobExecutionListener);

        return job;
    }

    @Bean
    public AvailabilityCheckImportStep getImportStep(PhotoRepository photoRepository, JobRepository jobRepository, @Value("${images.path}") String imagePath) {
        AvailabilityCheckImportStep availabilityCheckImportStep = new AvailabilityCheckImportStep(photoRepository, imagePath);
        availabilityCheckImportStep.setName("checkPhotoAvailability");
        availabilityCheckImportStep.setJobRepository(jobRepository);

        return availabilityCheckImportStep;
    }

    @Bean
    public ImportFailedJobExecutionListener getFailedJobExecutionListener(PhotoRepository photoRepository) {
        return new ImportFailedJobExecutionListener(photoRepository);
    }

    private PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }
}
