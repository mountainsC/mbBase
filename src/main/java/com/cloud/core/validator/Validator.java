package com.cloud.core.validator;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.validator.annotations.Compare;
import com.cloud.core.validator.annotations.CompareId;
import com.cloud.core.validator.annotations.Length;
import com.cloud.core.validator.annotations.Max;
import com.cloud.core.validator.annotations.Min;
import com.cloud.core.validator.annotations.NotEmpty;
import com.cloud.core.validator.annotations.Order;
import com.cloud.core.validator.annotations.Pattern;
import com.cloud.core.validator.rules.CompareRule;
import com.cloud.core.validator.rules.LengthRule;
import com.cloud.core.validator.rules.MaxRule;
import com.cloud.core.validator.rules.MinRule;
import com.cloud.core.validator.rules.NotEmptyRule;
import com.cloud.core.validator.rules.PatternRule;

import org.greenrobot.greendao.annotation.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:验证工具类
 * Modifier:
 * ModifyContent:
 */
class Validator {

    private OnValidationListener onValidationListener = null;
    private int checkSuccessCount = 0;//检测成功数
    private int checkCount = 0;//需要检测数
    //有效字段
    private List<Field> annotatedFields = new ArrayList<Field>();
    //当前验证字段索引
    private int currValidPosition = 0;
    private FilterFieldTask filterFieldTask = null;

    /**
     * 设置验证监听
     *
     * @param listener 验证监听
     */
    public void setOnValidationListener(@NotNull OnValidationListener listener) {
        this.onValidationListener = listener;
    }

    /**
     * 验证
     *
     * @param model view model object
     */
    @SuppressLint("StaticFieldLeak")
    public void validate(Object model) {
        if (model == null || onValidationListener == null) {
            return;
        }
        //rxcore未导入rxjava因此采用
        GlobalUtils.cancelTask(filterFieldTask);
        filterFieldTask = new FilterFieldTask();
        filterFieldTask.execute(model);
    }

