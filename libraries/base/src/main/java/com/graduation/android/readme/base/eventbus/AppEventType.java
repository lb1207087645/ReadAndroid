package com.graduation.android.readme.base.eventbus;

/**
 * EventBus的Event事件
 * Created by lin on 2017/10/31.
 */

public class AppEventType {

    /**
     * 登录成功
     */
    public static final int LOGIN_SUCCESS = 0X01;

    /**
     * 退出登录
     */
    public static final int LOGIN_OUT = 0X02;


    /**
     * 刷新患者列表
     */
    public static final int PATIENT_LIST = 0X03;


    /**
     * 切换到问诊室
     */
    public static final int SELECT_CONSULATION = 0X04;


    /**
     * 刷新问诊室
     */
    public static final int REFRESH_CONSULATION = 0X05;

    public static final int ADD_PRESCRPTION = 0X06;

    public static final int ADD_DRUG = 0X07;


    public static final int ADD_CHUFAN = 0X08;


    /**
     * 回退
     */
    public static final int BACK = 0X09;


    public static final int SEARCH_DOCTOR = 0Xa;

    /**
     * 支付成功回调
     */
    public static final int PAY_SUCCESS = 0Xb;


    /**
     * 支付取消回调
     */
    public static final int PAY_CANCEL = 0Xc;

    /**
     * 支付失败回调
     */
    public static final int PAY_FAIL = 0Xd;


    /**
     * 修改用户信息
     */
    public static final int EIDT_ACCOUNTINFO = 0Xe;

    /**
     * 有认证信息
     */
    public static final int HAS_APPROVE = 0Xf;


    /**
     * 下一步
     */
    public static final int STEP_ONE = 0X10;

    public static final int STEP_TWO = 0X11;


    /**
     * 切换到我的
     */
    public static final int SELECT_MINE = 0X12;


    public static final int STEP_TWO_SUBMIT = 0X13;

    public static final int DRUG_PRICE = 0X14;

    public static final int RESET_CHUFANG = 0X15;//撤销处方


    /**
     * 消息刷新
     */
    public static final int MESSAGE_REFRESH = 0X16;
    /**
     * 登录刷新会诊室
     */
    public static final int LOGIN_REFRESH_CONSULATION = 0X17;


    /**
     * 发送新消息监听
     */
    public static final int SEND_NEW_MESSAGE = 0X18;

    /**
     * 刷新发现页
     */
    public static final int MESSAGE_PAGE_DISCOVER_REFRESH = 0X19;


    /**
     * 刷新我的页面
     */
    public static final int MESSAGE_PAGE_MINE_REFRESH = 0X20;
    /**
     * 刷新问诊室
     */
    public static final int MESSAGE_PAGE_CONSULATION_MAIN_REFRESH = 0X21;

    /**
     * 有认证信息
     */
    public static final int HAS_APPROVE_STICKY = 0X22;


    public static final int HAS_APPROVE_STICKY1 = 0X23;
    /**
     * 处方模板列表
     */
    public static final int DURGLIST = 0X30;
    /**
     * 所有患者
     */
    public static final int ALLPATIENT = 0X32;
    /**
     * 我的患者
     */
    public static final int MyPATIENT = 0X33;
    /**
     * 未咨询患者
     */
    public static final int NOCONSULTATION = 0X34;
    /**
     * 绑定银行卡
     */
    public static final int BINDBANK = 0X35;
    /**
     * 解绑账号
     */
    public static final int UNBINDACCOUNT = 0X37;
    /**
     * 绑定银行卡
     */
    public static final int DOCTORHOMEPAGE = 0X36;


    /**
     * 传递的标示
     */
    public int type;
    /**
     * 传递的数据
     */
    public Object[] values;

    public AppEventType(int type, Object... value) {
        this.type = type;
        this.values = value;
    }

    public AppEventType(int type) {
        this.type = type;
    }

}
