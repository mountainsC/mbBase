package com.cloud.core.view.vlayout;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/22
 * @Description:布局类型
 * @Modifier:
 * @ModifyContent:
 */
public enum VLayoutType {
    /**
     * 线性横向
     */
    LinearHorizontal,
    /**
     * 线性纵向
     */
    LinearVertical,
    /**
     * 线性纵向瀑布形式
     */
    LinearVerticalStaggered,
    /**
     * 1拖N(最多5个)
     */
    OnePlusN,
    /**
     * 单个对象
     */
    SingleObject,
    /**
     * 当页面滑动到该图片区域才显示
     */
    ScrollFix
}
