package org.fuck.coolapk.hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import org.fuck.coolapk.CoolContext;
import org.fuck.coolapk.XposedEntry;
import org.fuck.coolapk.base.BaseHook;
import org.fuck.coolapk.utils.KotlinXposedHelperKt;

import java.util.Arrays;

import static org.fuck.coolapk.utils.OwnSPKt.hasEnable;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//佛祖镇楼BUG辟易

public class CustomVersionHook extends BaseHook {
    private static void hook() {
        try {
            Class<?> utilCodeUtil = XposedHelpers.findClass("ˡ.Ϳ", CoolContext.classLoader);
            KotlinXposedHelperKt.log("Found class: " + utilCodeUtil.getName(), false);
            // Hook
            XposedHelpers.findAndHookMethod(utilCodeUtil, "ފ", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    KotlinXposedHelperKt.log("Calling method: " + param.method.getName(), false);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    KotlinXposedHelperKt.log("Method returned: " + Arrays.toString((String[]) param.getResult()),
                            false);
                    // 改变返回值
                    param.setResult(new String[]{"User-Agent", "Dalvik/2.1.0 (Linux; U; Android 10; CHM-TL00 " +
                            "3A95B1A048E2BA5CB19AFD09C5B3EA) (#Build; Vivo; CHM-TL00; 3A95B1A048E2BA5CB19AFD09C5B3EA;" +
                            " 10) +CoolMarket/14.0.2-2402071-universal", "X-Requested-With", "XMLHttpRequest", "X-Sdk" +
                            "-Int", "21", "X-Sdk-Locale", "zh-CN", "X-App-Id", "com.coolapk.market", "X-App-Token",
                            "v2JDJ5JDEwJE1TNDNNRGMwTlRjeE5VVTUvODJlMU9kMy9nTldxUEdXVFdFTXpObDY3bGd2RDdCaVFscnlX", "X" +
                            "-App-Version", "14.0.2", "X-App-Code", "2402071", "X-Api-Version", "13", "X-App-Device",
                            "AbsVnbgsTQFNjQ1MUOwQkRBlTMCNUNBJkMFhDNwEUMCVTOBNDI7ADMMRVLNh0Qgszb2lmVgsTYs9mcvR3bNByOEdjOBZjOFdjO0EjOBljOEdDI7AyOgszQDhzQ0MUOygTQFJjRBZTQ0MjN1IjM4M0NEJTNEljQ", "X-Dark-Mode", "0", "X-App-Channel", "coolapk", "X-App-Mode", "universal", "X-App-Supported", "2402071", "Content-Type", "application/x-www-form-urlencoded"});
                    KotlinXposedHelperKt.log("Successfully modified the return value of this method.", false);
                }
            });
        } catch (Exception e) {
            KotlinXposedHelperKt.log("Error: " + e.getMessage(), false);
        }
    }

    @Override
    public void init() {
        hasEnable("customversiondata", true, () -> {
            if (XposedEntry.Companion.getVersionCode().equals("2301171")) {
                hook();
            } else {
                KotlinXposedHelperKt.log("This hook is not compatible with this version: " + XposedEntry.Companion.getVersionCode(), false);
            }
            return null;
        });
    }

}
