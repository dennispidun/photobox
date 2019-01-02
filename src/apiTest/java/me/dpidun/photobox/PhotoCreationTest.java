package me.dpidun.photobox;

import me.dpidun.photobox.photo.ImageLocationService;
import me.dpidun.photobox.photo.PhotoRepository;
import org.h2.store.fs.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PhotoCreationTest {

    public static final String A_SUFFIX = "A_SUFFIX.PNG";
    public static final String A_PREFIX = "A_PREFIX";

    @Autowired
    private ImageLocationService imageLocationService;

    @Autowired
    private PhotoRepository repository;

    @Test
    public void createFile_addsPhotoToDatabase() throws IOException {
        Path path = Paths.get(imageLocationService.getImageLocation());
        long count = repository.count();

        File testFile = Files.createTempFile(path, A_PREFIX, A_SUFFIX).toFile();
        testFile.deleteOnExit();
        String fileName = FileUtils.getName(testFile.getAbsolutePath());
        await().atMost(5000, TimeUnit.SECONDS).until(() -> repository.existsByFileName(fileName));
    }
}
