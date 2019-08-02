package model.pages.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import webprobe.WebProbe;
import webprobe.pages.Page;
import webprobe.utils.Assert;

import java.util.List;

public class FilterConditionsPopup extends Page {

    //**************************** Локаторы елементов  *****************************************************************
    private final By selfFrameLocator = By.id("URLSPW-1");
    private final By parentFilterFrameLocator = By.id("URLSPW-0");

    private final By mainButtonsLocator = By.cssSelector("table.urPWButtonTable div");
    private final By tabsLocator = By.cssSelector("td.lsPnstPanel span[id*='-title']");
    private final By conditionInputsLocator = By.cssSelector("input.lsField__input");
    private final By addRowButtonsLocator = By.cssSelector("a.urLnkFunction img[alt*='Вставить новую следующую строку']");
    //**************************** Настройка элементов  ****************************************************************

    //**************************** Настройка страницы  *****************************************************************
    private void initMainPageBlock(){
    }

    //**************************** Конструктор  ************************************************************************
    public FilterConditionsPopup(WebProbe webProbe) {
        super(webProbe);
        initMainPageBlock();
    }

    //**************************** Внутренние методы  ******************************************************************
    private void selectOptionFromDropdown(WebElement parentElement, String option){
        By dropdownLocator = By.cssSelector("table.lsItemlistbox__list");
        By dropdownOptionsLocator = By.cssSelector("table.lsItemlistbox__list tr");

        webProbe.waitForElementToBeClickable(parentElement, pageTimeOut);
        parentElement.click();
        waitForElementToBeVisible(dropdownLocator);
        clickElementContainsText(dropdownOptionsLocator, option);
        waitForElementToBeNotVisible(dropdownLocator);
    }

    //**************************** Методы  *****************************************************************************

    public void clickOk(){
        clickElementContainsText(mainButtonsLocator, "OK");
        webProbe.switchToRootFrame();
        waitForElementToBeNotVisible(selfFrameLocator);
        webProbe.switchToFrameBy(parentFilterFrameLocator);
    }

    public void openConditionsTab(){
        waitForElementToBeVisible(tabsLocator);
        clickElementContainsText(tabsLocator, "Определение условий");
        waitForElementToBeVisible(conditionInputsLocator);
    }

    public void selectCondition(Integer rowNumber, String condition){
        List<WebElement> conditionTabInputs = webProbe.getDriver().findElements(conditionInputsLocator);
        Integer conditionInputIndex = (rowNumber-1)*3 + 1;

        WebElement conditionInput = conditionTabInputs.get(conditionInputIndex);
        selectOptionFromDropdown(conditionInput, condition);
        webProbe.sleep(1);
    }

    public void selectConditionAndValue(Integer rowNumber, String condition, String value){
        Integer valueInputIndex = (rowNumber-1)*3 + 2;

        selectCondition(rowNumber, condition);
        List<WebElement> conditionTabInputs = webProbe.getDriver().findElements(conditionInputsLocator);
        WebElement valueInput = conditionTabInputs.get(valueInputIndex);
        selectOptionFromDropdown(valueInput, value);
        webProbe.sleep(1);
    }

    public void selectConditionAndTypeValue(Integer rowNumber, String condition, String value){
        Integer valueInputIndex = (rowNumber-1)*3 + 2;

        selectCondition(rowNumber, condition);
        List<WebElement> conditionTabInputs = webProbe.getDriver().findElements(conditionInputsLocator);
        WebElement valueInput = conditionTabInputs.get(valueInputIndex);
        valueInput.click();
        valueInput.sendKeys(value);
        webProbe.sleep(1);
    }

    public void addConditionRow(){
        List<WebElement> addRowButtons = webProbe.getDriver().findElements(addRowButtonsLocator);
        Assert.shouldBeTrue(addRowButtons.size()>0,"*** There are no add conditions row buttons");
        addRowButtons.get(addRowButtons.size()-1).click();
        webProbe.sleep(1);
    }

}
