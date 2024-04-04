package tests.pdf;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.images.Eyes;
import com.applitools.eyes.images.ImageRunner;
import com.applitools.eyes.images.Target;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class PDFJavaTest {

    private static ImageRunner runner = new ImageRunner();
    private static String APP_NAME = PDFJavaTest.class.getSimpleName();
    private static Configuration config = new Configuration();
    private static BatchInfo batch;

    @Test
    void validatePDF() {
        batch = new BatchInfo(APP_NAME);
        Eyes imagesEyes = new Eyes(runner);
        config.setHostApp(APP_NAME);
        config.setAppName(APP_NAME);
        config.setBatch(batch);
        imagesEyes.setConfiguration(config);

        try {
            imagesEyes.open(APP_NAME, "PDF test Java");

            String pdfPath = "./src/test/resources/sample-demo.pdf";

            try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfPath))) {
                PDFRenderer renderer = new PDFRenderer(document);

                for (int pageNum = 0; pageNum < document.getNumberOfPages(); pageNum++) {
                    BufferedImage image = renderer.renderImage(pageNum); // DPI (dots per inch)
                    System.out.println("Image created for page " + (pageNum + 1));
                    imagesEyes.check("Local File Image buffer", Target.image(image));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            imagesEyes.closeAsync();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            System.out.println("allTestResults: " + allTestResults);
            imagesEyes.abortIfNotClosed();
            runner.close();
        }
    }
}
