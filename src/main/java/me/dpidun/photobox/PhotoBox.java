package me.dpidun.photobox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhotoBox {

    public static void main(String[] args) {
        SpringApplication.run(PhotoBox.class, args);
    }

}
