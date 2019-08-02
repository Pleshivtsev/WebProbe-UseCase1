package interfaceTests.tme;

import org.testng.annotations.Test;
import testBase.SapUrl;
import testBase.TestBase;
import testLogic.forms.*;
import webprobe.WebProbe;
import webprobe.WebProbesPool;

import java.lang.reflect.Method;

@SuppressWarnings("NonAsciiCharacters")
public class TmeTests extends TestBase {


    @Test
    public void Создание_Накладной_В_Отстой(Method method){
        String webProbeName =  method.getName();
        WebProbe webProbe =  WebProbesPool.getInstance().getWebProbeByName(webProbeName);
        Invoice invoice = new Invoice(webProbe, SapUrl.TME.ПультДиспетчера, "XXX","XXX");
        invoice.созданиеНакладнойВОтстой();
    }

    @Test
    public void Создание_Накладной_Под_Погрузку(Method method){
        String webProbeName =  method.getName();
        WebProbe webProbe =  WebProbesPool.getInstance().getWebProbeByName(webProbeName);
        Invoice invoice = new Invoice(webProbe, SapUrl.TME.ПультДиспетчера, "XXX","XXX");
        invoice.созданиеНакладнойПодПогрузку();
    }

    @Test
    public void Создание_Накладной_В_Ремонт(Method method){
        String webProbeName =  method.getName();
        WebProbe webProbe =  WebProbesPool.getInstance().getWebProbeByName(webProbeName);
        Invoice invoice = new Invoice(webProbe, SapUrl.TME.ПультДиспетчера, "XXX","XXX");
        invoice.созданиеНакладнойВРемонт();
    }

}