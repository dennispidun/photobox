package me.dpidun.photobox.photo;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ImageLocationServiceTest {


    private static final String AN_IMAGE_PATH = "AN/IMAGE/PATH";
    private ImageLocationService unitUnderTest;

    @Before
    public void setUp() throws Exception {
        unitUnderTest = new ImageLocationService(AN_IMAGE_PATH);
    }

    @Test
    public void getImageLocation_shouldReturnImageLocation() {
        assertThat(unitUnderTest.getImageLocation(), is(AN_IMAGE_PATH));
    }

    @Test
    public void getImageLocation_withNoLocation_shouldReturnDefaulLocation() {
        unitUnderTest = new ImageLocationService(null);

        File tempLocation = new File("").getAbsoluteFile();
        String actualPath = tempLocation.getAbsolutePath();

        assertThat(unitUnderTest.getImageLocation(), is(actualPath));
    }


}