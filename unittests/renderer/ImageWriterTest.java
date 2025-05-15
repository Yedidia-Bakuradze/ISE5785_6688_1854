package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Represents the test cases related to image writing
 */
public class ImageWriterTest {

    /**
     * Default constructor to disable the warning in the JavaDoc generator
     */
    ImageWriterTest() {
    }

    /**
     * This test made for testing a build of a main window
     * It's ViewPlane: 10X16 squares
     * It's regulation: 800X500
     * Background color: Red for bordering, Yellow for actual main color
     * Test the {@link ImageWriter#writeToImage(String)} and {@link ImageWriter#writeToImage(String)}
     */
    @Test
    void testBuildMainImage() {
        assertDoesNotThrow(() -> {
            Color mainColor = new Color(java.awt.Color.YELLOW);
            Color borderColor = new Color(java.awt.Color.RED);
            int x = 801;
            int y = 501;
            ImageWriter imageWriter = new ImageWriter(x, y);
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    imageWriter.writePixel(j, i, i % 50 == 0 || j % 50 == 0 ? borderColor : mainColor);
                }
            }
            imageWriter.writeToImage("Basic Background and Border Image");
        }, "Failed to create image");
    }
}
