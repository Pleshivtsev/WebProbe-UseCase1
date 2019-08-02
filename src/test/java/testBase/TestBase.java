package testBase;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import webprobe.WebProbe;
import webprobe.WebProbesPool;
import webprobe.enums.BrowserType;

import java.io.File;
import java.lang.reflect.Method;

public class TestBase {

    TestEnvironment testEnvironment;

    @BeforeClass
    public void beforeClass(){
        String headless = System.getProperty("headless");
        testEnvironment = new TestEnvironment();

        if ( (headless != null) && (headless.equals("true")) ){
            testEnvironment.setBrowserType(BrowserType.CHROME_HEADLESS);
        }
        else {
            testEnvironment.setBrowserType(BrowserType.CHROME);
        }

        WebProbesPool.getInstance();
    }

    @BeforeMethod
    public void beforeMethod(Method method){
        String webProbeName =  method.getName();
        BrowserType browserType = testEnvironment.getBrowserType();
        WebProbesPool.getInstance().addWebProbeToPool(browserType, webProbeName);
        System.out.println("**** WebProbe " + webProbeName + " started *****");
    }

    @AfterMethod
    public void afterMethod(ITestResult testResult){
        String webProbeName = testResult.getName();
        System.out.println("*** Trying to stop WebProbe: " +  webProbeName + "  ****");
        WebProbesPool.getInstance().getWebProbes().forEach(wp -> System.out.println("*** WebpProbe :" + wp.getName()));
        System.out.println("*****************************************************************");

        WebProbe webProbe =  WebProbesPool.getInstance().getWebProbeByName(webProbeName);

        try {
            if (testResult.getStatus() == ITestResult.FAILURE ) {
                File scrFile = ((TakesScreenshot) webProbe.getDriver()).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(testEnvironment.getScreenShotsFolderPath() + testResult.getName() + ".jpg"));

                if (webProbe.getCurrentStepName() != null) System.out.println("*** " + testResult.getName()+":"+ webProbe.getCurrentStepName());

            }
        }
        catch (Exception e){
            System.out.println("***Screenshot was not saved because exception: " + e.getLocalizedMessage());
        }

        if (webProbe.getCurrentStepName() != null) {
            testResult.setAttribute("stepName", webProbe.getCurrentStepName());
        }

        WebProbesPool.getInstance().quitByName(webProbeName);
    }
}