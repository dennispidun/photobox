package me.dpidun.photobox.photo.imports;

import me.dpidun.photobox.photo.Photo;
import me.dpidun.photobox.photo.PhotoRepository;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.AbstractStep;

public class AvailabilityCheckImportStep extends AbstractStep {
    public static final String PARAMETER_PHOTO_ID = "photoId";
    private final PhotoRepository photoRepository;

    public AvailabilityCheckImportStep(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    protected void doExecute(StepExecution stepExecution) {
        long photoId = stepExecution.getJobParameters().getLong(PARAMETER_PHOTO_ID);
        if (photoId == 0) {
            throw new IllegalArgumentException();
        }

        Photo photo = photoRepository
                .findById(photoId)
                .orElseThrow(PhotoNotFoundException::new);

        photo.setProcessingStatus(Photo.ProcessingStatus.CREATED);
    }
}