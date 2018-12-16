package com.RRG.usbninja.config;

import com.RRG.usbninja.ButtonModel;
import com.RRG.usbninja.app.MainApplication;
import com.wise.wisekit.utils.SPUtils;

public class ConfigInfo {

    private static String PASSWORD = "password";

    /**
     * 单例对象实例
     */
    private static ConfigInfo instance = null;

    public static ConfigInfo getInstance() {
        if (instance == null) {
            instance = new ConfigInfo();
        }
        return instance;
    }

    public String getPassword() {
        return (String) SPUtils.get(MainApplication.getAppContext(), PASSWORD, "8888");
    }

    public void setPassword(String password) {
        SPUtils.put(MainApplication.getAppContext(), PASSWORD, password);
    }



    public String getNum1() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num1","");
    }

    public void setNum1(String num1) {
        SPUtils.put(MainApplication.getAppContext(), "num1", num1);
    }

    public String getNum2() {

        return (String) SPUtils.get(MainApplication.getAppContext(), "num2", "");
    }

    public void setNum2(String num2) {
        SPUtils.put(MainApplication.getAppContext(), "num2", num2);
    }

    public String getNum3() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num3", "");
    }

    public void setNum3(String num3) {
        SPUtils.put(MainApplication.getAppContext(), "num3", num3);
    }

    public String getNum4() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num4", "");
    }

    public void setNum4(String num4) {
        SPUtils.put(MainApplication.getAppContext(), "num4", num4);
    }

    public String getNum5() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num5", "");
    }

    public void setNum5(String num5) {
        SPUtils.put(MainApplication.getAppContext(), "num5", num5);
    }

    public String getNum6() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num6", "");
    }

    public void setNum6(String num6) {
        SPUtils.put(MainApplication.getAppContext(), "num6", num6);
    }

    public String getNum7() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num7", "");
    }

    public void setNum7(String num7) {
        SPUtils.put(MainApplication.getAppContext(), "num7", num7);
    }

    public String getNum8() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num8", "");
    }

    public void setNum8(String num8) {
        SPUtils.put(MainApplication.getAppContext(), "num8", num8);
    }

    public String getNum9() {
        return (String) SPUtils.get(MainApplication.getAppContext(), "num9", "");
    }

    public void setNum9(String num9) {
        SPUtils.put(MainApplication.getAppContext(), "num9", num9);
    }
}
