package model.pages;

import model.pages.common.SapCommonPage;
import org.openqa.selenium.By;
import webprobe.WebProbe;
import webprobe.pages.PageElement;

public class LoginPage extends SapCommonPage {

//**************************** Настройка элементов  ********************************************************************
    private PageElement loginField = new PageElement(By.id("sap-user"));
    private PageElement passwordField = new PageElement(By.id("sap-password"));
    private PageElement enterButton = new PageElement(By.id("LOGON_BUTTON"));

//**************************** Настройка страницы  *********************************************************************
    private void initMainPageBlock(){
        mainPageBlock.addPageElement(loginField);
        mainPageBlock.addPageElement(passwordField);
        mainPageBlock.addPageElement(enterButton);
    }

//**************************** Конструктор  ****************************************************************************
    public LoginPage(WebProbe webProbe){
        super(webProbe);
        initMainPageBlock();
    }

//**************************** Методы  *********************************************************************************
    public void login(String login, String password){
        executingMethod = "login";
        loginField.type(login);
        passwordField.type(password);
        enterButton.click();
    }

    public void continueLogin(boolean cancelExistsSessions){
        By cancelSessionsCheckbox = By.id("delete-session-cb-txt");
        By continueLoginButton = By.id("SESSION_QUERY_CONTINUE_BUTTON");

        if (!cancelExistsSessions) {
            clickComm(cancelSessionsCheckbox);
            waitForSecond();
        }
        clickComm(continueLoginButton);
    }

    public void checkLoginPage(){
        webProbe.navigateTo("http://sap-ci-tme.sap.tc:8055/sap/bc/webdynpro/sap/zcdtm_wda_dc?WDCONFIGURATIONID=ZCDTM_WDCA_DC&sap-client=340&sap-language=RU&sap-wd-stableids=X#");
        webProbe.getDriver().manage().window().maximize();
        verify();
    }

}