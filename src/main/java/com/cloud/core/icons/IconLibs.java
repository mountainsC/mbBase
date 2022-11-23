package com.cloud.core.icons;

import android.content.Context;
import android.graphics.Typeface;

import com.cloud.core.enums.FontType;


/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/20
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class IconLibs {
    private static IconLibs iconLibs = null;
    private Typeface iconfont = null;
    private Typeface traditionalFont = null;

    public static IconLibs getInstance() {
        return iconLibs == null ? iconLibs = new IconLibs() : iconLibs;
    }

    public Typeface getIconfont(Context context, FontType fontType) {
        if (fontType == FontType.agencyb) {
            if (traditionalFont == null) {
                traditionalFont = Typeface.createFromAsset(context.getAssets(), "fonts/AGENCYB.ttf");
            }
            return traditionalFont;
        }else {
            if (iconfont == null) {
                iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.ttf");
            }
            return iconfont;
        }
    }
}
