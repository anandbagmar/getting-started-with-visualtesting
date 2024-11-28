import { Eyes, Target, Configuration, BatchInfo } from '@applitools/eyes-images';

describe('Eyes-Images', () => {
    let eyes
    let batchInfo
    let configuration

    beforeEach(() => {
        eyes = new Eyes()

        // Initialize the eyes configuration
        const configuration = new Configuration();

        // You can get your api key from the Applitools dashboard
        // configuration.setApiKey('APPLITOOLS_API_KEY')

        // Set new batch
        configuration.setBatch(new BatchInfo('ImagesJSTest'))

        // Set the configuration to eyes
        eyes.setConfiguration(configuration);
    })

    afterEach(async () => {
        await eyes.abortIfNotClosed();
    })

    it('Images test', async () => {
        await eyes.open('Applitools site', 'Images test')

        let imageUrl = 'https://i.ibb.co/bJgzfb3/applitools.png';
        await eyes.check('URL', Target.image(imageUrl))
        await eyes.check('File path', Target.path('./src/test/resources/applitools.png'))

        const imageBuffer = await fetch('https://i.ibb.co/bJgzfb3/applitools.png').then(resp => resp.arrayBuffer())
        await eyes.check('Buffer', Target.image(imageBuffer));

        await eyes.close()
    })
})
