package me.dpidun.photobox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
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

                photoService.addPhoto(filename.toString());
            }
        }
        key.reset();
    }
}
