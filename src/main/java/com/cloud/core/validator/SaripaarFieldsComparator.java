package com.cloud.core.validator;

import com.cloud.core.validator.annotations.Order;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
class SaripaarFieldsComparator implements Comparator<Field> {
    private boolean mOrderedFields = true;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(final Field lhsField, final Field rhsField) {
        final Order lhsOrderAnnotation = lhsField.getAnnotation(Order.class);
        final Order rhsOrderAnnotation = rhsField.getAnnotation(Order.class);

        int comparison;
        if (lhsOrderAnnotation == null || rhsOrderAnnotation == null) {
            mOrderedFields = false;
            comparison = 0;
        } else {
            int lhsOrder = lhsOrderAnnotation.value();
            int rhsOrder = rhsOrderAnnotation.value();

            comparison = lhsOrder == rhsOrder
                    ? 0 : lhsOrder > rhsOrder ? 1 : -1;
        }

        return comparison;
    }

    /**
     * Tells if the fields are ordered. Useful only after the
     * {@link SaripaarFieldsComparator} is used to sort collection. Will
     * return true, if this method is called on an unused instance.
     *
     * @return true if all the fields are ordered, false otherwise.
     */
    public boolean areOrderedFields() {
        return mOrderedFields;
    }
}
