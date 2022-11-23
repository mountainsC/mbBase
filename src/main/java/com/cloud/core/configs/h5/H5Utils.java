package com.cloud.core.configs.h5;

import android.text.TextUtils;

import com.cloud.core.enums.RuleParams;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.ValidUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/30
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class H5Utils {
    /**
     * 背影颜色与当前颜色是否匹配
     *
     * @param bgcolor   背影颜色
     * @param thisColor 当前颜色
     * @return true-匹配;false-不匹配;
     */
    public static boolean isMatchThisColor(String bgcolor, String thisColor) {
        try {
            if (TextUtils.isEmpty(bgcolor)) {
                return false;
            }
            String pattern = String.format(RuleParams.MatchTagBetweenContent.getValue(), "\\(", "\\)");
            String text = ValidUtils.matche(pattern, bgcolor);
            String mcolor = "";
            if (TextUtils.isEmpty(text)) {
                mcolor = bgcolor;
            } else {
                int r = 0, g = 0, b = 0;
                String[] rgbs = text.split(",");
                if (rgbs.length == 3) {
                    r = ConvertUtils.toInt(rgbs[0]);
                    g = ConvertUtils.toInt(rgbs[1]);
                    b = ConvertUtils.toInt(rgbs[2]);
                    mcolor = ConvertUtils.toRGBHex(r, g, b);
                } else if (rgbs.length == 4) {
                    if (TextUtils.equals(rgbs[0], "0")) {
                        mcolor = thisColor;
                    }
                }
            }
            boolean isWhite = false;
            if (!TextUtils.isEmpty(mcolor)) {
                mcolor = mcolor.toLowerCase();
                if (TextUtils.equals(mcolor, thisColor)) {
                    isWhite = true;
                }
            }
            return isWhite;
        } catch (Exception e) {
            return false;
        }
    }
}
