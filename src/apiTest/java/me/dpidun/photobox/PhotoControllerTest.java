package me.dpidun.photobox;

import me.dpidun.photobox.photo.Photo;
import me.dpidun.photobox.photo.PhotoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PhotoControllerTest {

    private static final String A_FILE_PNG = "A_FILE.PNG";
    private static final String A_PATH = "A_PATH/";
    private static final String AN_URI = A_PATH + A_FILE_PNG;
    private static final long A_TIMESTAMP = 1234L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhotoRepository repository;

    @Before
    public void setUp() {
        Photo entity = new Photo(AN_URI);
        entity.setProcessingStatus(Photo.ProcessingStatus.FINISHED);
        entity.setCreatedAt(new Date(A_TIMESTAMP));
        repository.save(entity);
    }

    @Test
    public void getPhotos_shouldRespondWithPhotos() throws Exception {
        mockMvc.perform(get("/api/photos"))
                .andExpect(jsonPath("$[0].uri", endsWith(AN_URI)))
                .andExpect(status().isOk());
    }
}
