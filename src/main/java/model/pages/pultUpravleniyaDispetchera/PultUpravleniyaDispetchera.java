package model.pages.pultUpravleniyaDispetchera;

import model.pages.common.FilterConditionsPopup;
import model.pages.common.SapCommonPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import webprobe.WebProbe;
import webprobe.pages.PageElement;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class PultUpravleniyaDispetchera extends SapCommonPage {

    //**************************** Локаторы елементов  *****************************************************************
    private final By titleLocator = By.id("FPM_OVP_COMPONENT.ID_0001:PAGE_HEADER.ROOTUIELEMENTCONTAINER-title");
    private final By panelsLocator = By.cssSelector("td[id*='caCnt'] table.lsTbsTabDesign.lsPanel");
    private final By regimeSelectorLocator = By.id("FPM_FORM_UIBB_GL2.ID_17759A539A66C02DD27F2B10105B7B44:V_FORM.FGL2_FA163E742C851EE7BADFDFAC532347E7");
    private final By regimeOptionsLocator = By.cssSelector("tr[role='listitem']");
    private final By frameLocator = By.id("URLSPW-0");
    private final By frameLocator1 = By.id("URLSPW-1");
    private final By frameTableOptionsLocator = By.cssSelector(".urStd.urST3Cl");

    //**************************** Настройка элементов  ********************************************************************
    private PageElement title = new PageElement(titleLocator).setExpectedText("Пульт Управления Диспетчера");
    private PageElement regimeSelector = new PageElement(regimeSelectorLocator).setName("Выбор режима пульта");

    //**************************** Настройка страницы  *********************************************************************
    private void initMainPageBlock(){
        mainPageBlock.addPageElement(title);
        mainPageBlock.addPageElement(regimeSelector);
    }

    //**************************** Конструктор  ****************************************************************************
    public PultUpravleniyaDispetchera(WebProbe webProbe){
        super(webProbe);
        initMainPageBlock();
        setLastLoadElement(regimeSelector);
    }

    //**************************** Внутренние методы  ******************************************************************

    public WebElement getWagsPanel(){
        List<WebElement> panles = webProbe.waitForNumberOfElementsToBeMoreThan(panelsLocator, 1, pageTimeOut);
        return panles.get(0);
    }

    private WebElement wagsPanel(){
        List<WebElement> panles = webProbe.waitForNumberOfElementsToBeMoreThan(panelsLocator, 1, pageTimeOut);
        return panles.get(0);
    }

    public WebElement ordersPanel(){
        List<WebElement> panles = webProbe.waitForNumberOfElementsToBeMoreThan(panelsLocator, 1, pageTimeOut);
        return panles.get(1);
    }

    public void filterByStationNotEmpty(){
        FilterConditionsPopup filterConditionsPopup = new FilterConditionsPopup(webProbe);

        openFilterParameter("Станция дислокации");
        waitForSecond();
        filterConditionsPopup.openConditionsTab();
        filterConditionsPopup.selectCondition(1,"не пуст.");
        filterConditionsPopup.clickOk();
        waitForSecond();
    }

    public void wagsFilterBeginAt(String wagsStartNum){
        FilterConditionsPopup filterConditionsPopup = new FilterConditionsPopup(webProbe);
        openFilterParameter("Номер вагона");
        waitForSecond();
        filterConditionsPopup.selectConditionAndTypeValue(1,"начинается на", "6");
        filterConditionsPopup.clickOk();
        waitForSecond();
    }

//**************************** Методы  *****************************************************************************
    public void openAndCheckPage(){
        webProbe.navigateTo("http://sap-ci-tme.sap.tc:8055/sap/bc/webdynpro/sap/zcdtm_wda_dc?WDCONFIGURATIONID=ZCDTM_WDCA_DC&sap-client=340&sap-language=RU&sap-wd-stableids=X#");
        verify();
    }

    public void selectPultREgime(String режимПульта){
        click(regimeSelector);
        waitForElementToBeVisible(regimeOptionsLocator);
        clickElementContainsText(regimeOptionsLocator,режимПульта);
        waitForTwoSeconds();
    }

    public void selectUnnumberedOrders(){
        executingMethod = "selectUnnumberedOrders";
        By orderTypeOptionsLocator = By.cssSelector(".urRbg span.lsCBStdTxt");
        clickComm(orderTypeOptionsLocator, "Безномерные");
        waitForTwoSeconds();
    }

    public void selectTypeOfInstruction(String типЗадания){
        By tipZadaniyaLocator = By.id("FPM_FORM_UIBB_GL2.ID_17759A539A66C02DD27F2B10105B7B44:V_FORM.FGL2_FA163E742C851ED8A9A921C3E29D5DC2-btn");
        clickComm(tipZadaniyaLocator);
        switchToPopup1stLevel();
        clickComm(frameTableOptionsLocator, типЗадания);
        switchToRootFrameFromPopup1stLevel();
    }

    public void selectInstructionSubType(String подтипЗадания){
        executingMethod = "selectInstructionSubType";
        By podtipZadaniyaNaPeresylkuLocator = By.id("FPM_FORM_UIBB_GL2.ID_17759A539A66C02DD27F2B10105B7B44:V_FORM.FGL2_FA163E742C851ED8A9A9258B7392FDC5-btn");
        clickComm(podtipZadaniyaNaPeresylkuLocator);
        switchToPopup1stLevel();
        clickComm(frameTableOptionsLocator, подтипЗадания);
        switchToRootFrameFromPopup1stLevel();
        waitForSecond();
    }

    public void clickButtonInModalWindow(String buttonName){
        By buttonsPanelLocator = By.cssSelector("table.urPWButtonTable div");
        waitForElementToBeVisible(buttonsPanelLocator);
        webProbe.clickElementContainsText(buttonsPanelLocator, buttonName, pageTimeOut);
    }

    public void openFilterByWags(){
        By локаторСсылок = By.cssSelector("a.urLnkFunction");
        clickComm(wagsPanel(), локаторСсылок, "Фильтр");
        switchToPopup1stLevel();
        webProbe.sleep(1);
    }

    public void openFilterParameter(String parameterName){
        By parameterLocator = By.cssSelector("table.urFontStd.urHtmlTableReset");

        WebElement parameterContainer = webProbe.getWebElementContainsText(parameterLocator, parameterName, pageTimeOut);
        WebElement parameterButton = parameterContainer.findElement(By.cssSelector("div[id*='input-btn']"));

        JavascriptExecutor jse = (JavascriptExecutor)webProbe.getDriver();
        jse.executeScript("arguments[0].scrollIntoView(true);",parameterContainer);
        parameterButton.click();

        webProbe.switchToRootFrame();
        webProbe.waitForFrameAndSwitchToIt(frameLocator1, pageTimeOut);
    }

    private void selectValueInTheFilterParameter(String параметрФильтра, String значение){
        By локаторТаблицыЗначений = By.cssSelector("td.urST5HasContentDiv");

        openFilterParameter(параметрФильтра);
        waitForSecond();
        waitForElementToBeVisible(локаторТаблицыЗначений);
        clickComm(локаторТаблицыЗначений, значение);
        waitForSecond();
        pressOkItThePopup2ndLevel();
    }

    public void filterByStayMarker(String признакОтстоя){
        executingMethod = "filterByStayMarker";
        selectValueInTheFilterParameter("Признак отстоя", признакОтстоя);
    }

    public void filterByWasRod(String родВагона){

        selectValueInTheFilterParameter("Род вагона по реестру", родВагона);
    }

    public void startFilterSearch(Integer timeOut){

        Integer defaultTimeOut = getPageTimeOut();
        setPageTimeOut(timeOut);
        pressButtonInThePopup("Запуск");
        switchToRootFrameFromPopup1stLevel();
        setPageTimeOut(defaultTimeOut);
    }

    public void openWagsUserFilterByColumnName(String columnName){
        By columnLocator = By.cssSelector("tr[vpm='mrss-hdr'] div[title*='" + columnName +"']");
        By userMenuLocator = By.cssSelector("div.urMnu");
        By userFilerLocator = By.cssSelector("div.urMnu tr[ct='POMNI'] td.urMnuTxt");

        clickComm(columnLocator);
        waitForElementToBeVisible(userMenuLocator);
        webProbe.clickElementContainsText(userFilerLocator,"Пользовательский фильтр", pageTimeOut);
        webProbe.sleep(1);
        webProbe.waitForFrameAndSwitchToIt(frameLocator, pageTimeOut);
    }

    public void selectWagsWithNotEmptyStationName(){
        executingMethod = "selectWagsWithNotEmptyStationName";

        By filterOptionsLocator = By.cssSelector("td.urLayoutDefault td.lsTblEdf3HlpBtnTd");
        By dropdownLocator = By.cssSelector("table.lsItemlistbox__list");
        By dropdownOptionsLocator = By.cssSelector("table.lsItemlistbox__list tr");

        openWagsUserFilterByColumnName("Название станции");
        waitForElementToBeVisible(filterOptionsLocator);

        List<WebElement> filterOptionsButtons = webProbe.getDriver().findElements(filterOptionsLocator);

        filterOptionsButtons.get(1).click();

        waitForElementToBeVisible(dropdownLocator);
        webProbe.clickElementContainsText(dropdownOptionsLocator,"не пуст.", pageTimeOut);
        webProbe.sleep(1);
        clickButtonInModalWindow("OK");
        webProbe.switchToRootFrame();
        waitForElementToBeNotVisible(frameLocator);
        waitForLoaderDisappear();
    }

    public boolean isAvailableWags(){
        executingMethod = "isAvailableWags";
        By wagsTableContentLocator = By.cssSelector("tbody[id*='content']");
        WebElement wagsTableContent = getWagsPanel().findElement(wagsTableContentLocator);

        return !wagsTableContent.getText().contains("Нет доступных данных");
    }

    public boolean isAvailableOrders(){
        executingMethod = "isAvailableOrders";
        By orderTableContentLocator = By.cssSelector("tbody[id*='content']");
        WebElement orderTableContent = ordersPanel().findElement(orderTableContentLocator);

        return !orderTableContent.getText().contains("Нет доступных данных");
    }

    public void selectOrderByVzOrDepo(String названиеПоляВвода, String номерЗаявкиВЗДепо, Integer timeOut){
        executingMethod = "selectOrderByVzOrDepo";
        Integer defaultTimeOut = getPageTimeOut();

        By локаторКонтейнераПоляВвода = By.cssSelector("span.urFwDLR,lsFlowLayoutItem--container");
        By локаторПоляВвода = By.cssSelector("span.lsTokenizer__inputcontainer input");
        By buttonsLocator = By.cssSelector("div.lsButton");

        WebElement контейнерПоляВвода = findElement(ordersPanel(), локаторКонтейнераПоляВвода, названиеПоляВвода);
        WebElement полеВвода = findElement(контейнерПоляВвода, локаторПоляВвода);

        enterText(полеВвода, номерЗаявкиВЗДепо);
        setPageTimeOut(timeOut);
        clickComm(ordersPanel(), buttonsLocator, "Запуск");
        waitForTwoSeconds();
        waitForLoaderDisappear();
        setPageTimeOut(defaultTimeOut);
    }

    public void selectAllAvailableOrders(Integer timeOut){
        Integer defaultTimeOut = getPageTimeOut();
        By buttonsLocator = By.cssSelector("div.lsButton");
        clickComm(ordersPanel(), buttonsLocator, "Запуск");
        waitForTwoSeconds();
        waitForSapLoaderDisappear(timeOut);
        setPageTimeOut(defaultTimeOut);
    }

    public void selectWagsByNumber(String номерВагона, Integer timeOut){
        executingMethod = "selectWagsByNumber";
        Integer defaultTimeOut = getPageTimeOut();

        By локаторКонтейнераПоляВвода = By.cssSelector("span.urFwDLR,lsFlowLayoutItem--container");
        By локаторПоляВвода = By.cssSelector("span.lsTokenizer__inputcontainer input");
        By buttonsLocator = By.cssSelector("div.lsButton");

        WebElement контейнерПоляВвода = findElement(wagsPanel(), локаторКонтейнераПоляВвода, "Номер вагона");
        WebElement полеВвода = findElement(контейнерПоляВвода, локаторПоляВвода);

        enterText(полеВвода, номерВагона);
        setPageTimeOut(timeOut);
        clickComm(wagsPanel(), buttonsLocator, "Запуск");
        waitForTwoSeconds();
        waitForLoaderDisappear();

        setPageTimeOut(defaultTimeOut);
    }

    public String select1stAvaliableWag(){

        By локаторПервогоВагона = By.cssSelector("tr.urST4RowFirstVisible div.urST5SCMetricInner");
        By локаторНомераПервогоВагона = By.cssSelector("tr.urST4RowFirstVisible>td>div>a");

        clickComm(wagsPanel(), локаторПервогоВагона);
        waitForTwoSeconds();
        waitForLoaderDisappear();
        return webProbe.getText(локаторНомераПервогоВагона);
    }

    public void select1stAvailbaleOrder(){

        By локаторПервойЗаявки = By.cssSelector("tr.urST4RowFirstVisible div.urST5SCMetricInner");
        clickComm(ordersPanel(), локаторПервойЗаявки);
        waitForTwoSeconds();
    }

    public String createInvoice(Integer timeOut){

        By локаторКнопкиСоздать = By.id("FPM_OVP_COMPONENT.ID_0001:PAGE_HEADER.FPM_CA_TOOLBAR_CREATE_WB");
        By локаторСообщенияОсозданииНакладной = By.id("WDR_MESSAGE_AREA.ID_9DB0ABEB72D13A0450A5CC9A4247D9D9:MESSAGE_AREA.MESSAGE_LIST-contentTBody");

        clickComm(локаторКнопкиСоздать);
        webProbe.waitForElementToBeVisibleBy(локаторСообщенияОсозданииНакладной, timeOut);
        return webProbe.getText(локаторСообщенияОсозданииНакладной);
    }

}