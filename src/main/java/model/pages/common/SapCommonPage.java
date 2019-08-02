package model.pages.common;

import model.pages.CommonPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import webprobe.WebProbe;



public class SapCommonPage extends CommonPage {

    public final static int СтатусЗагруженаУспешно = 0;
    public final static int СтатусТаймаут = 1;
    public final static int Ошибка500 = 500;


    public SapCommonPage(WebProbe webProbe) {
        super(webProbe);
    }



    public void waitForSapLoaderDisappear(int timeout){
        By loaderLocator = By.id("ur-loading");
        try {
            webProbe.setImplicitlyWait(3);
            webProbe.waitForElementToBeVisibleBy(loaderLocator, 3);
            webProbe.setImplicitlyWait(30);
        }catch (TimeoutException e){
            return;
        }
        webProbe.waitForElementToBeNotVisibleBy(loaderLocator, timeout);
    }




    public void enterValueToTitledInput(String inputTitle, String value){
        By locator = By.cssSelector("input[title*='"+inputTitle+"']");
        clickComm(locator);
        waitForSecond();
        enterText(locator,value);
    }

    public void clearTitledInput(String inputTitle){
        By locator = By.cssSelector("input[title*='"+inputTitle+"']");
        clickComm(locator);
        waitForSecond();
        findElement(locator).clear();
    }




}