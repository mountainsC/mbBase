package com.cloud.core.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/18
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
@Retention(CLASS)
@Target({ANNOTATION_TYPE,TYPE,METHOD,CONSTRUCTOR,FIELD,PACKAGE})
public @interface RestrictTo {

    /**
     * The scope to which usage should be restricted.
     */
    Scope[] value();

    enum Scope {
        /**
         * Restrict usage to code within the same library (e.g. the same
         * gradle group ID and artifact ID).
         */
        LIBRARY,

        /**
         * Restrict usage to code within the same group of libraries.
         * This corresponds to the gradle group ID.
         */
        LIBRARY_GROUP,

        /**
         * Restrict usage to code within the same group ID (based on gradle
         * group ID). This is an alias for {@link #LIBRARY_GROUP}.
         *
         * @deprecated Use {@link #LIBRARY_GROUP} instead
         */
        @Deprecated
        GROUP_ID,

        /**
         * Restrict usage to tests.
         */
        TESTS,

        /**
         * Restrict usage to subclasses of the enclosing class.
         * <p>
         * <strong>Note:</strong> This scope should not be used to annotate
         * packages.
         */
        SUBCLASSES,
    }
}
