package model.pages;

import org.openqa.selenium.*;
import webprobe.WebProbe;
import webprobe.pages.Page;
import webprobe.pages.PageBlock;
import webprobe.utils.Assert;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class CommonPage extends Page {
    public String executingMethod = "";
    protected String baseUrl = "";
//**************************** Локаторы елементов  *********************************************************************
    protected final By loaderLocator = By.id("ur-loading");
    protected WebDriver driver;

//**************************** Настройка страницы  *********************************************************************
    protected PageBlock mainPageBlock;

    private void addCommonErrorStrings(){
        addErrorString("500 Internal Server Error");
    }

    private void initMainPageBlock(){
        mainPageBlock = new PageBlock();
        mainPageBlock.setName("Main page block");
    }

//**************************** Конструктор  ****************************************************************************
    public CommonPage(WebProbe webProbe){
        super(webProbe);
        driver = webProbe.getDriver();
        initMainPageBlock();
        addPageBlock(mainPageBlock);
        addCommonErrorStrings();
    }

    //**************************** Внутренние методы  **********************************************************************

    protected void waitForSecond(){
        webProbe.sleep(1);
    }

    protected void waitForTwoSeconds(){
        webProbe.sleep(2);
    }

    protected WebElement findElement(By locator){
        return driver.findElement(locator);
    }

    protected WebElement findElement(WebElement parentWebElement, By childLocator){
        return parentWebElement.findElement(childLocator);
    }

    protected WebElement findElement(WebElement parentWebElement, By parentLocator, String textInTheElement){
        List<WebElement> elements = findElements(parentWebElement, parentLocator);
        return webProbe.getWebElementContainsText(elements, textInTheElement);
    }



    protected List<WebElement> findElements(By locator){
        return driver.findElements(locator);
    }

    protected List<WebElement> findElements(WebElement parentElement, By locator){
        return parentElement.findElements(locator);
    }

    protected void clickComm(By locator){
        webProbe.waitForElementToBeClickableBy(locator, pageTimeOut).click();
    }

    protected void clickComm(WebElement parentWebElement, By childElementLocator){
        parentWebElement.findElement(childElementLocator).click();
    }

    protected WebElement clickComm(By parentLocator, String textInTheElement){
        WebElement element = webProbe.getWebElementContainsText(parentLocator, textInTheElement, pageTimeOut);
        element.click();
        return element;
    }

    protected WebElement clickComm(WebElement parentElement, By childElementsLocator, String textInTheChildElement){
        List<WebElement> elements = findElements(parentElement, childElementsLocator);
        WebElement element = webProbe.getWebElementContainsText(elements, textInTheChildElement);
        element.click();
        return element;
    }

    protected WebElement enterText(By locator, String text){
        WebElement webElement = driver.findElement(locator);
        webElement.click();
        webElement.clear();
        webElement.sendKeys(text);
        return webElement;
    }

    protected WebElement enterText(WebElement webElement, String text){
        webElement.click();
        webElement.clear();
        webElement.sendKeys(text);
        return webElement;
    }

    protected WebElement enterTextAndPressEnter(By locator, String text){
        WebElement webElement = enterText(locator, text);
        webElement.sendKeys(Keys.ENTER);
        return webElement;
    }

    protected WebElement enterTextAndPressTab(By locator, String text){
        WebElement webElement = enterText(locator, text);
        webElement.sendKeys(Keys.TAB);
        return webElement;
    }

    protected WebElement enterTextAndPressEnter(WebElement webElement, String text){
        enterText(webElement, text);
        webElement.sendKeys(Keys.ENTER);
        return webElement;
    }

    protected WebElement scrollToElement(By locator){
        WebElement element;
        element = findElement(locator);
        webProbe.scrollToElement(element);
        return element;
    }

    protected String getTextPreventStale(By locator){
        try{
            return webProbe.getText(locator);
        }catch (StaleElementReferenceException e){
            return webProbe.getText(locator);
        }
    }

//**********************************************
//**********************************************



    // Получение группы для поиска элемента, используется для групповых полей ввода ДСЦ, ВЗ
    protected WebElement getFieldsGroupElement(String groupFieldTitle, String someFieldTitle){
        final By fieldGroupsLocator = By.cssSelector("div[ct='SC'].lsHTMLContainer table.urFontStd.urHtmlTableReset");
        List<WebElement> fieldGroups = webProbe.getDriver().findElements(fieldGroupsLocator);

        Assert.shouldBeTrue(fieldGroups.size() > 0, "*** Found 0 elements groups on this page");

        for (WebElement webElement:fieldGroups){
            String webElementText = webElement.getText();
            if (webElementText.contains(groupFieldTitle) && webElementText.contains(someFieldTitle)){
                return webElement;
            }
        }

        Assert.pageAssert("*** Necessary fields group was not found: " + groupFieldTitle);
        return null;
    }


//**************************** Методы  *********************************************************************************
    public void openPage(){
        if (baseUrl == null || baseUrl.equals("")){
            Assert.pageAssert("Не задан базовый урл страницы");
        }
        webProbe.navigateTo(baseUrl);
    }

    public void openAndCheckPage(){
        openPage();
        verify();
    }


    public void waitForLoaderDisappear(){
        waitForElementToBeNotVisible(loaderLocator);
    }

    public void waitForLoaderAppear(){
        waitForElementToBeVisible(loaderLocator);
    }

    public void waitForLoaderDisappear(Integer timeout){
        webProbe.waitForElementToBeNotVisibleBy(loaderLocator, timeout);
    }

    // Ввод значения в поле группы и нажание ENTER (Для форм ВЗ, ДСЦ, и т.п.)
    public void enterValueToField(String groupTitle, String fieldTitle, String fieldValue){
        // Получение элемента группы
        WebElement groupElement = getFieldsGroupElement(groupTitle, fieldTitle);
        // Получение искомого инпута
        WebElement inputField = groupElement.findElement(By.cssSelector("input[title='" + fieldTitle +"']"));
        inputField.click();
        inputField.clear();
        inputField.sendKeys(fieldValue);
        webProbe.sleep(1);
        inputField.sendKeys(Keys.ENTER);
        webProbe.sleep(2);
    }

    //Выбор поля в группе и выбор значения из выпадающего списка
    public void selectValueFromDropdown(String groupTitle, String fieldTitle, String fieldValue){
        By dropdownItemsLocator = By.cssSelector("tr.lsItemlistbox__item.urNoUserSelect");
        // Получение элемента группы
        WebElement groupElement = getFieldsGroupElement(groupTitle, fieldTitle);
        // Получение искомого инпута
        WebElement inputField = groupElement.findElement(By.cssSelector("input[title='" + fieldTitle +"']"));
        inputField.click();
        waitForElementToBeNotVisible(dropdownItemsLocator);
        clickElementContainsText(dropdownItemsLocator, fieldValue);
        webProbe.sleep(1);
    }

    //Выбор опции, из списка выпадающего из кнопки
    public void selectValueFromDropdownButton(String buttonName, String value){
        By parentLocator = By.cssSelector("body");
        selectItemFromDropDownMenu(parentLocator, buttonName, value);
    }

    protected void selectItemFromDropDownMenu(By parentLocator, String buttonName, String value){
        By buttonsLocator = By.cssSelector("div.lsButton.lsTbarBtnStd");
        By dropdownMenuLocator = By.cssSelector("table.lsMnuTable");
        By menuItems = By.cssSelector("table.lsMnuTable tr.lsMnuItemHeight");
        WebElement parent = webProbe.getDriver().findElement(parentLocator);

        webProbe.scrollToElement(parent);
        List<WebElement> buttons = parent.findElements(buttonsLocator);
        webProbe.clickElementContainsText(buttons, buttonName, pageTimeOut);
        waitForElementToBeVisible(dropdownMenuLocator);
        webProbe.sleep(1);
        clickElementContainsText(menuItems, value);
        webProbe.sleep(1);
    }

    protected void pressButtonInSelectedToolbar(By toolbarLocator, String buttonName){
        By buttonsLocator = By.cssSelector("div.lsButton.lsTbarBtnStd");

        WebElement toolbar = findElement(toolbarLocator);
        webProbe.scrollToElement(toolbar);
        List<WebElement> buttons = findElements(toolbar, buttonsLocator);
        webProbe.clickElementContainsText(buttons, buttonName, pageTimeOut);
        webProbe.sleep(1);
    }

    protected void switchToPopup1stLevel(){
        By popupLocator = By.id("URLSPW-0");
        waitForElementToBeVisible(popupLocator);
        webProbe.switchToFrameBy(popupLocator);
    }

    protected void returnToRootFrameFrom(By locatorOfVanishingPopup){
        webProbe.switchToRootFrame();
        waitForElementToBeNotVisible(locatorOfVanishingPopup);
    }

    protected void pressOkInThePopup(){
        By popupLocator = By.id("URLSPW-0");
        By buttonsPanelLocator = By.cssSelector("table.urPWButtonTable div");
        waitForElementToBeVisible(buttonsPanelLocator);
        webProbe.clickElementContainsText(buttonsPanelLocator, "OK", pageTimeOut);
        returnToRootFrameFrom(popupLocator);
    }

    protected void pressOkItThePopup2ndLevel(){
        By buttonsPanelLocator = By.cssSelector("table.urPWButtonTable div");
        By локаторОкна2гоУровня = By.id("URLSPW-1");

        waitForElementToBeVisible(buttonsPanelLocator);
        webProbe.clickElementContainsText(buttonsPanelLocator, "OK", pageTimeOut);
        returnToRootFrameFrom(локаторОкна2гоУровня);
        switchToPopup1stLevel();
    }

    protected void switchToRootFrameFromPopup1stLevel(){
        By popupLocator = By.id("URLSPW-0");
        returnToRootFrameFrom(popupLocator);
    }

    protected void pressButtonInThePopup(String buttonName){
        By buttonsPanelLocator = By.cssSelector("table.urPWButtonTable div");
        waitForElementToBeVisible(buttonsPanelLocator);
        webProbe.clickElementContainsText(buttonsPanelLocator, buttonName, pageTimeOut);
    }

    public void pressButtonByName(String buttonName){
        By buttonsLocator = By.cssSelector("div.lsButton.lsTbarBtnStd");
        clickElementContainsText(buttonsLocator, buttonName);
    }


    public String getServiceMessageText(){
        By msgTextLocator = By.cssSelector("div.lsMACenterContainer");
        waitForElementToBeVisible(msgTextLocator);
        return webProbe.getText(msgTextLocator);
    }

    public void closeServiceMessage(){
        By closeButtonLocator = By.cssSelector("div[id*='collapse']");
        By infoMessageLocator = By.cssSelector("td[id*='msgNotifier'] div[id*='center']");

        clickComm(closeButtonLocator);
        waitForElementToBeNotVisible(infoMessageLocator);
    }

}