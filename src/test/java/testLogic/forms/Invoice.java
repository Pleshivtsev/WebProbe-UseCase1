package testLogic.forms;

import model.pages.pultUpravleniyaDispetchera.InvoiceForm;
import model.pages.pultUpravleniyaDispetchera.PultUpravleniyaDispetchera;
import org.testng.Assert;
import testData.invoices.InvoiceToLoad;
import testData.invoices.InvoiceToOtstoy;
import testData.invoices.InvoiceToRemont;
import testLogic.CommonLogic;
import testLogic.StepMethod;
import testLogic.StepMethodArg;
import webprobe.WebProbe;

import java.util.Set;

@SuppressWarnings("NonAsciiCharacters")
public class Invoice {

    private InvoiceToOtstoy invoiceToOtstoy;
    private InvoiceToLoad invoiceToLoad;
    private InvoiceToRemont invoiceToRemont;
    private PultUpravleniyaDispetchera pult;
    private InvoiceForm формаПросмотраНакладной;
    private WebProbe wp;

    private String baseUrl;
    private String login;
    private String password;

    private Set<String> existingWindows;

    public Invoice(WebProbe wp, String baseUrl, String login, String password) {
        this.login = login;
        this.password = password;
        this.wp = wp;
        this.baseUrl = baseUrl;

        this.invoiceToOtstoy = new InvoiceToOtstoy();
        this.invoiceToLoad = new InvoiceToLoad();
        this.invoiceToRemont = new InvoiceToRemont();
        pult = new PultUpravleniyaDispetchera(wp);
        pult.setPageTimeOut(90);
        формаПросмотраНакладной = new InvoiceForm(wp);
    }

//*************************************************************************************************
    private void step(String stepName, StepMethod stepMethod){
        wp.setCurrentStepName(stepName);
        stepMethod.run();
    }

    private void step(String stepName, StepMethodArg<String> stepMethod, String arg){
        wp.setCurrentStepName(stepName);
        stepMethod.run(arg);
    }
//*************************************************************************************************

    private void login(){
        wp.maximize();
        CommonLogic.login(wp,baseUrl,login, password,30);
    }

    private void setInvoiceParameters(String regime, Boolean isBeznomeryeZayavki, String tipZadaniya, String podtipZadaniya){
        pult.selectPultREgime(regime);
        if (isBeznomeryeZayavki) {
            pult.selectUnnumberedOrders();
        }
        pult.selectTypeOfInstruction(tipZadaniya);
        pult.selectInstructionSubType(podtipZadaniya);
    }

    private void setInvoiceParametersToOtstoy(){
        setInvoiceParameters(invoiceToOtstoy.pultRegime,true,invoiceToOtstoy.tipZadaniya,invoiceToOtstoy.podtipZadaniya);
    }

    private void setInvoiceParametersToLoad(){
        setInvoiceParameters(invoiceToLoad.pultRegime,false, invoiceToLoad.tipZadaniya, invoiceToLoad.podtipZadaniya);
    }

    private void setInvoiceParametersToRemont(){
        setInvoiceParameters(invoiceToRemont.pultRegime,false, invoiceToRemont.tipZadaniya, invoiceToRemont.podtipZadaniya);
    }

    private void getWagsAcceptableToStay(){
        pult.openFilterByWags();
        pult.filterByStationNotEmpty();
        pult.wagsFilterBeginAt("6");
        pult.filterByStayMarker("Запланирован в отстой");
        pult.startFilterSearch(90);
        pult.selectWagsWithNotEmptyStationName();
        if (!pult.isAvailableWags())
            Assert.fail("Нет доступных вагонов для постановки в отстой");
    }

    private void getWagsAcceptableToLoad(){
        pult.openFilterByWags();
        pult.filterByStationNotEmpty();
        pult.filterByWasRod("ПВ");
        pult.startFilterSearch(90);
        if (!pult.isAvailableWags())
            Assert.fail("Нет доступных вагонов под погрузку");
    }

    private void getWagsAcceptableToRepair(){
        pult.selectWagsByNumber("52086881", 90); // 52564390
        if (!pult.isAvailableWags())
            Assert.fail("Нет доступных вагонов для ремонта");
    }

    private void getWagsForProductionTest(){
        pult.openFilterByWags();
        pult.filterByWasRod("КР");
        pult.filterByStayMarker("Запланирован в отстой");
        pult.startFilterSearch(90);
        if (!pult.isAvailableWags())
            Assert.fail("Нет доступных вагонов в отстой");
    }

    private void getOrdersForProductionTest(){
        pult.selectAllAvailableOrders(90);
        if (!pult.isAvailableOrders())
            Assert.fail("Нет доступных заявок на отстой");
    }

    private void getOrdersByNum(String orderNum){
        pult.selectOrderByVzOrDepo("Номер заявки на постанову в отстой", orderNum, 90);
        if (!pult.isAvailableOrders())
            Assert.fail("Нет доступных заявок на отстой");
    }

    private void getVzByNum(String orderNum){
        pult.selectOrderByVzOrDepo("Номер ВЗ", orderNum, 90);
        if (!pult.isAvailableOrders())
            Assert.fail("Нет доступных заявок на отстой");
    }

