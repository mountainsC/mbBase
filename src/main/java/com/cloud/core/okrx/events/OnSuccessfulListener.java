package com.cloud.core.okrx.events;

import com.cloud.core.enums.ResultState;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class OnSuccessfulListener<T> {
    public void onSuccessful(T t, String reqKey) {

    }

    public void onSuccessful(T t, String reqKey, Object extras) {

    }

    public void onSuccessful(T t, ResultState resultState, String reqKey, Object extra) {

    }
}
