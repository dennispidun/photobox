package me.dpidun.photobox.photo;

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

    public List<PhotoListItem> getPhotos() {
        return photoRepository
                .findAll()
                .stream()
                .filter((photo -> photo.getProcessingStatus().equals(Photo.ProcessingStatus.FINISHED)))
                .map((photo -> new PhotoListItem("assets/photos/"+photo.fileName, photo.createdAt)))
                .collect(Collectors.toList());
    }

    public void addPhoto(String fileName) {

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
        photoRepository.save(photo);
    }
}