    private void getDepoesForRepair(){
        pult.select1stAvaliableWag();
        if (!pult.isAvailableOrders())
            Assert.fail("Нет доступных депо для ремонта");
    }

    private void getFirstWag(){
        pult.setPageTimeOut(1);
        pult.select1stAvaliableWag();
        pult.setPageTimeOut(30);
    }

    private void createInvoice(){
        String successMessage = "успешно создана для документа";
        existingWindows = wp.getDriver().getWindowHandles();

        String realMessage = pult.createInvoice(90);
        if(!realMessage.contains(successMessage))
            Assert.fail(realMessage);
    }

    private void switchToInvoiceWindow(){

        wp.waitForNewWindowAndSwitch(existingWindows,5);
        формаПросмотраНакладной.verify();
    }

    private void sendInvoiceToEtran(){
        String successMessage = "отправлена в ЭТРАН";
        String realMessage = формаПросмотраНакладной.отправитьНакладнуюВЭтран();
        if (!realMessage.contains(successMessage))
            Assert.fail(realMessage);
    }

    private void invoiceStorn(){
        String stornInvoiceMessage = формаПросмотраНакладной.stornIvoice();

        if (!(stornInvoiceMessage.contains("Данные успешно сохранены") && stornInvoiceMessage.contains("сторнировано")) )
            Assert.fail(stornInvoiceMessage);
    }


//**********************************************************************************************************************
//*******   Сценарии ***************************************************************************************************

    public void созданиеНакладнойВОтстой(){
        step("Шаг 1. Вход в систему",                               this::login);
        step("Шаг 2. Открытие и проверка формы создания накладной", pult::verify);
        step("Шаг 3. Установка начальных параметров накладной",     this::setInvoiceParametersToOtstoy);
        step("Шаг 4. Отобрать вагоны, пригодные для отстоя",        this::getWagsAcceptableToStay);
        step("Шаг 5. Отбор заявок по номеру №" + invoiceToOtstoy.orderNum, this::getOrdersByNum, invoiceToOtstoy.orderNum);
        step("Шаг 6. Выбрать заявку",                               pult::select1stAvailbaleOrder);
        step("Шаг 7. Выбрать вагон",                                this::getFirstWag);
        step("Шаг 8. Создать накладную",                            this::createInvoice);
        step("Шаг 9. Переключиться в окно накладной",               this::switchToInvoiceWindow);
        step("Шаг 10. Отправить накладную в ЭТРАН",                 this::sendInvoiceToEtran);
    }

    public void созданиеНакладнойПодПогрузку(){
        step("Шаг 1. Вход в систему",                               this::login);
        step("Шаг 2. Открытие и проверка формы создания накладной", pult::verify);
        step("Шаг 3. Установка начальных параметров накладной",     this::setInvoiceParametersToLoad);
        step("Шаг 4. Отобрать вагоны, пригодные под погрузку",      this::getWagsAcceptableToLoad);
        step("Шаг 5. Отбор заявок по номеру №" + invoiceToLoad.orderNum, this::getVzByNum, invoiceToLoad.orderNum);
        step("Шаг 6. Выбрать заявку",                               pult::select1stAvailbaleOrder);
        step("Шаг 7. Выбрать вагон",                                this::getFirstWag);
        step("Шаг 8. Создать накладную",                            this::createInvoice);
        step("Шаг 9. Переключиться в окно накладной",               this::switchToInvoiceWindow);
        step("Шаг 10. Отправить накладную в ЭТРАН",                 this::sendInvoiceToEtran);
    }

    public void созданиеНакладнойВРемонт(){
        step("Шаг 1. Вход в систему",                               this::login);
        step("Шаг 2. Открытие и проверка формы создания накладной", pult::verify);
        step("Шаг 3. Установка начальных параметров накладной",     this::setInvoiceParametersToRemont);
        step("Шаг 4. Отобрать вагоны, пригодные в ремонт",          this::getWagsAcceptableToRepair);
        step("Шаг 5. Выбор депо для ремонта",                       this::getDepoesForRepair);
        step("Шаг 6. Выбрать заявку",                               pult::select1stAvailbaleOrder);
        step("Шаг 7. Создать накладную",                            this::createInvoice);
        step("Шаг 8. Переключиться в окно накладной",               this::switchToInvoiceWindow);
        // Перестало работать сторнирование после отправки в ЭТРАН
        //step("Шаг 9. Отправить накладную в ЭТРАН",                  this::sendInvoiceToEtran);
        step("Шаг 10. Стонировать накладную",                       this::invoiceStorn);
    }

    public void общаяПроверкаФункциональности(){
        step("Шаг 1. Вход в систему",                               this::login);
        step("Шаг 2. Открытие и проверка формы создания накладной", pult::verify);
        step("Шаг 3. Установка начальных параметров накладной",     this::setInvoiceParametersToOtstoy);
        step("Шаг 4. Отобрать вагоны, пригодные для отстоя",        this::getWagsForProductionTest);
        step("Шаг 5. Отобрать заявки для отстоя",                   this::getOrdersForProductionTest);
    }
}