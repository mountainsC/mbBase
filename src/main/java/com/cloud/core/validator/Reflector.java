package com.cloud.core.validator;

import com.cloud.core.validator.annotations.ValidateUsing;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
class Reflector {
    /**
     * Retrieves the attribute method of the given {@link Annotation}.
     *
     * @param annotationType The {@link Annotation}
     *                       {@link Class} to check.
     * @param attributeName  Attribute name.
     * @return The {@link Method} if the attribute is present,
     * null otherwise.
     */
    public static Method getAttributeMethod(final Class<? extends Annotation> annotationType,
                                            final String attributeName) {
        Method attributeMethod = null;
        try {
            attributeMethod = annotationType.getMethod(attributeName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return attributeMethod;
    }

    /**
     * Retrieve an attribute value from an {@link Annotation}.
     *
     * @param annotation        An {@link Annotation} instance.
     * @param attributeName     Attribute name.
     * @param attributeDataType {@link Class} representing the attribute data type.
     * @param <T>               Attribute value type.
     * @return The attribute value.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttributeValue(final Annotation annotation, final String attributeName,
                                          final Class<T> attributeDataType) {

        T attributeValue = null;
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Method attributeMethod = getAttributeMethod(annotationType, attributeName);

        if (attributeMethod == null) {
            String message = String.format("Cannot find attribute '%s' in annotation '%s'.",
                    attributeName, annotationType.getName());
            throw new IllegalStateException(message);
        } else {
            try {
                Object result = attributeMethod.invoke(annotation);
                attributeValue = attributeDataType.isPrimitive()
                        ? (T) result
                        : attributeDataType.cast(result);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return attributeValue;
    }

    /**
     * Checks if an annotation was annotated with the given annotation.
     *
     * @param inspected The {@link Annotation} to be checked.
     * @param expected  The {@link Annotation} that we are looking for.
     * @return true if the annotation is present, false otherwise.
     */
    public static boolean isAnnotated(final Class<? extends Annotation> inspected,
                                      final Class<? extends Annotation> expected) {
        boolean isAnnotated = false;
        Annotation[] declaredAnnotations = inspected.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            isAnnotated = expected.equals(declaredAnnotation.annotationType());
            if (isAnnotated) {
                break;
            }
        }
        return isAnnotated;
    }

    /**
     * Method finds the data type of the {@link AnnotationRule} that is tied up to the given rule
     * annotation.
     *
     * @param ruleAnnotation Rule {@link Annotation}.
     * @return The expected data type for the
     */
    public static Class<?> getRuleDataType(final Annotation ruleAnnotation) {
        ValidateUsing validateUsing = getValidateUsingAnnotation(ruleAnnotation.annotationType());
        return getRuleDataType(validateUsing);
    }

    /**
     * Method finds the data type of the {@link AnnotationRule} that is tied up to the given rule
     * annotation.
     *
     * @param validateUsing
     * @return The expected data type for the
     */
    public static Class<?> getRuleDataType(final ValidateUsing validateUsing) {
        Class<? extends AnnotationRule> rule = validateUsing.value();
        Method[] methods = rule.getDeclaredMethods();
        return getRuleTypeFromIsValidMethod(rule, methods);
    }

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *  Private Methods
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    private static ValidateUsing getValidateUsingAnnotation(
            final Class<? extends Annotation> annotationType) {
        ValidateUsing validateUsing = null;

        Annotation[] declaredAnnotations = annotationType.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            if (ValidateUsing.class.equals(annotation.annotationType())) {
                validateUsing = (ValidateUsing) annotation;
                break;
            }
        }
        return validateUsing;
    }

    private static Class<?> getRuleTypeFromIsValidMethod(final Class<? extends AnnotationRule> rule,
                                                         final Method[] methods) {

        Class<?> returnType = null;
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();

            if (matchesIsValidMethodSignature(method, parameterTypes)) {
                // This will be null, if there are no matching methods
                // in the class with a similar signature.
                if (returnType != null) {
                    String message = String.format(
                            "Found duplicate 'boolean isValid(T)' method signature in '%s'.",
                            rule.getName());
                    throw new SaripaarViolationException(message);
                }
                returnType = parameterTypes[0];
            }
        }
        return returnType;
    }

    private static boolean matchesIsValidMethodSignature(final Method method,
                                                         final Class<?>[] parameterTypes) {
        int modifiers = method.getModifiers();

        boolean isPublic = Modifier.isPublic(modifiers);
        boolean nonVolatile = !Modifier.isVolatile(modifiers);
        boolean returnsBoolean = Boolean.TYPE.equals(method.getReturnType());
        boolean matchesMethodName = "isValid".equals(method.getName());
        boolean hasSingleParameter = parameterTypes.length == 1;

        return isPublic && nonVolatile && returnsBoolean && matchesMethodName && hasSingleParameter;
    }

    private Reflector() {
    }
}
