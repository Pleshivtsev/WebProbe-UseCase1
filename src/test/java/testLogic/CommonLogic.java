package testLogic;

import model.pages.LoginPage;
import model.pages.common.SapCommonPage;
import org.openqa.selenium.By;
import webprobe.WebProbe;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonLogic {

    public static String getCurrentDate(){
        return new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
    }

    public static String getDateShifted(int dayShift){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.DATE,dayShift);
        return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
    }

    public static int login(WebProbe webProbe, String baseUrl, String login, String password, int timeout){
        int статусЗагрузкиСтраницы;
        webProbe.navigateTo(baseUrl);

        LoginPage loginPage = new LoginPage(webProbe);
        loginPage.verify();
        loginPage.login(login, password);
        loginPage.waitForSapLoaderDisappear(timeout);
        String bodyText = webProbe.getText(By.cssSelector("body"));

        //500 Internal Server Error
        if (bodyText.contains("500 Internal Server Error")) return SapCommonPage.Ошибка500;
        //Вы уже зарегистрированы в системе со следующими сеансами:
        if (bodyText.contains("Вы уже зарегистрированы в системе со следующими сеансами:")) loginPage.continueLogin(false);

        loginPage.waitForSapLoaderDisappear(timeout);

        bodyText = webProbe.getText(By.cssSelector("body"));
        if (bodyText.contains("500 Internal Server Error")) return SapCommonPage.Ошибка500;

        return SapCommonPage.СтатусЗагруженаУспешно;
    }

    public static int checkFor500Error(WebProbe webProbe){
        String bodyText = webProbe.getText(By.cssSelector("body"));
        if (bodyText.contains("500 Internal Server Error")) return SapCommonPage.Ошибка500;
        return SapCommonPage.СтатусЗагруженаУспешно;
    }


}
