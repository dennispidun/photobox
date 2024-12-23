package me.dpidun.photobox.scheduler;

import me.dpidun.photobox.photo.ImageLocationService;
import me.dpidun.photobox.photo.PhotoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PostRunScriptExecutorTest {

    public static final String A_FILE = "test.png";
    private String myPath;
    PostRunScriptExecutor unitUnderTest;
    PhotoService mockPhotoService;
    private File testFolder;

    @Before
    public void setUp() {
        testFolder = new File("test_folder");
        testFolder.mkdir();
        testFolder.deleteOnExit();

        myPath = testFolder.getAbsolutePath();

        mockPhotoService = mock(PhotoService.class);
        unitUnderTest = new PostRunScriptExecutor(mockPhotoService, new ImageLocationService(myPath));
    }

    @After
    public void tearDown() {
        if (testFolder.exists()) {
            testFolder.delete();
        }
    }

    @Test
    public void importOnStartup_shouldSaveImages() throws Exception {
        File testFile = new File(testFolder, A_FILE);
        testFile.createNewFile();
        testFile.deleteOnExit();

        unitUnderTest.importOnStartup();
        
        verify(mockPhotoService).addPhoto(A_FILE);
    }
}