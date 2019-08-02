package testLogic.checklist;

import org.openqa.selenium.By;
import org.testng.Assert;
import webprobe.WebProbe;

public class ChecklistItem {

    private String name;
    private By locator;

    private By webElement;
    private By parentWebElement;

    private String expectedSubString;

    public ChecklistItem(String name, By locator, String expectedSubString) {
        this.name = name;
        this.locator = locator;
        this.expectedSubString = expectedSubString;
    }

    public void verify(WebProbe webProbe){
        if (locator == null) Assert.fail(name + ": не задан локатор элемента");

        String elementText = webProbe.getText(locator);
        if (!elementText.contains(expectedSubString)) Assert.fail(name + " не содержит текст " + expectedSubString);
    }

}