package testLogic.checklist;

import org.openqa.selenium.By;
import org.testng.Assert;
import webprobe.WebProbe;

import java.util.ArrayList;

public class CheckList{

    private ArrayList<ChecklistItem> checklist;

    public void verify(WebProbe webProbe){
        if ( (checklist == null) || (checklist.size() == 0) ) Assert.fail("Лист проверки не задан");
        checklist.forEach(checklistItem -> checklistItem.verify(webProbe));
    }

    public void add(String name, By locator, String expectedSubString){
        if (checklist == null) checklist = new ArrayList<>();
        ChecklistItem checklistItem = new ChecklistItem(name, locator, expectedSubString);
        checklist.add(checklistItem);
    }

}
