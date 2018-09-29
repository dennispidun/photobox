package me.dpidun.photobox.photo;

import me.dpidun.photobox.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    boolean existsByFileName(String fileName);
}
