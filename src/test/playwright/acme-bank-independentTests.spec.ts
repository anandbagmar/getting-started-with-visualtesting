import { test } from '@playwright/test';
import { BatchInfo, Configuration, EyesRunner, VisualGridRunner, BrowserType, DeviceName, ScreenOrientation, Eyes, Target } from '@applitools/eyes-playwright';

let Batch: BatchInfo;
let Config: Configuration;
const appName = 'ACME Bank';

test.beforeAll(async() => {
    // Create a new Batch
    Batch = new BatchInfo({name: `Playwright Typescript - Independent Tests`});
    Batch.setNotifyOnCompletion(false);

    // Configure Applitools SDK to run on the Ultrafast Grid
    Config = new Configuration();
    Config.setBatch(Batch);
    Config.addBrowsers(
        { name: BrowserType.CHROME, width: 800, height: 600 },
        { name: BrowserType.FIREFOX, width: 1600, height: 1200 },
        { name: BrowserType.SAFARI, width: 1024, height: 768 },
        { chromeEmulationInfo: { deviceName: DeviceName.iPhone_11, screenOrientation: ScreenOrientation.PORTRAIT} },
        { chromeEmulationInfo: { deviceName: DeviceName.Nexus_10, screenOrientation: ScreenOrientation.LANDSCAPE} }
    )
});

test.describe('ACME Bank', () => {
    let eyes: Eyes;
    let Runner: EyesRunner;
    test.beforeEach(async ({ page }) => {
        console.log("beforeEach -", test.info().title);
        Runner = new VisualGridRunner({ testConcurrency: 5 });
        eyes = new Eyes(Runner, Config);

        // Start Applitools Visual AI Test
        // Args: Playwright Page, App Name, Test Name, Viewport Size for local driver
        await eyes.open(page, appName, `${test.info().title}`, { width: 1200, height: 600 })
    });

    test('log into a bank account', async ({ page }) => {
        await page.goto('https://sandbox.applitools.com/bank?layoutAlgo=true');

        // Full Page - Visual AI Assertion
        await eyes.check('Login page', Target.window().fully());

        await page.locator('id=username').fill('user');
        await page.locator('id=password').fill('password');
        await page.locator('id=log-in').click();
        await page.locator('css=.dashboardNav_navContainer__kA4wD').waitFor({state: 'attached'});

        // Full Page - Visual AI Assertion
        await eyes.check('Main page', Target.window().fully()
            // Uncomment to apply Layout regions and have test pass
            /* .layoutRegions(
                '.dashboardOverview_accountBalances__3TUPB',
                '.dashboardTable_dbTable___R5Du'
            ) */
        );
    });

    test('log into a bank account-layout', async ({ page }) => {
        await page.goto('https://sandbox.applitools.com/bank?layoutAlgo=true');

        // Full Page - Visual AI Assertion
        await eyes.check('Login page', Target.window().fully());

        await page.locator('id=username').fill('user');
        await page.locator('id=password').fill('password');
        await page.locator('id=log-in').click();
        await page.locator('css=.dashboardNav_navContainer__kA4wD').waitFor({state: 'attached'});

        // Full Page - Visual AI Assertion
        await eyes.check('Main page', Target.window().fully()
            .layoutRegions(
                '.dashboardOverview_accountBalances__3TUPB',
                '.dashboardTable_dbTable___R5Du'
            )
        );
    });

    test.afterEach(async () => {
        console.log("afterEach -", test.info().title);
        // End Applitools Visual AI Test
        await eyes.closeAsync();
        // Wait for Ultrast Grid Renders to finish and gather results
        const results = await Runner.getAllTestResults(false);
        console.log('Visual test results', results);
    });
});

test.afterAll(async() => {
    Batch.setNotifyOnCompletion(true);
    // Wait for Ultrast Grid Renders to finish and gather results
    // const results = await Runner.getAllTestResults();
    // console.log('Visual test results', results);
    console.log("afterAll")
});
