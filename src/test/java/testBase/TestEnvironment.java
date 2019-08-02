package testBase;

import webprobe.enums.BrowserLocation;
import webprobe.enums.BrowserType;
import webprobe.enums.RemoteBrowserType;

public class TestEnvironment{


    private BrowserLocation browserLocation;
    private BrowserType browserType;
    private RemoteBrowserType remoteBrowserType;

    private String screenShotsFolderPath;



    public TestEnvironment(){
        browserLocation = BrowserLocation.LOCAL;
        //browserType = BrowserType.CHROME;
        browserType = BrowserType.CHROME_HEADLESS;
        //remoteBrowserType = RemoteBrowserType.CHROME;
        screenShotsFolderPath = "C:\\Testing\\Testresults\\Screenshots\\";
    }

//**********************************************************************************************************************
    public void setBrowserLocation(BrowserLocation browserLocation) {
        this.browserLocation = browserLocation;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public void setScreenShotsFolderPath(String screenShotsFolderPath) {
        this.screenShotsFolderPath = screenShotsFolderPath;
    }

    public void setRemoteBrowserType(RemoteBrowserType remoteBrowserType) {
        this.remoteBrowserType = remoteBrowserType;
    }

//**********************************************************************************************************************
    public BrowserLocation getBrowserLocation() {
        return browserLocation;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public String getScreenShotsFolderPath() {
        return screenShotsFolderPath;
    }

    public RemoteBrowserType getRemoteBrowserType() {
        return remoteBrowserType;
    }
}
