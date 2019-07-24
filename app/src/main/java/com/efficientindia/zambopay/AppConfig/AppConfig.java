package com.efficientindia.zambopay.AppConfig;
/**
 * Created by aftab on 4/28/2018.
 */
public class AppConfig {
    private static final String CONSTANT="https://www.zambo.in/mobileapi/";
    private static final String TRANS_CONSTANT="https://www.zambo.in/dmtmrupay/";
    private static final String DMT_CONSTANT="https://www.zambo.in/dmtmahagram/";
    private static final String MRU_PAY_DMT="https://www.zambo.in/mrupaydmt/";

    /*==============================================================================================*/

    static final String LOGIN=CONSTANT+"login";
    static final String REGISTER=CONSTANT+"registration";
    public static final String PROFILE_DATA=CONSTANT+"profile";
    static final String GET_STATES=CONSTANT+"states";
    static final String UPDATE_PROFILE=CONSTANT+"profileUpdate";
    public static final String RECHARGE_PROCESS=CONSTANT+"rechargeProcess";
    public static final String BILL_PAYMENT_PROCESS=CONSTANT+"billPaymentProcess";
    public static final String AEPS_OUTLET=CONSTANT+"aepsOutlet";
    public static final String GET_SENDER_INFO=TRANS_CONSTANT+"getSenderInfo";
    public static final String CREATE_SENDER=TRANS_CONSTANT+"createSender";
    public static final String RESEND_OTP=TRANS_CONSTANT+"resendOtp";
    public static final String VERIFY_SENDER=TRANS_CONSTANT+"verifySender";
    public static final String BENEFICIARY_LIST=TRANS_CONSTANT+"getBeniList";
    public static final String ADD_BENEFICIARY=TRANS_CONSTANT+"addBeneficiary";
    public static final String VERIFY_BENEFICIARY=TRANS_CONSTANT+"verifyBeneficiary";
    public static final String DELETE_BENEFICIARY=TRANS_CONSTANT+"deleteBeneficiary";
    public static final String TRANSFER=TRANS_CONSTANT+"sendMoney";
    public static final String GET_SERVICE=CONSTANT+"activeServices";
    public static final String ACTIVE_SERVICE=CONSTANT+"activeServices1";
    public static final String PAYMENT_TRANSACTION = CONSTANT + "paymentTransaction";
    public static final String AEPS_2_SERVICE = CONSTANT+"aeps2Outlet";
    public static final String MICRO_ATM=CONSTANT+"aeps2Outlet";
    public static final String ROUND_PAY_OPERATOR=CONSTANT+"getRoundpayOperatorsList";
    public static final String GET_CROWNDFINCH_OPERATOR=CONSTANT+"getCrowdfinchOperatorsList";
    public static final String RECHARGE_PROCESS3 = CONSTANT+"recharge3Process";
    public static final String RECHARGE2_PROCESS=CONSTANT+"recharge2Process";
    public static final String RECHARGE2_AIRTELPROCESS=CONSTANT+"recharge2AirtelProcess";
    public static final String ASSIGNED_SERVICE = CONSTANT+"assignedServices";

    /*==============================================================================================*/
    public static final String GET_SENDER_LIMIT= MRU_PAY_DMT+"getSenderLimit";
    public static final String GET_SENDER_INFO_DMT =MRU_PAY_DMT+"getBeneficiary" ;
    public static final String CREATE_SENDER_DMT = DMT_CONSTANT+"createSender";
    public static final String RESEND_OTP_DMT = DMT_CONSTANT+"resendOtp";
    public static final String VERIFY_SENDER_DMT = DMT_CONSTANT+"verifyBeneficiary";
    public static final String BANK_LIST_DMT2 = DMT_CONSTANT + "bankList";
    public static final String ADD_BENEFICIARY_DMT = DMT_CONSTANT + "addBeneficiary";
    public static final String OTP_VERIFY_BENE = DMT_CONSTANT + "otpVerifyBeneficiary";
    public static final String TRANSFER_DMT = DMT_CONSTANT + "sendMoney";
    public static final String REDEEM_BALANCE_AEPS = CONSTANT+"redeemAeps";
    public static final String outletcheckstatus=MRU_PAY_DMT+"checkOutletStatus";


  //  public static final String BENEFICIARY_LIST_DMT = DMT_CONSTANT+"";
    /*==============================================================================================*/
    public static final String GET_OPERATOR_BBPS = CONSTANT+"getBbpsOperatorsList";
    public static final String GET_PARAMETERS_BBPS = CONSTANT+"getBbpsBillerParam";
    public static final String GET_BILLER_MODE_BBPS = CONSTANT +"getBbpsOperatorsBillerMode";
    public static final String FETCH_BILL_BBPS = CONSTANT+"billFetch";
    public static final String PAY_BILL_BBPS = CONSTANT+"bbpsBillPaymentProcess";
    public static final String PAY_BILL_AMOUNT_BBPS = CONSTANT+"bbpsMobilePaymentProcess";
    /*==============================================================================================*/
    public static final String GET_OPERATOR_LIST = CONSTANT+"getOperatorsList";
    public static final String RECHARGE_HISTORY = CONSTANT+"rechargeStatement";
    public static final String BILL_STATEMENT=CONSTANT+"billsStatement";
    public static final String WALLET_STATEMENT=CONSTANT+"accountStatement";

}
