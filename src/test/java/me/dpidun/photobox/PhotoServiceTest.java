package me.dpidun.photobox;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PhotoServiceTest {

    public static final String AN_URI = "AN_URI/";
    PhotoService unitUnderTest;
    PhotoRepository photoRepository;

    @Before
    public void setUp() {
        photoRepository = mock(PhotoRepository.class);
        unitUnderTest = new PhotoService(photoRepository);
    }

    @Test
    public void getPhotos_shouldReturnPhotos() {

        when(photoRepository.findAll()).thenReturn(Collections.singletonList(new Photo(AN_URI)));

        List<Photo> actual= unitUnderTest.getPhotos();
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).uri, is(AN_URI));
    }
}