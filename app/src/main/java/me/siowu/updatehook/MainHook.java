package me.siowu.updatehook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    private static final String TARGET_CLASS = "com.lenovo.tbengine.core.serverapi.ServerApi";
    private static final String CUSTOM_URL = " https://blueddd-ota.hf.space/upgrade?";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.lenovo.tbengine")) {
            return;
        }

        try {
            // 加载目标类
            Class<?> serverApiClass = XposedHelpers.findClass(TARGET_CLASS, lpparam.classLoader);

            // 获取 DeviceInfo 类
            Class<?> deviceInfoClass = lpparam.classLoader.loadClass("com.lenovo.tbengine.core.device.DeviceInfo");

            // Hook geServerResponseOrThrowError 方法，替换 URL 参数
            XposedHelpers.findAndHookMethod(
                    TARGET_CLASS,
                    lpparam.classLoader,
                    "geServerResponseOrThrowError",
                    String.class,
                    java.util.Properties.class,
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("原始 URL: " + param.args[2]);
                            if (param.args[2].equals("https://ota.lenovo.com/engine/upgrade?")) {
                                param.args[2] = CUSTOM_URL; // 替换为你自己的地址
                            }
                            XposedBridge.log("已替换为新 URL: " + param.args[2]);
                        }
                    }
            );

            // Hook doQueryNewVersion 方法（可选，仅用于日志）
            XposedHelpers.findAndHookMethod(
                    TARGET_CLASS,
                    lpparam.classLoader,
                    "doQueryNewVersion",
                    deviceInfoClass,
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("拦截到 doQueryNewVersion 方法");
                        }
                    }
            );

        } catch (ClassNotFoundException e) {
            XposedBridge.log("DeviceInfo 类未找到: " + e.getMessage());
        } catch (Throwable t) {
            XposedBridge.log("Hook 失败: " + t.getMessage());
            XposedBridge.log(t);
        }
    }
}