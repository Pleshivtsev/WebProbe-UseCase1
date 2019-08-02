package model;

import java.util.HashMap;
import java.util.Map;

public class Defaults {

    private  Map<String, String> systems = new HashMap<>();

    public String tme_CreateDscUrl = "http://sap-ci-tme.sap.tc:8055/sap/bc/webdynpro/scmtms/tcm_efa_cu?sap-client=340&sap-language=RU&CHANGE_MODE=C&WDCONFIGURATIONID=%2FSCMTMS%2FTCM_EFA_CU&sap-wd-stableids=X#";



    private void initSystems(){
        systems.put("TME_PultDispetchera","http://sap-ci-tme.sap.tc:8055/sap/bc/webdynpro/sap/zcdtm_wda_dc?WDCONFIGURATIONID=ZCDTM_WDCA_DC&sap-client=340&sap-language=RU&sap-wd-stableids=X#");
        systems.put("TME_CreateVZForm", "http://sap-ci-tme.sap.tc:8055/sap/bc/webdynpro/scmtms/fwd_order?CHANGE_MODE=C&sap-language=RU&sap-wd-stableids=X#");
    }

    public Defaults(){
        initSystems();
    }

    public String getSystemUrl(String systemName){
        return systems.get(systemName);
    }



}
