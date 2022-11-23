package com.cloud.core.validator.rules;

import android.text.TextUtils;

import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.validator.AnnotationRule;
import com.cloud.core.validator.annotations.Compare;
import com.cloud.core.validator.enums.CompareSymbol;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class CompareRule extends AnnotationRule<Compare, Object> {

    //引用属性值
    private Object refValue = null;

    public CompareRule(Compare compare, Object refValue) {
        super(compare);
        this.refValue = refValue;
    }

    @Override
    public boolean isValid(Object object) {
        if (object == null || refValue == null) {
            return false;
        }
        if (CompareSymbol.eq == mRuleAnnotation.symbol()) {
            //比较对象相等
            return compareObjectEq(object);
        } else if (CompareSymbol.greater == mRuleAnnotation.symbol()) {
            //比较数值是否大于引用数值
            return compareNumberGreater(object);
        } else if (CompareSymbol.less == mRuleAnnotation.symbol()) {
            //比较数值是否小于引用数值
            return compareNumberLess(object);
        }
        return true;
    }

    private boolean compareNumberLess(Object object) {
        if ((object instanceof Integer ||
                object instanceof Double ||
                object instanceof Long ||
                object instanceof Float) && (
                refValue instanceof Integer ||
                        refValue instanceof Double ||
                        refValue instanceof Long ||
                        refValue instanceof Float
        )) {
            double ref = ConvertUtils.toDouble(refValue);
            double value = ConvertUtils.toDouble(object);
            return value < ref;
        }
        return false;
    }

    private boolean compareNumberGreater(Object object) {
        if ((object instanceof Integer ||
                object instanceof Double ||
                object instanceof Long ||
                object instanceof Float) && (
                refValue instanceof Integer ||
                        refValue instanceof Double ||
                        refValue instanceof Long ||
                        refValue instanceof Float
        )) {
            double ref = ConvertUtils.toDouble(refValue);
            double value = ConvertUtils.toDouble(object);
            return value > ref;
        }
        return false;
    }

    private boolean compareObjectEq(Object object) {
        if ((object instanceof Integer ||
                object instanceof Double ||
                object instanceof Long ||
                object instanceof Float) && (
                refValue instanceof Integer ||
                        refValue instanceof Double ||
                        refValue instanceof Long ||
                        refValue instanceof Float
        )) {
            double ref = ConvertUtils.toDouble(refValue);
            double value = ConvertUtils.toDouble(object);
            return ref == value;
        } else if ((object instanceof String) && (refValue instanceof String)) {
            String ref = String.valueOf(refValue).trim();
            String value = String.valueOf(object).trim();
            return TextUtils.equals(ref, value);
        } else {
            return object == refValue;
        }
    }
}
