import com.applitools.eyes.*;
import com.applitools.eyes.fluent.Target;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;


public class Main {

    public static void main(String[] args) {
        Main m = new Main();
        m.testVG();
        System.exit(0);
    }

    private void testVG(){

        EyesRunner visualGridRunner = new VisualGridRunner(100);
        Configuration renderConfig = new Configuration();

        ChromeOptions cOptions = new ChromeOptions();
        //System.setProperty("webdriver.chrome.driver", "chromedriver");
        cOptions.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(cOptions);

        int i = 0;
        int randNum = getRand();
        long before;
        long startloop;




        BatchInfo batchInfo = new BatchInfo("MemTest VG" + randNum);
        batchInfo.setId("VGMEMTEST" + randNum);

        renderConfig
                .addBrowser(1400, 600, BrowserType.CHROME)
                .addBrowser(1400, 600, BrowserType.FIREFOX)
                .addBrowser(600, 600, BrowserType.EDGE)
                .addBrowser(1600, 600, BrowserType.EDGE)

                .addBrowser(600, 600, BrowserType.IE_10)
                .addBrowser(1600, 600, BrowserType.IE_10)

                .addBrowser(600, 600, BrowserType.IE_11)
                .addBrowser(1600, 600, BrowserType.IE_11)

                .addBrowser(1200 ,600, BrowserType.SAFARI)

                .setAppName("MemTest")
                .setServerUrl("https://eyesapi.applitools.com")
                .setBatch(batchInfo)
                .setMatchLevel(MatchLevel.STRICT)
                .setSendDom(true)
                .setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        Eyes eyes = new Eyes(visualGridRunner);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        FileLogger logHandler = new FileLogger("eyes_test.log", false, true);
        eyes.setLogHandler(logHandler);

        System.out.println("API KEY: " + eyes.getApiKey());
        startloop = System.currentTimeMillis();

        try {

            for(i=0;i<10;i++) {

                eyes.getLogger().log("************************* ITERATION " + String.format("%03d",(i+1)));
                Date date = new Date();
                System.out.println(new Timestamp(date.getTime()) + ": Iteration " + (i+1));
                before = System.currentTimeMillis();

                renderConfig.setTestName("Memtest " + String.format("%03d",(i+1)));
                eyes.setConfiguration(renderConfig);

                eyes.open(driver);

                driver.get("https://wikipedia.com/");
                eyes.check("Test 1", com.applitools.eyes.selenium.fluent.Target.window().fully());

                driver.get("https://www.collegeatlas.org");
                eyes.check("Test 2", com.applitools.eyes.selenium.fluent.Target.window().fully());

                driver.get("https://www.seia.org/");
                eyes.check("Test 3", com.applitools.eyes.selenium.fluent.Target.window().fully());

                driver.get("https://www.seia.org/about");
                eyes.check("Test 4", com.applitools.eyes.selenium.fluent.Target.window().fully());


                eyes.closeAsync();

                System.out.println("Completed Rendering in " + ((System.currentTimeMillis() - before)) / 1000 + " seconds");
                showMemory();
            }

            driver.quit();

            TestResultsSummary allTestResults = visualGridRunner.getAllTestResults(false);
            System.out.println("Completed all in " + ((System.currentTimeMillis() - startloop)) / 1000 + " seconds");

        } catch (Exception e) {
            eyes.abortIfNotClosed();
            driver.quit();
            e.printStackTrace();
        }

    }

    private static void testClassic(){
        EyesRunner runner = new ClassicRunner();
        Eyes eyes = new Eyes(runner);

        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        ChromeOptions cOptions = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        cOptions.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(cOptions);

        Integer i = 0;
        String batchId = "TEST7";
        BatchInfo bi = new BatchInfo("MemTest 7");
        bi.setId(batchId);
        eyes.setBatch(bi);

        // FileLogger logHandler = new FileLogger("eyes_test3.log", false, true);
        // eyes.setLogHandler(logHandler);

        try {

            for(i=0;i<3;i++) {
                System.out.println("**** Iteration " + (i+1) + " ****");

                eyes.open(driver, "Memtest", "Memtest " + String.format("%03d",(i+1)), new RectangleSize(1200, 800));

                //driver.get("http://localhost/~christopheremerson/simple.html");
                driver.get("https://www.gore.com/");

               // eyes.checkWindow("test");

                eyes.check("test", Target.window().fully());

                eyes.close(false);
                //TestResultsSummary allTestResults = runner.getAllTestResults();
                //System.out.println(allTestResults);
                showMemory();
            }

            driver.quit();

        } catch (Exception e) {
            eyes.abortIfNotClosed();
            driver.quit();
            e.printStackTrace();
        }

    }

    private static void showMemory(){

        int MegaBytes = 10241024;
        long freeMemory = Runtime.getRuntime().freeMemory()/MegaBytes;
        long totalMemory = Runtime.getRuntime().totalMemory()/MegaBytes;
        long maxMemory = Runtime.getRuntime().maxMemory()/MegaBytes;

        System.out.println("Used : " + (maxMemory - freeMemory));
        System.out.println("Free : " + freeMemory);
        System.out.println("Heap : " + totalMemory);
        System.out.println("Max  : " + maxMemory);

    }

    private static int getRand(){
        Random rand = new Random();
        int randNum = rand.nextInt(10000);
        return randNum;
    }
}

