package me.dpidun.photobox;

import org.h2.store.fs.FileUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {


    private PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> getPhotos() {
        return photoRepository
                .findAll()
                .stream()
                .filter((photo -> photo.getProcessingStatus().equals(ProcessingStatus.FINISHED)))
                .collect(Collectors.toList());
    }

    public void addPhoto(String fileName) {

        if (photoRepository.existsByFileName(fileName)) {
            throw new PhotoAlreadyExistsException();
        }

        Photo photo = new Photo(fileName);
        photo.setCreatedAt(new Date());
        photoRepository.save(photo);
    }
}
