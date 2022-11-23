package com.cloud.core.enums;

import android.text.TextUtils;

import com.cloud.core.constants.client.keys.Certification;
import com.cloud.core.constants.client.keys.CreditAuth;
import com.cloud.core.constants.client.keys.GoodsDetail;
import com.cloud.core.constants.client.keys.InfoEvaluationFail;
import com.cloud.core.constants.client.keys.MoreGoods;
import com.cloud.core.constants.client.keys.OrderDetail;
import com.cloud.core.constants.client.keys.OrderList;
import com.cloud.core.constants.client.keys.PayRentMoney;
import com.cloud.core.constants.client.keys.RiskModel;
import com.cloud.core.constants.mer.ModuleKeys;
import com.cloud.core.constants.mer.keys.MerchentGoodsDetails;
import com.cloud.core.constants.mer.keys.StoreList;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/25
 * @Description:各模块Activity名称
 * @Modifier:
 * @ModifyContent:
 */
public enum ActivityNames {
    //首页
    Main("/main/", 1),
    //图片预览
    PreviewImageActivity("/image/preview/", 1,
            "urlList=IMG_URLS",
            "isFullPath=FULL_ADDRESS",
            "position=POSITION"),
    //登录页面
    LoginActivity("/login/", 1),
    //店铺主页
    StoreActivity("/mer/home/", 1,
            "merId=MERCHANT_ID"),
    //地址管理
    AddressManagerActivity("/address/list/", 1,
            "stype=sourceType"),
    //h5页面
    H5WebViewActivity("/h5/", 1,
            "url=URL",
            "isUrl=IS_URL",
            "isHideShare=isHideShare"),
    //聊天页面
    ChatActivity("/chat/customer/service/", 1,
            "tId=targetId",
            "tname=targetName"),
    //地址编辑页面
    EditAddressActivity("/user/address/edit/", 1,
            "dt=DATA_INFO"),
    //商品详情
    GoodsDetailActivity("/pd/detail/", 2,
            String.format("gid=%s", GoodsDetail.goodsId),
            String.format("aid=%s", GoodsDetail.activityId),
            String.format("source=%s", GoodsDetail.source),
            String.format("isPreview=%s", GoodsDetail.isPreview)),
    //信息评估(认证)失败页面
    InfoEvaluationFailActivity("/auth/fail/", 1,
            String.format("content=%s", InfoEvaluationFail.content),
            String.format("isVBin=%s", InfoEvaluationFail.isVBin)),
    //信用授权（实名信息-芝麻信用授权-京东小白授权）
    //authType 0:实名认证;1:芝麻认证;2:京东认证;
    CreditAuthorizationActivity("/credit/auth/", 1,
            String.format("authType=%s", CreditAuth.authType)),
    //订单确认页面
    OrderSureActivity("/order/sure/", 1),
    //支付租金页面
    PayRentMoneyActivity("/order/pay/", 1,
            "orderId=" + PayRentMoney.orderId,
            "orderState=" + PayRentMoney.orderState),
    //订单详情
    OrderDetailActivity("/order/detail/", 1,
            "orderId=" + OrderDetail.orderId,
            "orderNumber=" + MerchentGoodsDetails.orderNumberId,
            "position=" + OrderDetail.position),
    //订单列表
    OrderListActivity("/order/list/", 1,
            "state=" + OrderList.state,
            "isShop=" + OrderList.isShop),
    //附近的店
    NearbyMerchantActivity("/mer/near/", 1),
    //更多(关联)商品
    MoreGoodsActivity("/pd/more/associated/", 1,
            "goodsId=" + MoreGoods.goodsId,
            "title=" + MoreGoods.title,
            "position=" + OrderDetail.position),
    //风控
    RiskControlActivity("/risk/model/", 1, "riskDataJson=" + RiskModel.riskDataJson),
    //添加、续费模块
    OpenAppModelActivity("/application/model/continue/", 1, "moduleId=" + ModuleKeys.addContinueModuleId),
    //店铺列表
    StoreListActivity("/store/list/", 1,
            "moduleId=" + StoreList.moduleId,
            "validTime=" + StoreList.validTime),
    //实名认证
    CertificationActivity("/user/real/name/", 1,
            "status=" + Certification.status);

    private String schemeKey = "";
    private int schemeVersion = 0;
    private String[] mappers = null;

    private ActivityNames(String schemeKey, int schemeVersion, String... mappers) {
        this.schemeKey = schemeKey;
        this.schemeVersion = schemeVersion;
        this.mappers = mappers;
    }

    public String getSchemeKey() {
        return this.schemeKey;
    }

    public int getSchemeVersion() {
        return this.schemeVersion;
    }

    public String[] getMappers() {
        return this.mappers;
    }

    public static final ActivityNames getActivityNames(String name) {
        ActivityNames currEnum = null;
        for (ActivityNames e : ActivityNames.values()) {
            if (TextUtils.equals(e.name(), name)) {
                currEnum = e;
                break;
            }
        }
        return currEnum;
    }
}
