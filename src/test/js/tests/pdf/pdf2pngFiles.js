import { strict as assert } from "assert";
import Canvas from "canvas";
import fs from "fs";
import { getDocument } from "pdfjs-dist/legacy/build/pdf.mjs";

class NodeCanvasFactory {
    create(width, height) {
        assert(width > 0 && height > 0, "Invalid canvas size");
        const canvas = Canvas.createCanvas(width, height);
        const context = canvas.getContext("2d");
        return {
            canvas,
            context,
        };
    }

    reset(canvasAndContext, width, height) {
        assert(canvasAndContext.canvas, "Canvas is not specified");
        assert(width > 0 && height > 0, "Invalid canvas size");
        canvasAndContext.canvas.width = width;
        canvasAndContext.canvas.height = height;
    }

    destroy(canvasAndContext) {
        assert(canvasAndContext.canvas, "Canvas is not specified");

        // Zeroing the width and height cause Firefox to release graphics
        // resources immediately, which can greatly reduce memory consumption.
        canvasAndContext.canvas.width = 0;
        canvasAndContext.canvas.height = 0;
        canvasAndContext.canvas = null;
        canvasAndContext.context = null;
    }
}

async function renderPDFToImageFiles(pdfPath, outputDirectory) {
    const images = [];
    const filenames = [];
    const canvasFactory = new NodeCanvasFactory();

    // Some PDFs need external cmaps.
    const CMAP_URL = "../../../node_modules/pdfjs-dist/cmaps/";
    const CMAP_PACKED = true;

    // Where the standard fonts are located.
    const STANDARD_FONT_DATA_URL =
        "../../../node_modules/pdfjs-dist/standard_fonts/";

    const loadingTask = getDocument({
        url: pdfPath,
        cMapUrl: CMAP_URL,
        cMapPacked: CMAP_PACKED,
        standardFontDataUrl: STANDARD_FONT_DATA_URL,
        canvasFactory: canvasFactory,
    });

    try {
        const pdfDocument = await loadingTask.promise;
        console.log(`# PDF document have ${pdfDocument.numPages} pages loaded`);
        for (let pageNum = 1; pageNum <= pdfDocument.numPages; pageNum++) {
            // Get the first page.
            const page = await pdfDocument.getPage(pageNum);
            // Render the page on a Node canvas with 100% scale.
            const viewport = page.getViewport({ scale: 1.0 });
            const canvasAndContext = canvasFactory.create(
                viewport.width,
                viewport.height
            );
            const renderContext = {
                canvasContext: canvasAndContext.context,
                viewport,
            };

            const renderTask = page.render(renderContext);
            await renderTask.promise;
            // Convert the canvas to an image buffer.
            const image = canvasAndContext.canvas.toBuffer();
            const filename = `${outputDirectory}/output-${pageNum}.png`;
            fs.writeFileSync(filename, image);
            filenames.push(filename);
            console.log(
                `Finished converting page number ${pageNum} of PDF file with pages ${pdfDocument.numPages} to a PNG image.`
            );
            // Release page resources.
            page.cleanup();
        }
        console.log("All pages converted to images.");
        return filenames;
    } catch (reason) {
        console.error("Error:", reason);
        return [];
    }
}

export { renderPDFToImageFiles };
