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

import java.sql.Timestamp;
import java.util.Date;


public class Main {

    public static void main(String[] args) {
        Main m = new Main();
        m.testVG();
        System.exit(0);
    }

    private void testVG(){

        EyesRunner visualGridRunner = new VisualGridRunner(10);
        Configuration renderConfig = new Configuration();

        ChromeOptions cOptions = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        cOptions.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(cOptions);

        Integer i = 0;

      //  FileLogger logHandler = new FileLogger("eyes_test3D.log", false, true);
      //  eyes.setLogHandler(logHandler);

        BatchInfo batchInfo = new BatchInfo("MemTest VG 98");
        batchInfo.setId("VGMEMTEST98");

        renderConfig
                .addBrowser(1400, 600, BrowserType.CHROME)
                .addBrowser(1400, 600, BrowserType.FIREFOX)
                .setAppName("MemTest")
                .setServerUrl("https://eyes.applitools.com")
                .setBatch(batchInfo)
                .setMatchLevel(MatchLevel.LAYOUT2)
                .setSendDom(false)
                .setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        Eyes eyes = new Eyes(visualGridRunner);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        try {

            for(i=0;i<100;i++) {

                Date date = new Date();
                System.out.println(new Timestamp(date.getTime()) + ": Iteration " + (i+1));

                renderConfig.setTestName("Memtest " + String.format("%03d",(i+1)));
                eyes.setConfiguration(renderConfig);

                eyes.open(driver);

                // driver.get("http://localhost/~christopheremerson/simple.html");
                 driver.get("https://www.gore.com/");
                // driver.get("http://localhost/~christopheremerson/large.html");
                // driver.get("https://techcrunch.com/");

                eyes.checkWindow("test");
                //eyes.check("Test", Target.window());  // iCheckSettign error with fluent, may be my machine or config

                TestResults testResult = eyes.close(false);
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

            for(i=0;i<1000;i++) {
                System.out.println("**** Iteration " + (i+1) + " ****");

                eyes.open(driver, "Memtest", "Memtest " + String.format("%03d",(i+1)), new RectangleSize(1200, 800));

                //driver.get("http://localhost/~christopheremerson/simple.html");
                driver.get("https://www.gore.com/");

                eyes.checkWindow("test");
                //eyes.check("test", Target.window().fully());
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
}

