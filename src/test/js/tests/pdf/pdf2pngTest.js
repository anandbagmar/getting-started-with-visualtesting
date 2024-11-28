import { renderPDFToImageFiles } from './pdf2pngFiles.js'; // Import the function to test
import { strict as assert } from 'assert';
import { Eyes, Target, Configuration, BatchInfo } from '@applitools/eyes-images';
import fs from 'fs';

describe('PDF Rendering Test', function() {
    let eyes

    beforeEach(() => {
        eyes = new Eyes()

        // Initialize the eyes configuration
        const configuration = new Configuration();

        // You can get your api key from the Applitools dashboard
        // configuration.setApiKey('APPLITOOLS_API_KEY')

        // Set new batch
        configuration.setBatch(new BatchInfo('PDFJSTest'))

        // Set the configuration to eyes
        eyes.setConfiguration(configuration);
    })

    it('should render PDF and return array of images', async function() {
        await eyes.open('PDFJSTest', 'PDF JS Test');

        const dateTime = getCurrentDateTime();
        console.log("Current Date and Time:", dateTime);
        const directoryPath = `./reports/${dateTime}`;

        await createDirectory(directoryPath);

        const pdfPath = './src/test/resources/sample-demo.pdf'; // Replace with the path to your PDF file
        console.log(`Validate pdf: ${pdfPath}`);

        const listOfSavedImageFileNames = await renderPDFToImageFiles(pdfPath, `${directoryPath}`);

        // Assert that images is an array
        assert(Array.isArray(listOfSavedImageFileNames), 'listOfSavedImageFileNames should be an array');

        // Assert that images array is not empty
        assert(listOfSavedImageFileNames.length > 0, 'listOfSavedImageFileNames array should not be empty');

        // Iterate over the images array
        // images.forEach((image, index) => {
        for (let pageNum = 0; pageNum < listOfSavedImageFileNames.length; pageNum++) {
            console.log(`Validating file: ${pageNum+1}: ${listOfSavedImageFileNames[pageNum]}`)
            await eyes.check(`page-${pageNum+1}`, Target.image(`${listOfSavedImageFileNames[pageNum]}`));
        }

        console.log('Finished rendering and inspecting PDF images.');
        let testResults = await eyes.close();
        console.log(`Eyes validation results: ${testResults}`)
    });

    afterEach(async () => {
        await eyes.abortIfNotClosed();
    })

    function getCurrentDateTime() {
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        const currentDate = new Date();

        // Get the current date
        const year = currentDate.getFullYear();
        const month = months[currentDate.getMonth()];
        const day = String(currentDate.getDate()).padStart(2, '0');

        // Get the current time
        const hours = String(currentDate.getHours()).padStart(2, '0');
        const minutes = String(currentDate.getMinutes()).padStart(2, '0');
        const seconds = String(currentDate.getSeconds()).padStart(2, '0');

        // Format the output
        const datePart = `${month}-${year}`;
        const timePart = `${day}-${month}-${year}/${hours}-${minutes}-${seconds}`;

        // Return the combined output
        return `${datePart}/${timePart}`;
    }

    function createDirectory(path) {
        try {
            fs.mkdirSync(path, { recursive: true });
            console.log(`Directory created successfully at ${path}`);
        } catch (err) {
            console.error(`Error creating directory: ${err}`);
        }
    }

});