    private class FilterFieldTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            checkSuccessCount = 0;
            checkCount = 0;
            annotatedFields.clear();
        }

        @Override
        protected Object doInBackground(Object... objects) {
            Object model = objects[0];
            getFirstOrderAnnotatedField(model.getClass());
            return model;
        }

        @Override
        protected void onPostExecute(Object model) {
            sigleValidate(model);
        }
    }

    private void sigleValidate(Object model) {
        if (annotatedFields.size() > currValidPosition) {
            Field field = annotatedFields.get(currValidPosition);
            Annotation[] annotations = field.getDeclaredAnnotations();
            validateTill(model, field, annotations);
        }
    }

    private TreeMap<Integer, Annotation> annoSort(Annotation[] annotations) {
        TreeMap<Integer, Annotation> map = new TreeMap<Integer, Annotation>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        for (Annotation annotation : annotations) {
            if (annotation instanceof NotEmpty) {
                map.put(1, annotation);
            } else if (annotation instanceof Length) {
                map.put(2, annotation);
            } else if (annotation instanceof Min) {
                map.put(3, annotation);
            } else if (annotation instanceof Max) {
                map.put(4, annotation);
            } else if (annotation instanceof Pattern) {
                map.put(5, annotation);
            } else if (annotation instanceof Compare) {
                map.put(6, annotation);
            }
        }
        return map;
    }

    private void validateTill(Object model, Field field, Annotation[] annotations) {
        Object value = GlobalUtils.getPropertiesValue(model, field.getName());
        //排序将Compare验证放至最后
        TreeMap<Integer, Annotation> map = annoSort(annotations);
        //对当前属性做校验
        for (Map.Entry<Integer, Annotation> entry : map.entrySet()) {
            if (matchcAnnotationType(model, entry.getValue(), value)) {
                break;
            }
        }
    }

    private boolean matchcAnnotationType(Object model, Annotation annotation, Object value) {
        if (annotation.annotationType() == NotEmpty.class) {
            return emptyTip(model, annotation, value);
        } else if (annotation.annotationType() == Length.class) {
            return lengthTip(model, annotation, value);
        } else if (annotation.annotationType() == Min.class) {
            return minTip(model, annotation, value);
        } else if (annotation.annotationType() == Max.class) {
            return maxTip(model, annotation, value);
        } else if (annotation.annotationType() == Pattern.class) {
            return patternTip(model, annotation, value);
        } else if (annotation.annotationType() == Compare.class) {
            return compareTip(model, annotation, value);
        }
        return true;
    }

    private void validSuccessDealWith(Object model) {
        checkSuccessCount++;
        if (checkCount <= checkSuccessCount) {
            onValidationListener.onValidationSucceeded();
        } else {
            currValidPosition++;
            sigleValidate(model);
        }
    }

    private void validFailedDealWith(String message) {
        onValidationListener.onValidationFailed(message);
    }

    private String getCompareId(Annotation[] annotations) {
        String compareId = "";
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == CompareId.class) {
                CompareId cid = (CompareId) annotation;
                compareId = cid.value();
                break;
            }
        }
        return compareId;
    }

    private Object getRefValue(Object model, String refId) {
        Object refValue = null;
        for (Field field : annotatedFields) {
            //获取字段标识id
            Annotation[] annotations = field.getDeclaredAnnotations();
            String compareId = getCompareId(annotations);
            //如果id取其值
            if (TextUtils.equals(compareId, refId)) {
                refValue = GlobalUtils.getPropertiesValue(model, field.getName());
                break;
            }
        }
        return refValue;
    }

    private boolean compareTip(Object model, Annotation annotation, Object value) {
        //获取引用属性值
        Compare compare = (Compare) annotation;
        Object refValue = getRefValue(model, compare.refId());
        CompareRule compareRule = new CompareRule(compare, refValue);
        if (compareRule.isValid(value)) {
            validSuccessDealWith(model);
            return false;
        } else {
            validFailedDealWith(compare.message());
            return true;
        }
    }

    private boolean patternTip(Object model, Annotation annotation, Object value) {
        Pattern pattern = (Pattern) annotation;
        PatternRule patternRule = new PatternRule(pattern);
        if (patternRule.isValid(value)) {
            validSuccessDealWith(model);
            return false;
        } else {
            validFailedDealWith(pattern.message());
            return true;
        }
    }

    private boolean minTip(Object model, Annotation annotation, Object value) {
        Min min = (Min) annotation;
        MinRule minRule = new MinRule(min);
        if (minRule.isValid(value)) {
            validSuccessDealWith(model);
            return false;
        } else {
            validFailedDealWith(min.message());
            return true;
        }
    }

    private boolean maxTip(Object model, Annotation annotation, Object value) {
        Max max = (Max) annotation;
        MaxRule maxRule = new MaxRule(max);
        if (maxRule.isValid(value)) {
            validSuccessDealWith(model);
            return false;
        } else {
            validFailedDealWith(max.message());
            return true;
        }
    }

    private boolean lengthTip(Object model, Annotation annotation, Object value) {
        Length length = (Length) annotation;
        LengthRule lengthRule = new LengthRule(length);
        if (lengthRule.isValid(value)) {
            validSuccessDealWith(model);
            return false;
        } else {
            validFailedDealWith(length.message());
            return true;
        }
    }

    private boolean emptyTip(Object model, Annotation annotation, Object value) {
        NotEmpty empty = (NotEmpty) annotation;
        NotEmptyRule emptyRule = new NotEmptyRule(empty);
        if (emptyRule.isValid(value)) {
            validSuccessDealWith(model);
            return false;
        } else {
            validFailedDealWith(empty.message());
            return true;
        }
    }

    private void checkStat(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            //排除CompareId注解
            if (isMatchAnnotation(annotation) && (annotation.annotationType() != CompareId.class)) {
                checkCount++;
            }
        }
    }

    private void getFirstOrderAnnotatedField(Class<?> controllerClass) {
        Field[] declaredFields = controllerClass.getDeclaredFields();
        for (Field field : declaredFields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (isSaripaarAnnotated(annotations)) {
                annotatedFields.add(field);
                //count check number
                checkStat(annotations);
            }
        }
        // Sort
        SaripaarFieldsComparator comparator = new SaripaarFieldsComparator();
        Collections.sort(annotatedFields, comparator);
    }

    private boolean hasOrderAnnotation(Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        boolean flag = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Order.class) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private boolean isMatchAnnotation(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (annotationType == Length.class ||
                annotationType == Min.class ||
                annotationType == Max.class ||
                annotationType == NotEmpty.class ||
                annotationType == Pattern.class ||
                annotationType == Compare.class ||
                annotationType == CompareId.class) {
            return true;
        }
        return false;
    }

    private boolean isSaripaarAnnotated(Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        boolean hasOrderAnnotation = hasOrderAnnotation(annotations);
        boolean hasSaripaarAnnotation = false;

        if (!hasOrderAnnotation) {
            for (Annotation annotation : annotations) {
                if (isMatchAnnotation(annotation)) {
                    hasSaripaarAnnotation = true;
                    break;
                }
            }
        }

        return hasOrderAnnotation || hasSaripaarAnnotation;
    }
}
