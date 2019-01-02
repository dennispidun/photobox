package me.dpidun.photobox.photo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ImageLocationService {

    private String imagePath;

    public ImageLocationService(@Value("${images.path:#{null}}") String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageLocation() {
        if (this.imagePath == null) {
            File tempLocation = new File("").getAbsoluteFile();
            this.imagePath = tempLocation.getAbsolutePath();
        }

        return this.imagePath;
    }

}
