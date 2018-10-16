package me.dpidun.photobox;

import me.dpidun.photobox.photo.PhotoService;
import me.dpidun.photobox.scheduler.PhotoWatcherScheduler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.*;

public class PhotoWatcherSchedulerTest {

    public static final String A_SUFFIX = "A_SUFFIX.PNG";
    public static final String A_PREFIX = "A_PREFIX";

    private PhotoWatcherScheduler unitUnderTest;
    private PhotoService mockPhotoService;
    private WatchService mockWatcher;

    @Before
    public void setUp() {
        mockWatcher = mock(WatchService.class);
        mockPhotoService = mock(PhotoService.class);

        unitUnderTest = new PhotoWatcherScheduler(mockWatcher, mockPhotoService);
    }

    @Test
    public void watchPhotos_shouldDetectChangedPhotos() throws Exception {
        mockWatchService();

        unitUnderTest.watchPhotos();

        ArgumentCaptor<String> fileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(mockPhotoService).addPhoto(fileNameArgumentCaptor.capture());

        assertThat(fileNameArgumentCaptor.getValue(), startsWith(A_PREFIX));
        assertThat(fileNameArgumentCaptor.getValue(), endsWith(A_SUFFIX));
    }

    private void mockWatchService() throws InterruptedException {
        WatchKey key = mock(WatchKey.class);
        WatchEvent watchEvent = mock(WatchEvent.class);

        when(watchEvent.kind()).thenReturn(ENTRY_CREATE);
        when(watchEvent.context()).thenReturn(Paths.get(A_PREFIX, A_SUFFIX));

        when(key.pollEvents()).thenReturn(Collections.singletonList(watchEvent));
        when(mockWatcher.take()).thenReturn(key);
    }
}