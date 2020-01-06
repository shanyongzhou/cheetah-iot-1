package com.cheetah.gateway.util;


import java.util.Locale;

/**
 * 对 UAP 的相关数据的静态工具方法
 */
public class UAPUtils {
    /**
     * 将 UAP 平台的 locale 转换为标准的 locale
     *
     * @param uapLocal UAP 平台传递的 locale 代码字符串
     * @return 标准化的 locale 代码字符串
     */
    public static String formatUAPLocal(String uapLocal) {
        switch (uapLocal) {
            case UAPConstants.UAP_APP_LOCAL_EN:
                return Locale.US.toString();
            case UAPConstants.UAP_APP_LOCAL_FR:
                return Locale.FRANCE.toString();
            case UAPConstants.UAP_APP_LOCAL_ZH:
                return Locale.SIMPLIFIED_CHINESE.toString();
            default:
                // 默认情况使用原 UAP 平台的 locale 代码
                return uapLocal;
        }
    }
}
