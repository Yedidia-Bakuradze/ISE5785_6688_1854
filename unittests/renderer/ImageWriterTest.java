package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class ImageWriterTest {

    /*
    * This test made for testing a build of a main window
    * It's ViewPlane: 10X16 squares
    * It's regulation: 800X500
    * Background color: Red for bordering, Yellow for actual main color
     */
    @Test
    void testBuildMainImage(){
        assertDoesNotThrow(() -> {
            Color mainColor = new Color(java.awt.Color.YELLOW);
            Color borderColor = new Color(java.awt.Color.RED);
            ImageWriter imageWriter = new ImageWriter(800, 500);
            for (int i = 0; i < 800; i++) {
                for (int j = 0; j < 500; j++) {
                    imageWriter.writePixel(i, j, i % 50 == 0 || j % 50 == 0 ? borderColor : mainColor);
                }
            }
            imageWriter.writeToImage("firstImage");
        }, "Failed to create image");
    }
}
