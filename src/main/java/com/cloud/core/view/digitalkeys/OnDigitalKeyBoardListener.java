package com.cloud.core.view.digitalkeys;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/9
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnDigitalKeyBoardListener {
    /**
     * editText ACTION_UP事件回调
     *
     * @param editText
     * @param event
     */
    public void onEditTextActionUp(EditText editText, MotionEvent event);

    /**
     * @param inputType     输出类型
     * @param digitalSymbol 数字或符号
     * @param editText      当前聚集的editText
     */
    public void onKeyBoardListener(InputDigitalKeyType inputType, String digitalSymbol, EditText editText);
}
