package me.dpidun.photobox.photo.imports;

import me.dpidun.photobox.photo.Photo;
import me.dpidun.photobox.photo.PhotoRepository;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.AbstractStep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AvailabilityCheckImportStep extends AbstractStep {
    public static final String PARAMETER_PHOTO_ID = "photoId";
    private final PhotoRepository photoRepository;
    private final String imagePath;

    public AvailabilityCheckImportStep(PhotoRepository photoRepository, String imagePath) {
        this.photoRepository = photoRepository;
        this.imagePath = imagePath;
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

        photo.setProcessingStatus(Photo.ProcessingStatus.CHECKING_STEP_AVAILABILITY);

        photoRepository.save(photo);

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {

        }

        photo.setProcessingStatus(Photo.ProcessingStatus.FINISHED);

        photoRepository.save(photo);

    }

    public boolean isFileLocked(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            if (in!=null) in.close();
            return false;
        } catch (FileNotFoundException e) {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}