package com.cloud.core.utils;

import android.content.Context;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.util.SensorsDataUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Project: mb_android
 * @Package: com.mibao.utils
 * @Author: HSL
 * @Time: 2018/06/12 10:42
 * @E-mail: 13967189624@163.com
 * @Description: 神策埋点
 */
public class ShenCeStatisticsUtil {

    /**
     * 公共属性：平台类型
     */
    public static void platFormType() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("platformType", "Android");
            SensorsDataAPI.sharedInstance().registerSuperProperties(properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 底部菜单点击
     *
     * @param elementName 元素名称 : 首页/分类/发现/我的
     */
    public static void menuClick(String elementName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("menu_name", elementName);
            SensorsDataAPI.sharedInstance().track("MenuClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入首页区块
     *
     * @param blockName 区块名称： 限时特惠、春游必备、电玩空间、二手优选
     * @param blockMode 进入区块方式（Banner、更多）
     */
    public static void homeBlockClick(String blockName, String blockMode) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("block_name", blockName);
            properties.put("block_mode", blockMode);
            SensorsDataAPI.sharedInstance().track("BlockClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 领取礼包按钮
     * 预置属性
     */
    public static void getStoreGiftClick() {
        try {
            JSONObject properties = new JSONObject();
            SensorsDataAPI.sharedInstance().track("BookieBenefitBtnClick", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫一扫
     * 预置属性
     */
    public static void scanClick() {
        try {
            JSONObject properties = new JSONObject();
            SensorsDataAPI.sharedInstance().track("Scan", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 地图
     * 预置属性
     */
    public static void mapClick() {
        try {
            JSONObject properties = new JSONObject();
            SensorsDataAPI.sharedInstance().track("Map", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索
     *
     * @param location    搜索入口
     * @param keyword     搜索关键词
     * @param isHistory   是否使用历史词
     * @param isRecommend 是否使用推荐词
     * @param order       推荐词排序
     */
    public static void search(String location, String keyword, boolean isHistory, boolean isRecommend, int order) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("search_location", location);
            properties.put("keyword", keyword);
            properties.put("is_history", isHistory);
            properties.put("is_recommend", isRecommend);
            properties.put("recommend_order", order);
            SensorsDataAPI.sharedInstance().track("Search", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息中心
     *
     * @param choose : 消息通知/在线客服
     */
    public static void messageCenter(String choose) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("service_choose", choose);
            SensorsDataAPI.sharedInstance().track("MessageCenter", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击浏览店铺主页
     *
     * @param merchantId 店铺id
     * @param shopName   店铺名称
     * @param source     入口
     */
    public static void storeDetail(int merchantId, String shopName, String source) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("store_id", merchantId);
            properties.put("store_name", shopName);
            properties.put("entry_source", source);
            SensorsDataAPI.sharedInstance().track("StoreDetail", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 城市选择
     *
     * @param selectCity
     */
    public static void selectCity(String selectCity) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("city_name", selectCity);
            SensorsDataAPI.sharedInstance().track("CitySelect", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击浏览商品详情
     *
     * @param source         入口
     * @param goodsId        商品ID
     * @param goodsName      商品名称
     * @param firstCategory  商品一级分类
     * @param secondCategory 商品二级分类
     * @param price          租金
     * @param storeId        店铺ID
     * @param storeName      店铺名称
     */
    public static void commodityDetail(String source,
                                       String goodsId,
                                       String goodsName,
                                       String firstCategory,
                                       String secondCategory,
                                       String price,
                                       int storeId,
                                       String storeName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("entry_source", source);
            properties.put("commodity_id", goodsId);
            properties.put("commodity_name", goodsName);
            properties.put("first_category", firstCategory);
            properties.put("second_category", secondCategory);
            properties.put("price", price);
            properties.put("store_id", storeId);
            properties.put("store_name", storeName);
            SensorsDataAPI.sharedInstance().track("CommodityDetail", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实名认证页面，点击实名认证按钮时
     *
     * @param userId
     */
    public static void authenticationClick(long userId) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("user_id", String.valueOf(userId));
            SensorsDataAPI.sharedInstance().track("AuthenticationClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品一级分类
     *
     * @param categoryName
     */
    public static void firstCategory(String categoryName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("first_category", categoryName);
            SensorsDataAPI.sharedInstance().track("CategoryClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品二级分类
     *
     * @param categoryName
     */
    public static void secondCategory(String categoryName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("second_category", categoryName);
            SensorsDataAPI.sharedInstance().track("CategoryClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文章分类
     *
     * @param type
     */
    public static void articleTypeClick(String type) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("article_type", type);
            SensorsDataAPI.sharedInstance().track("ArticleTypeClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文章详情浏览
     *
     * @param articleName  文章标题
     * @param articleLabel 标签
     */
    public static void articleContent(String articleName, String articleLabel) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("article_name", articleName);
            properties.put("tag", articleLabel);
            SensorsDataAPI.sharedInstance().track("ArticleContent", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的页面元素点击
     *
     * @param elementName 元素名称
     */
    public static void meElementClick(String elementName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("element_name", elementName);
            SensorsDataAPI.sharedInstance().track("MeelementClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品分享
     *
     * @param goodsId
     * @param goodsName
     */
    public static void shareGoods(String goodsId, String goodsName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("share_object", "商品");
            properties.put("commodity_id", goodsId);
            properties.put("commodity_name", goodsName);
            SensorsDataAPI.sharedInstance().track("Share", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文章分享
     *
     * @param articleId
     * @param articleName
     */
    public static void shareArticle(int articleId, String articleName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("share_object", "文章");
            properties.put("article_id", articleId);
            properties.put("article_name", articleName);
            SensorsDataAPI.sharedInstance().track("Share", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 店铺分享
     *
     * @param storeId
     * @param storeName
     */
    public static void shareStore(String storeId, String storeName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("share_object", "店铺");
            properties.put("store_id", storeId);
            properties.put("store_name", storeName);
            SensorsDataAPI.sharedInstance().track("Share", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享平台
     *
     * @param platform
     */
    public static void sharePlatform(String platform) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("share_method", platform);
            SensorsDataAPI.sharedInstance().track("Share", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 活动分享统计
     *
     * @param activityName  活动名称
     * @param shareLocation 分享位置
     */
    public static void shareActivity(String activityName, String shareLocation) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("share_object", "活动");
            properties.put("activity_name", activityName);
            properties.put("share_location", shareLocation);
            SensorsDataAPI.sharedInstance().track("Share", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享成功统计
     *
     * @param activityName 活动名称
     */
    public static void shareSuccess(String activityName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("share_object", "分享成功");
            properties.put("activity_name", activityName);
            SensorsDataAPI.sharedInstance().track("Share", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 签到成功
     *
     * @param userId   用户ID
     * @param userName 用户名称
     * @param location 签到位置：签到按钮/首页弹窗
     * @param time     所属时间
     * @param count    签到次数
     */
    public static void signInSuccessful(String userId, String userName, String location, String time, String count) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("userID", userId);
            properties.put("user_name", userName);
            properties.put("source_location", location);
            properties.put("subordinate_to_the_time", time);
            properties.put("signed_in_the_number", count);
            SensorsDataAPI.sharedInstance().track("SignInSuccessfully", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品元素点击 ：服务协议/租赁到期/图文详情/商品参数/租赁规则/用户评价
     *
     * @param element
     */
    public static void goodsElementClick(String element) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("element_name", element);
            SensorsDataAPI.sharedInstance().track("CDelementClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 立即下单按钮点击
     *
     * @param commodityId   商品ID
     * @param commodityName 商品名称
     * @param sku           商品型号
     * @param leasePeriod   商品租期
     */
    public static void submitOrderBtnClick(String commodityId,
                                           String commodityName,
                                           String sku,
                                           String leasePeriod) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("commodity_id", commodityId);
            properties.put("commodity_name", commodityName);
            properties.put("commodity_sku", sku);
            properties.put("lease_period", leasePeriod);
            SensorsDataAPI.sharedInstance().track("SubmitOrderBtnClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交订单
     *
     * @param orderId          订单ID
     * @param goodsId          商品ID
     * @param goodsName        商品名称
     * @param goodsSku         商品型号
     * @param orderAmount      订单金额
     * @param receiverName     收货人姓名
     * @param receiverProvince 收货人省份
     * @param receiverCity     收货人城市
     * @param receiverArea     收货人地区
     * @param orderTime        下单时间
     * @param leasePeriod      商品租期
     * @param isUseCoupon      是否使用优惠券
     * @param couponAmount     优惠券金额
     * @param insurancePay     保险支付金额
     * @param rentAmount       租金金额
     * @param isCheckLater     是否勾选花呗授权
     * @param openTheDeposit   是否开启押金开关
     * @param deliveryMethod   收获方式
     */
    public static void confirmOrderBtnClick(String orderId,
                                            String goodsId,
                                            String goodsName,
                                            String goodsSku,
                                            double orderAmount,
                                            String receiverName,
                                            String receiverProvince,
                                            String receiverCity,
                                            String receiverArea,
                                            long orderTime,
                                            String leasePeriod,
                                            boolean isUseCoupon,
                                            double couponAmount,
                                            String insurancePay,
                                            double rentAmount,
                                            boolean isCheckLater,
                                            boolean openTheDeposit,
                                            String deliveryMethod) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("order_id", orderId);
            properties.put("commodity_id", goodsId);
            properties.put("commodity_name", goodsName);
            properties.put("commodity_sku", goodsSku);
            properties.put("order_amount", orderAmount);
            properties.put("receiver_name", receiverName);
            properties.put("receiver_province", receiverProvince);
            properties.put("receiver_city", receiverCity);
            properties.put("receiver_area", receiverArea);
            properties.put("order_time", orderTime);
            properties.put("lease_period", leasePeriod);
            properties.put("is_use_coupon", isUseCoupon);
            properties.put("coupon_amount", couponAmount);
            properties.put("insurance_pay_total", insurancePay);
            properties.put("rent_amount", rentAmount);
            properties.put("is_checkLater", isCheckLater);
            properties.put("open_the_deposit", openTheDeposit);
            properties.put("delivery_method", deliveryMethod);
            SensorsDataAPI.sharedInstance().track("ComfirmOrderBtnClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 立即支付按钮点击 （后端完成）
     *
     * @param orderId            订单ID
     * @param goodsId            商品ID
     * @param goodsName          商品名称
     * @param orderTotal         订单总额
     * @param realPay            实付金额
     * @param currentPeriod      当前账期
     * @param amountPay          应付金额
     * @param payType            支付方式：支付宝、微信、京东
     * @param couponName         优惠券名称
     * @param containsTheDeposit 是否包含押金
     * @param orderStatus        订单状态
     * @param couponAmount       优惠券金额
     */
    public static void payBtnClick(String orderId,
                                   String goodsId,
                                   String goodsName,
                                   double orderTotal,
                                   double realPay,
                                   String currentPeriod,
                                   double amountPay,
                                   String payType,
                                   String couponName,
                                   boolean containsTheDeposit,
                                   String orderStatus,
                                   String couponAmount) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("order_id", orderId);
            properties.put("commodity_id", goodsId);
            properties.put("commodity_name", goodsName);
            properties.put("order_total", orderTotal);
            properties.put("amount_of_payment", realPay);
            properties.put("current_period", currentPeriod);
            properties.put("amount_payable", amountPay);
            properties.put("pay_method", payType);
            properties.put("coupon_name", couponName);
            properties.put("contains_the_deposit", containsTheDeposit);
            properties.put("order_status", orderStatus);
            properties.put("coupon_amount", couponAmount);
            SensorsDataAPI.sharedInstance().track("PayBtnClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品收藏
     *
     * @param addOrCancel    新增/取消
     * @param goodsId
     * @param goodsName
     * @param firstCategory
     * @param secondCategory
     */
    public static void goodsCollection(String addOrCancel,
                                       String goodsId,
                                       String goodsName,
                                       String firstCategory,
                                       String secondCategory) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("favorites_type", "商品");
            properties.put("add_cancel", addOrCancel);
            properties.put("commodity_id", goodsId);
            properties.put("commodity_name", goodsName);
            properties.put("first_category", firstCategory);
            properties.put("second_category", secondCategory);
            SensorsDataAPI.sharedInstance().track("Favorites", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 店铺收藏
     *
     * @param addOrCancel
     * @param storeId
     * @param storeName
     */
    public static void shopCollection(String addOrCancel, int storeId, String storeName) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("favorites_type", "店铺");
            properties.put("add_cancel", addOrCancel);
            properties.put("store_id", storeId);
            properties.put("store_name", storeName);
            SensorsDataAPI.sharedInstance().track("Favorites", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     *
     * @param orderType          订单类型：租赁订单/买断申请/续租申请/售后申请
     * @param orderStatus        订单状态：预约租赁/待支付/续租待支付/租赁中
     * @param containsTheDeposit 是否包含押金
     */
    public static void cancelOrderClick(String orderType, String orderStatus, boolean containsTheDeposit) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("order_type", orderType);
            properties.put("order_status", orderStatus);
            properties.put("contains_the_deposit", containsTheDeposit);
            SensorsDataAPI.sharedInstance().track("CancelOrderClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 申请售后订单
     *
     * @param orderType 订单类型：维修/换货/退货
     */
    public static void applyForAfterSalesOrder(String orderType) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("type_of_after_sales_order", orderType);
            SensorsDataAPI.sharedInstance().track("ApplicationForAfter_salesOrders", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 售后申请成功
     *
     * @param applyType         申请类型：维修/换货/退货
     * @param applyReason       申请理由
     * @param orderId           订单Id
     * @param afterSalesOrderId 售后申请单ID
     * @param isInsurance       是否购买保险
     * @param goodsId           商品ID
     * @param businessId        商家ID
     * @param businessName      商家名称
     * @param firstCategory     一级商品分类
     * @param secondCategory    二级商品分类
     */
    public static void applyForAfterSalesOrderSuccess(String applyType,
                                                      String applyReason,
                                                      String orderId,
                                                      String afterSalesOrderId,
                                                      boolean isInsurance,
                                                      String goodsId,
                                                      String businessId,
                                                      String businessName,
                                                      String firstCategory,
                                                      String secondCategory) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("application_type", applyType);
            properties.put("apply_for_reason", applyReason);
            properties.put("order_id", orderId);
            properties.put("after_sales_application_ID", afterSalesOrderId);
            properties.put("free_insurance", isInsurance);
            properties.put("commodity_id", goodsId);
            properties.put("business_id", businessId);
            properties.put("business_name", businessName);
            properties.put("first_category", firstCategory);
            properties.put("second_category", secondCategory);
            SensorsDataAPI.sharedInstance().track("SuccessfulAfter_salesApplication", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 申请服务订单
     *
     * @param orderType 服务申请单类型 ：续租/买断
     */
    public static void applyForServiceOrder(String orderType) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("type_of_service_order", orderType);
            SensorsDataAPI.sharedInstance().track("ApplicationForServiceOrder", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 续租提交申请
     *
     * @param goodId                 商品ID
     * @param firstCategory          商品一级分类
     * @param goodsName              商品名称
     * @param goodsSku               商品型号
     * @param orderTotalRent         订单总租金
     * @param renewalDays            选择续租天数
     * @param renewalReferenceAmount 续租参考金额
     * @param isUseCoupon            是否使用优惠券
     */
    public static void renewApplyDetail(String goodId,
                                        String firstCategory,
                                        String goodsName,
                                        String goodsSku,
                                        double orderTotalRent,
                                        String renewalDays,
                                        double renewalReferenceAmount,
                                        boolean isUseCoupon) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("commodity_id", goodId);
            properties.put("first_category", firstCategory);
            properties.put("commodity_name", goodsName);
            properties.put("commodity_sku", goodsSku);
            properties.put("order_total_rent", orderTotalRent);
            properties.put("renewal_days", renewalDays);
            properties.put("reference_amount_for_renewal", renewalReferenceAmount);
            properties.put("is_use_coupon", isUseCoupon);
            SensorsDataAPI.sharedInstance().track("RenewalApplication", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 买断提交申请
     *
     * @param goodId                   商品ID
     * @param firstCategory            商品一级分类
     * @param goodsName                商品名称
     * @param goodsSku                 商品型号
     * @param orderTotalValue          订单商品总价值
     * @param orderPaidRent            订单商品已交租金
     * @param buyoutTheEstimatedAmount 买断预估金额
     */
    public static void buyOutSubmitApply(String goodId,
                                         String firstCategory,
                                         String goodsName,
                                         String goodsSku,
                                         double orderTotalValue,
                                         double orderPaidRent,
                                         double buyoutTheEstimatedAmount) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("commodity_id", goodId);
            properties.put("first_category", firstCategory);
            properties.put("commodity_name", goodsName);
            properties.put("commodity_sku", goodsSku);
            properties.put("order_total_value", orderTotalValue);
            properties.put("order_paid_rent", orderPaidRent);
            properties.put("buy_out_the_estimated_amount", buyoutTheEstimatedAmount);
            SensorsDataAPI.sharedInstance().track("Buy0utSubmitApplication", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付赔偿按钮点击 （后端完成）
     *
     * @param compensationReason 赔偿原因
     * @param commodityValue     商品价值
     * @param compensationAmount 赔偿金额
     * @param orderId            订单ID
     * @param goodsName          商品名称
     * @param goodsSku           商品规格
     * @param orderTime          下单时间
     * @param orderLease         订单租期
     * @param orderTotalRent     订单总金额
     */
    public static void payCompensationClick(String compensationReason,
                                            double commodityValue,
                                            double compensationAmount,
                                            String orderId,
                                            String goodsName,
                                            String goodsSku,
                                            String orderTime,
                                            String orderLease,
                                            double orderTotalRent) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("cause_of_compensation", compensationReason);
            properties.put("commodity_value", commodityValue);
            properties.put("amount_of_compensation", compensationAmount);
            properties.put("order_id", orderId);
            properties.put("commodity_name", goodsName);
            properties.put("commodity_model", goodsSku);
            properties.put("order_time", orderTime);
            properties.put("order_lease", orderLease);
            properties.put("order_total_rent", orderTotalRent);
            SensorsDataAPI.sharedInstance().track("PayCompensationPressClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 邀请好友
     *
     * @param isInvite        是否被邀请 已填写好友邀请码视为被邀请
     * @param saveAlbumNumber 保存到相册点击次数
     * @param inviteNumber    邀请好友数量
     */
    public static void inviteFriend(boolean isInvite, int saveAlbumNumber, int inviteNumber) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("be_invited", isInvite);
            properties.put("save_to_album", saveAlbumNumber);
            properties.put("number_of_invitation", inviteNumber);
            SensorsDataAPI.sharedInstance().track("FriendRequest", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 联系客服
     *
     * @param pageSource     页面来源
     * @param customerSource 客服来源（客服电话、在线客服）
     */
    public static void contactCustomerService(String pageSource, String customerSource) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("channel_source", "APP");
            properties.put("page_source", pageSource);
            properties.put("customer_service_source", customerSource);
            SensorsDataAPI.sharedInstance().track("ContactCustomerService", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 渠道追踪
     *
     * @param context
     */
    public static void appInstall(Context context) {
        try {
            String downloadChannel = null;
            // 获取 <application> 中的 <meta-data>
            // 获取下载商店的渠道
            downloadChannel = SensorsDataUtils.getApplicationMetaData(context, "UMENG_CHANNEL");
            //初始化我们SDK后 调用这段代码，用于记录安装事件、渠道追踪。
            JSONObject properties = new JSONObject();
            properties.put("DownloadChannel", downloadChannel);//这里的 DownloadChannel 负责记录下载商店的渠道。
            //这里安装事件取名为 AppInstall。
            //注意 由于要追踪不同渠道链接中投放的推广渠道，所以 Manifest 中不能按照“方案一”神策meta-data方式定制渠道信息，代码中也不能传入 $utm_ 开头的渠道字段！！！
            SensorsDataAPI.sharedInstance(context).trackInstallation("AppInstall", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 支付结果
     *
     * @param payTime          支付时间
     * @param payReason        支付原因
     * @param payAmount        支付金额
     * @param theDepositAmount 押金金额
     * @param isSuccess        是否成功
     */
    public static void payResult(long payTime,
                                 String payReason,
                                 int payAmount,
                                 int theDepositAmount,
                                 boolean isSuccess) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("pay_time", payTime);
            properties.put("pay_reason", payReason);
            properties.put("pay_amount", payAmount);
            properties.put("the_deposit_amount", theDepositAmount);
            properties.put("is_success", isSuccess);
            SensorsDataAPI.sharedInstance().track("PayResult", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付结果
     *
     * @param cancelTheReason  取消原因
     * @param commodityId      商品ID
     * @param commodityName    商品名称
     * @param couponName       优惠券名称
     * @param couponAmount     优惠券金额
     * @param orderAmount      订单金额
     * @param theDepositAmount 押金金额
     */
    public static void cancelOrderCompletion(String cancelTheReason,
                                             String commodityId,
                                             String commodityName,
                                             String couponName,
                                             int couponAmount,
                                             int orderAmount,
                                             int theDepositAmount) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("cancel_the_reason", cancelTheReason);
            properties.put("commodity_id", commodityId);
            properties.put("commodity_name", commodityName);
            properties.put("coupon_name", couponName);
            properties.put("coupon_amount", couponAmount);
            properties.put("order_amount", orderAmount);
            properties.put("the_deposit_amount", theDepositAmount);
            SensorsDataAPI.sharedInstance().track("CancelOrderCompletion", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
