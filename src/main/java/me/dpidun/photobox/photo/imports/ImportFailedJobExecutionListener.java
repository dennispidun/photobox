package me.dpidun.photobox.photo.imports;


import me.dpidun.photobox.photo.Photo;
import me.dpidun.photobox.photo.PhotoRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import static me.dpidun.photobox.photo.imports.AvailabilityCheckImportStep.PARAMETER_PHOTO_ID;

public class ImportFailedJobExecutionListener implements JobExecutionListener {

    private PhotoRepository photoRepository;

    public ImportFailedJobExecutionListener(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // noop
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (!jobExecution.getStatus().equals(BatchStatus.FAILED)) {
            return;
        }

        long photoId = jobExecution.getJobParameters().getLong(PARAMETER_PHOTO_ID);
        Photo photo = photoRepository
                .findById(photoId)
                .orElseThrow(PhotoNotFoundException::new);

        photo.setProcessingStatus(Photo.ProcessingStatus.FAILED);

        photoRepository.save(photo);
    }
}