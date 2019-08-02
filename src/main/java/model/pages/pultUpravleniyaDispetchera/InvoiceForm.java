package model.pages.pultUpravleniyaDispetchera;

import model.pages.CommonPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import webprobe.WebProbe;
import webprobe.pages.PageElement;
import webprobe.utils.Assert;

import java.util.List;

public class InvoiceForm extends CommonPage {

    //**************************** Локаторы елементов  *****************************************************************
    private final By invoiceTableTitleLocator = By.cssSelector(".lsHdArTitle2");
    private final By invoiceCheckBoxLocator = By.cssSelector(".urST3TDBtn.urCursorClickable");

    private final By invoiceToolbarLocator = By.cssSelector(".urTWhlFlat.lsToolbar--leftItems");
    private final By invoiceToolbarButtonsLocator = By.cssSelector(".urTWhlFlat.lsToolbar--leftItems>span");

    private final By messageLocator = By.cssSelector(".lsMACenterContainer");

    private final By frameLocator = By.id("URLSPW-0");


    //**************************** Настройка элементов  ****************************************************************
    private PageElement invoiceTableTitle = new PageElement(invoiceTableTitleLocator).setExpectedText("Накладные");
    private PageElement invoiceCheckBox = new PageElement(invoiceCheckBoxLocator);


    //**************************** Настройка страницы  *****************************************************************
    private void initMainPageBlock(){
        mainPageBlock.addPageElement(invoiceTableTitle);
        mainPageBlock.addPageElement(invoiceCheckBox);
    }
    //**************************** Конструктор  ************************************************************************
    public InvoiceForm(WebProbe webProbe) {
        super(webProbe);
        initMainPageBlock();
        setLastLoadElement(invoiceCheckBox);
    }
    //**************************** Внутренние методы  ******************************************************************


//**************************** Методы  *****************************************************************************

    public void выбратьНакладную(){
        WebElement invoiceToolbar = webProbe.getDriver().findElement(invoiceToolbarLocator);
        click(invoiceCheckBox);
        webProbe.waitForStalenessOfElement(invoiceToolbar, pageTimeOut);
    }

    public void нажатьКнопкуОтправить(){
        waitForElementToBeVisible(invoiceToolbarLocator);
        List<WebElement> toolBarButtons = webProbe.getDriver().findElements(invoiceToolbarButtonsLocator);
        WebElement sendButton = toolBarButtons.get(2).findElement(By.cssSelector("div"));
        String buttonText = sendButton.getText();
        Assert.pageAssertTrue(buttonText.equals("Отправить документ"), "Button text is not:Отправить документ, but:"+buttonText);
        sendButton.click();
    }

    public String отправитьНакладнуюВЭтран(){
        By локаторТулбара = By.cssSelector(".urTWhlFlat.lsToolbar--leftItems");
        By локаторЧекбоксаНакладной = By.cssSelector(".urST3TDBtn.urCursorClickable");

        WebElement тулбар = findElement(локаторТулбара);
        clickComm(локаторЧекбоксаНакладной);
        webProbe.waitForStalenessOfElement(тулбар, 30);
        нажатьКнопкуОтправить();
        waitForSecond();
        return getServiceMessageText();
    }


    public void closeInfoWindow(){
        By closeButtonLocator = By.cssSelector("div[id*='collapse']");
        By infoWindowLocator = By.cssSelector("div[id*='centerContainer']");
        clickComm(closeButtonLocator);
        waitForElementToBeNotVisible(infoWindowLocator);
    }

    public void clickToolBarButton(String buttonTitle){
        By buttonlocator = By.cssSelector("td[id*='menuindicator'] div[id*='itms'] div[title*='" + buttonTitle +"']");
        clickComm(buttonlocator);
    }


    public String stornIvoice(){
        clickToolBarButton("Обработать");
        webProbe.sleep(5);
        clickToolBarButton("Обновить");
        webProbe.sleep(1);
        clickToolBarButton("Отмена документа");
        webProbe.sleep(1);
        webProbe.waitForFrameAndSwitchToIt(frameLocator, pageTimeOut);

        By popupTitlteLocator = By.cssSelector("div[id*='ttltxt']");
        PageElement popupTitle = new PageElement(popupTitlteLocator).setExpectedText("Сторнировать документ");
        popupTitle.fillWebElement(webProbe.getDriver());

        Assert.shouldBeTrue(popupTitle.verify(),"***Something wrong with verify popup window");

        By popupButtonsLocator = By.cssSelector("td.lsPopupWindow_Footer-buttons div.urBtnStd");
        webProbe.clickElementContainsText(popupButtonsLocator,"OK", pageTimeOut);

        webProbe.switchToRootFrame();
        setPageTimeOut(180);
        waitForElementToBeNotVisible(frameLocator);

        waitForElementToBeVisible(messageLocator);
        return webProbe.getText(messageLocator);
    }

}
