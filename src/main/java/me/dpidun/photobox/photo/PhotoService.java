package me.dpidun.photobox.photo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.dpidun.photobox.photo.imports.AvailabilityCheckImportStep.PARAMETER_PHOTO_ID;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;
    private JobLauncher jobLauncher;
    private Job importJob;

    public PhotoService(PhotoRepository photoRepository, JobLauncher jobLauncher,
        @Qualifier("importJob") Job importJob) {
        this.photoRepository = photoRepository;
        this.jobLauncher = jobLauncher;
        this.importJob = importJob;
    }

    public List<PhotoListItem> getPhotos() {
        return photoRepository
                .findAll()
                .stream()
                .filter((photo -> photo.getProcessingStatus().equals(Photo.ProcessingStatus.FINISHED)))
                .map((photo -> new PhotoListItem("assets/photos/" + photo.fileName, photo.createdAt)))
                .collect(Collectors.toList());
    }

    public void addPhoto(String fileName) throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        if (!fileName.toLowerCase().endsWith(".png")
                && !fileName.toLowerCase().endsWith(".jpg")
                && !fileName.toLowerCase().endsWith(".jpeg")) {
            throw new FileTypeNotSupportedException();
        }

        if (photoRepository.existsByFileName(fileName)) {
            throw new PhotoAlreadyExistsException();
        }

        Photo photo = new Photo(fileName);
        photo.setCreatedAt(new Date());
        photo.setProcessingStatus(Photo.ProcessingStatus.CREATED);

        photo = photoRepository.save(photo);

        Map<String, JobParameter> jobParameterMap = new HashMap<>();
        jobParameterMap.put(PARAMETER_PHOTO_ID, new JobParameter(photo.getId()));

        this.jobLauncher.run(this.importJob, new JobParameters(jobParameterMap));

    }
}
