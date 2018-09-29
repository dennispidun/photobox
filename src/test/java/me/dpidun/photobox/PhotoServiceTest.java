package me.dpidun.photobox;

import org.exparity.hamcrest.date.DateMatchers;
import org.h2.store.fs.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class PhotoServiceTest {

    public static final String AN_URI = "AN_URI/";
    public static final String A_FILE_SUFFIX = ".PNG";
    public static final String A_FILE_PREFIX = "A_FILE_PREFIX";
    PhotoService unitUnderTest;
    PhotoRepository photoRepository;

    private File A_FILE;
    private String A_FILE_NAME;

    @Before
    public void setUp() throws IOException {
        photoRepository = mock(PhotoRepository.class);
        unitUnderTest = new PhotoService(photoRepository);

        A_FILE = File.createTempFile(A_FILE_PREFIX, A_FILE_SUFFIX);
        A_FILE.deleteOnExit();

        A_FILE_NAME = FileUtils.getName(A_FILE.getAbsolutePath());
    }

    @Test
    public void getPhotos_shouldReturnPhotos() {

        when(photoRepository.findAll()).thenReturn(Collections.singletonList(new Photo(AN_URI)));

        List<Photo> actual = unitUnderTest.getPhotos();
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).fileName, is(AN_URI));
    }

    @Test(expected = PhotoAlreadyExistsException.class)
    public void addPhoto_alreadyExisting_shouldThrowPhotoAlreadyExistsException() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(photoRepository.existsByFileName(stringArgumentCaptor.capture())).thenReturn(true);

        try {
            unitUnderTest.addPhoto(A_FILE_NAME);
        } finally {
            assertThat(stringArgumentCaptor.getValue(), startsWith(A_FILE_PREFIX));
            assertThat(stringArgumentCaptor.getValue(), endsWith(A_FILE_SUFFIX));
        }
    }

    @Test
    public void addPhoto_shouldAddPhoto() {
        when(photoRepository.existsByFileName(anyString())).thenReturn(false);

        unitUnderTest.addPhoto(A_FILE_NAME);

        ArgumentCaptor<Photo> photoArgumentCaptor = ArgumentCaptor.forClass(Photo.class);
        verify(photoRepository).save(photoArgumentCaptor.capture());

        assertThat(photoArgumentCaptor.getValue().fileName, startsWith(A_FILE_PREFIX));
        assertThat(photoArgumentCaptor.getValue().fileName, endsWith(A_FILE_SUFFIX));
        assertThat(photoArgumentCaptor.getValue().createdAt, DateMatchers.within(20, ChronoUnit.SECONDS, new Date()));
    }
}