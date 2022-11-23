package com.cloud.core.glides;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.configs.BaseCConfig;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.enums.ImgRuleType;
import com.cloud.core.utils.GlobalUtils;

import java.text.MessageFormat;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/12/30
 * Description:jpg图片数据源
 * Modifier:
 * ModifyContent:
 */
public class FormatDataModel {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static String getUrl(Context context,
                                String dataModelUrl,
                                ImgRuleType ruleType,
                                int imgWidth,
                                int imgHeight,
                                int imgCorners) {
        if (screenWidth == 0) {
            screenWidth = GlobalUtils.getScreenWidth(context);
        }
        if (screenHeight == 0) {
            screenHeight = GlobalUtils.getScreenHeight(context);
        }
        if (imgWidth > screenWidth) {
            imgWidth = screenWidth;
        }
        if (imgHeight > screenHeight) {
            imgHeight = screenHeight;
        }
        String resultUrl = dataModelUrl;
        RxCoreConfigItems configItems = BaseCConfig.getInstance().getConfigItems(context);
        if (TextUtils.equals(configItems.getImagePlatformType(), "ALIBABA")) {
            resultUrl = buildSuffix(context,
                    dataModelUrl,
                    ruleType,
                    imgWidth,
                    imgHeight,
                    imgCorners);
        } else if (TextUtils.equals(configItems.getImagePlatformType(), "QINIU")) {
            resultUrl = qiniuImgProcessRule(context,
                    dataModelUrl,
                    ruleType,
                    imgWidth,
                    imgHeight,
                    imgCorners);
        }
        return resultUrl;
    }

    private static String qiniuImgProcessRule(Context context,
                                              String url,
                                              ImgRuleType ruleType,
                                              int imgWidth,
                                              int imgHeight,
                                              int imgCorners) {
        if (ruleType == ImgRuleType.GeometricForWidth) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth));
        } else if (ruleType == ImgRuleType.GeometricRoundedCornersForWidth) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgCorners > 0 ? imgCorners : 12));
        } else if (ruleType == ImgRuleType.TailoringCircle) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth / 2));
        } else if (ruleType == ImgRuleType.TailoringWHRectangular) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgHeight));
        } else if (ruleType == ImgRuleType.TailoringWHRectangularRoundedCorners) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgHeight), String.valueOf(imgCorners));
        } else if (ruleType == ImgRuleType.GeometricCircleForWidth) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgCorners));
        }
        return url;
    }

    private static String buildSuffix(Context context,
                                      String url,
                                      ImgRuleType ruleType,
                                      int imgWidth,
                                      int imgHeight,
                                      int imgCorners) {
        if (ruleType == ImgRuleType.GeometricForWidth) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth));
        } else if (ruleType == ImgRuleType.GeometricRoundedCornersForWidth) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgCorners > 0 ? imgCorners : 12));
        } else if (ruleType == ImgRuleType.TailoringCircle) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth / 2));
        } else if (ruleType == ImgRuleType.TailoringWHRectangular) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgHeight));
        } else if (ruleType == ImgRuleType.TailoringWHRectangularRoundedCorners) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgHeight), String.valueOf(imgCorners));
        } else if (ruleType == ImgRuleType.GeometricCircleForWidth) {
            return url + MessageFormat.format(ruleType.getRule(context), String.valueOf(imgWidth), String.valueOf(imgCorners));
        }
        return url;
    }
}
