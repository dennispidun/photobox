package me.dpidun.photobox.scheduler;

import me.dpidun.photobox.photo.PhotoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.springframework.data.util.CastUtils.cast;

@Component
public class PhotoWatcherScheduler {

    private final WatchService watchService;
    private final PhotoService photoService;

    public PhotoWatcherScheduler(WatchService watchService, PhotoService photoService) {
        this.watchService = watchService;
        this.photoService = photoService;
    }

    @Scheduled(fixedRate = 1000)
    public void watchPhotos() {

        WatchKey key;
        try {
            key = watchService.take();
        } catch (InterruptedException e) {
            return;
        }

        for (WatchEvent<?> event : key.pollEvents()) {
            if (event.kind() == ENTRY_CREATE) {

                WatchEvent<Path> ev = cast(event);
                Path filename = ev.context();
                try {
                    photoService.addPhoto(filename.toString());
                } catch (Exception e) { }

            }
        }
        key.reset();
    }
}
