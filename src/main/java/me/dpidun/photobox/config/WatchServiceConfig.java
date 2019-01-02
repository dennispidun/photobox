package me.dpidun.photobox.config;

import me.dpidun.photobox.photo.ImageLocationService;
import me.dpidun.photobox.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@Configuration
public class WatchServiceConfig {

    @Autowired
    private ImageLocationService imageLocationService;

    @Bean
    public WatchService getWatchService() throws IOException {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path logDir = Paths.get(imageLocationService.getImageLocation());
        logDir.register(watcher, ENTRY_CREATE);
        return watcher;
    }
}
