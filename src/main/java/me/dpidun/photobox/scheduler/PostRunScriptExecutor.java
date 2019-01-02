package me.dpidun.photobox.scheduler;

import me.dpidun.photobox.photo.ImageLocationService;
import me.dpidun.photobox.photo.PhotoService;
import org.h2.store.fs.FileUtils;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;

@Component
public class PostRunScriptExecutor {

    private final PhotoService photoService;
    private final ImageLocationService imageLocationService;

    public PostRunScriptExecutor(PhotoService photoService, ImageLocationService imageLocationService) {
        this.photoService = photoService;
        this.imageLocationService = imageLocationService;
    }

    @PostConstruct
    public void importOnStartup() {
        File folder = new File(imageLocationService.getImageLocation());
        try {
            Arrays.stream(folder.listFiles()).forEach(file
                    -> {
                try {
                    photoService.addPhoto(FileUtils.getName(file.getAbsolutePath()));
                } catch (JobParametersInvalidException
                        | JobExecutionAlreadyRunningException
                        | JobInstanceAlreadyCompleteException
                        | JobRestartException e) {
                }
            });
        } catch (Exception e) {
        }
    }
}
