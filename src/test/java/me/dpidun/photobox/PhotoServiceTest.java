package me.dpidun.photobox;

import me.dpidun.photobox.photo.*;
import org.exparity.hamcrest.date.DateMatchers;
import org.h2.store.fs.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PhotoServiceTest {

    private static final String AN_URI = "AN_URI/";
    private static final String A_FILE_SUPPORTED_SUFFIX = ".PNG";
    private static final String ANOTHER_FILE_SUPPORTED_SUFFIX = ".JPG";
    private static final String ANOTHER_OTHER_FILE_SUPPORTED_SUFFIX = ".JPEG";
    private static final String A_FILE_UNSUPPORTED_SUFFIX = ".GIF";
    private static final String A_FILE_PREFIX = "A_FILE_PREFIX";
    private static final String ANOTHER_URI = "ANOTHER_URI/";
    private PhotoService unitUnderTest;
    private PhotoRepository photoRepository;

    private String A_FILE_NAME;

    @Before
    public void setUp() throws IOException {
        photoRepository = mock(PhotoRepository.class);
        unitUnderTest = new PhotoService(photoRepository);

        File a_SUPPORTED_FILE = File.createTempFile(A_FILE_PREFIX, A_FILE_SUPPORTED_SUFFIX);
        a_SUPPORTED_FILE.deleteOnExit();

        A_FILE_NAME = FileUtils.getName(a_SUPPORTED_FILE.getAbsolutePath());
    }

    @Test
    public void getPhotos_shouldReturnPhotos() {

        Photo photo = new Photo(AN_URI);
        photo.setProcessingStatus(ProcessingStatus.FINISHED);
        when(photoRepository.findAll()).thenReturn(Collections.singletonList(photo));

        List<PhotoListItem> actual = unitUnderTest.getPhotos();
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getUri(), is(AN_URI));
    }

    @Test
    public void getPhotos_withProcessingPhotos_shouldReturnSuccessfulPhotos() {
        when(photoRepository.findAll()).thenReturn(Arrays.asList(
                new Photo(1L, AN_URI, new Date(), ProcessingStatus.CREATED),
                new Photo(2L, AN_URI, new Date(), ProcessingStatus.PROCESSING_STEP_FINISHING),
                new Photo(3L, AN_URI, new Date(), ProcessingStatus.PROCESSING_STEP_THUMBNAIL),
                new Photo(4L, AN_URI, new Date(), ProcessingStatus.PROCESSING_STEP_WATERMARK),
                new Photo(5L, AN_URI, new Date(), ProcessingStatus.FAILED),
                new Photo(6L, ANOTHER_URI, new Date(), ProcessingStatus.FINISHED)));

        List<PhotoListItem> actual = unitUnderTest.getPhotos();
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getUri(), is(ANOTHER_URI));

    }

    @Test(expected = PhotoAlreadyExistsException.class)
    public void addPhoto_alreadyExisting_shouldThrowPhotoAlreadyExistsException() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(photoRepository.existsByFileName(stringArgumentCaptor.capture())).thenReturn(true);

        try {
            unitUnderTest.addPhoto(A_FILE_NAME);
        } finally {
            assertThat(stringArgumentCaptor.getValue(), startsWith(A_FILE_PREFIX));
            assertThat(stringArgumentCaptor.getValue(), endsWith(A_FILE_SUPPORTED_SUFFIX));
        }
    }

    @Test(expected = FileTypeNotSupportedException.class)
    public void addPhoto_withNoJpegOrPngFileExtension_shouldThrowFileTypeNotSupportedException() {
        unitUnderTest.addPhoto(A_FILE_PREFIX + A_FILE_UNSUPPORTED_SUFFIX);
    }

    @Test
    public void addPhoto_withJpegOrPngFileExtension_shouldCheckExistence() {
        when(photoRepository.existsByFileName(anyString())).thenReturn(false);

        unitUnderTest.addPhoto(A_FILE_PREFIX + ANOTHER_FILE_SUPPORTED_SUFFIX);
        unitUnderTest.addPhoto(A_FILE_PREFIX + ANOTHER_OTHER_FILE_SUPPORTED_SUFFIX);
        unitUnderTest.addPhoto(A_FILE_PREFIX + A_FILE_SUPPORTED_SUFFIX);
    }

    @Test
    public void addPhoto_shouldAddPhoto() {
        when(photoRepository.existsByFileName(anyString())).thenReturn(false);

        unitUnderTest.addPhoto(A_FILE_NAME);

        ArgumentCaptor<Photo> photoArgumentCaptor = ArgumentCaptor.forClass(Photo.class);
        verify(photoRepository).save(photoArgumentCaptor.capture());

        assertThat(photoArgumentCaptor.getValue().getFileName(), startsWith(A_FILE_PREFIX));
        assertThat(photoArgumentCaptor.getValue().getFileName(), endsWith(A_FILE_SUPPORTED_SUFFIX));
        assertThat(photoArgumentCaptor.getValue().getCreatedAt(), DateMatchers.within(20, ChronoUnit.SECONDS, new Date()));
    }
}