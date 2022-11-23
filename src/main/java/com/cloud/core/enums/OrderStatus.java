package com.cloud.core.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/10/12
 * @Description:订单状态
 * @Modifier:
 * @ModifyContent:
 */
public enum OrderStatus {

    /**
     * 待系统审核
     * 用户创建订单后的状态
     */
    PendingSystemCreaditCheck("pending_system_credit_check"),
    /**
     * 等待机密信用审核
     */
    PendingJimiCreditCheck("pending_jimi_credit_check"),
    /**
     * 待人工审核
     * 机审通过后成为人工待审核
     */
    PendingArtificialCreditCheck("pending_artificial_credit_check"),
    /**
     * 待审核
     */
    pendingOrderReceiving("pending_order_receiving"),
    /**
     * 待支付
     * 人工审核通过后待支付
     */
    PendingPay("pending_pay"),
    /**
     * 待发货
     * 用户支付成功后商家代发货
     */
    PendingSendGoods("pending_send_goods"),
    /**
     * 待收货
     * 商家发货成功后用户待收货（发货五天后为进行中，或者顺丰接口接口数据为已签收）
     */
    PendingReceiveGoods("pending_receive_goods"),
    pendingRunning("pending_running"),
    /**
     * 进行中
     * 收货成功后状态，系统开始计算租金
     */
    Running("running"),
    /**
     * 进行中
     * 收货成功后状态，系统开始计算租金
     */
    pendingCheck("pending_check"),
    /**
     * 到期待退还
     * 租赁时间到期后，并且用户把所有的款项结算完成时的状态
     */
    PendingReturn("pending_return"),
    /**
     * 退还中
     * 用户预约还机后状态
     */
    Returning("returning"),
    /**
     * 退还逾期
     * 用户未在有效期内退还商品
     */
    ReturnOverdue("return_overdue"),
    /**
     * 租赁逾期
     * 用户未在有效期内退还商品
     */
    RunningOverdue("running_overdue"),
    /**
     * 已退还
     * 用户预约还机后状态(商家未收到货)
     */
    ReturnUnreceived("returned_unreceived"),
    /**
     * 已退还
     * 用户预约还机后状态(商家提出未收到货后平台已经处理过的状态)
     */
    ReturnReceived("returned_received"),
    /**
     * 平台处理中
     * 商家申请未收到货后的状态，平台沟通后修改为《已退还》状态
     */
    SystemsDisposal("systems_disposal"),
    /**
     * 等待平台审核
     * 商家收到用户还机后商品觉得有损坏时可以申请用户赔偿
     */
    PendingCompensateCheck("pending_compensate_check"),
    /**
     * 赔偿逾期
     * 用户未按时赔偿
     */
    CompensateOverdue("compensate_overdue"),
    /**
     * 等待用户赔偿
     * 商家申请用户赔偿大后台通过审核后等待用户赔偿
     */
    PendingUserCompensate("pending_user_compensate"),
    /**
     * 待退押金
     * 商户收到货确认商品没有任何问题时状态为待退押金
     */
    PendingRefundDeposit("pending_refund_deposit"),
    /**
     * 退押金审核
     * 商户确认退款后等待大后台审核
     */
    PendingRefundDepositCheck("pending_refund_deposit_check"),
    /**
     * 租赁完成
     * 押金退还成功后为已完成状态
     */
    LeaseFinished("lease_finished"),
    /**
     * 已取消
     * 机审没通过、人审没通过、用户主动取消后的状态
     */
    Canceled(Arrays.asList(
            "user_canceled",
            "order_payment_overtime_canceled",
            "artificial_credit_check_unpass_canceled")
    ),
    /**
     * 退货中
     */
    ReturningGoods("return_goods"),
    /**
     * 维修中
     */
    Repiring("repairing"),
    /**
     * 换货中
     */
    Exchanging("exchange_goods"),
    /**
     * 续租待支付
     */
    PendingReletPay("pending_relet_pay"),
    /**
     * 等待续租开始
     */
    PendingReletStart("pending_relet_start"),
    /**
     * 买断待支付
     */
    PendingBuyoutPay("pending_buyout_pay"),
    /**
     * 超时支付取消
     */
    PaymentOverTimeCanceled("payment_over_time_canceled"),
    /**
     * 买断完成
     */
    BuyoutFinished("buyout_finished"),
    /**
     * 拒绝买断
     */
    NotBuyout("not_buyout"),
    /**
     * 待审核
     */
    Check("check"),
    /**
     * 待签署
     */
    pendingSign("pending_sign"),
    /**
     * 同意买断
     */
    Buyout("buyout"),
    /**
     * 用户取消
     */
    UserCanceled("user_canceled"),
    /**
     * 坏账已完成
     */
    BadDebtFinished("bad_debt_finished"),
    /**
     * 坏账租转售
     */
    BadDebtRunning("bad_debt_running"),
    /**
     * 已完成
     * 租赁完成状态
     */
    Completed("lease_finished");

    private String value = "";
    private List<String> values = null;

    private OrderStatus(String value) {
        this.value = value;
    }

    private OrderStatus(List<String> values) {
        this.values = values;
    }

    public String getValue() {
        return this.value;
    }

    public List<String> getValues() {
        if (this.values == null) {
            this.values = new ArrayList<String>();
        }
        return this.values;
    }
}
