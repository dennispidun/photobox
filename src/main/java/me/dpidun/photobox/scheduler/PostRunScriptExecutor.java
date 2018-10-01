package me.dpidun.photobox.scheduler;

import me.dpidun.photobox.photo.PhotoService;
import org.h2.store.fs.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;

@Component
public class PostRunScriptExecutor {

    private final PhotoService photoService;

    private final String imagePath;

    public PostRunScriptExecutor(PhotoService photoService, @Value("${images.path}") String imagePath) {
        this.photoService = photoService;
        this.imagePath = imagePath;
    }

    @PostConstruct
    public void importOnStartup() {
        File folder = new File(imagePath);
        try {
            Arrays.stream(folder.listFiles()).forEach(file
                -> photoService.addPhoto(FileUtils.getName(file.getAbsolutePath())));
        } catch (Exception e) { }
    }
}
