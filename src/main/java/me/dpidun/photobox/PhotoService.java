package me.dpidun.photobox;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PhotoService {


    private PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> getPhotos() {
        return photoRepository.findAll();
    }
}
