package zz.com.test1.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by lenovo on 2017/4/5.
 */

public class AccountYingshouModel extends BaseResult {

    /**
     * ErrMsg :
     * Success : 1
     * DataL : [{"Day":"2017-04-04","Money":"46.05"}]
     * AlipayMoney : 0.02
     * CashMoney : 46.01
     * TotalMoney : 46.05
     * WxMoney : 0.02
     */
    @JSONField(name = "ErrMsg")
    private String ErrMsg;
    @JSONField(name = "Success")
    private int Success;
    @JSONField(name = "AlipayMoney")
    private String AlipayMoney;
    @JSONField(name = "CashMoney")
    private String CashMoney;
    @JSONField(name = "TotalMoney")
    private String TotalMoney;
    @JSONField(name = "WxMoney")
    private String WxMoney;
    /**
     * Day : 2017-04-04
     * Money : 46.05
     */
    @JSONField(name = "DataL")
    private List<DataLBean> DataL;

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int Success) {
        this.Success = Success;
    }

    public String getAlipayMoney() {
        return AlipayMoney;
    }

    public void setAlipayMoney(String AlipayMoney) {
        this.AlipayMoney = AlipayMoney;
    }

    public String getCashMoney() {
        return CashMoney;
    }

    public void setCashMoney(String CashMoney) {
        this.CashMoney = CashMoney;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String TotalMoney) {
        this.TotalMoney = TotalMoney;
    }

    public String getWxMoney() {
        return WxMoney;
    }

    public void setWxMoney(String WxMoney) {
        this.WxMoney = WxMoney;
    }

    public List<DataLBean> getDataL() {
        return DataL;
    }

    public void setDataL(List<DataLBean> DataL) {
        this.DataL = DataL;
    }

    @Override
    public String toString() {
        return "AccountYingshouModel{" +
                "ErrMsg='" + ErrMsg + '\'' +
                ", Success=" + Success +
                ", AlipayMoney='" + AlipayMoney + '\'' +
                ", CashMoney='" + CashMoney + '\'' +
                ", TotalMoney='" + TotalMoney + '\'' +
                ", WxMoney='" + WxMoney + '\'' +
                ", DataL=" + DataL +
                '}';
    }

    public static class DataLBean {
        @JSONField(name = "Day")
        private String Day;
        @JSONField(name = "Money")
        private String Money;

        public String getDay() {
            return Day;
        }

        public void setDay(String Day) {
            this.Day = Day;
        }

        public String getMoney() {
            return Money;
        }

        public void setMoney(String Money) {
            this.Money = Money;
        }

        @Override
        public String toString() {
            return "DataLBean{" +
                    "Day='" + Day + '\'' +
                    ", Money='" + Money + '\'' +
                    '}';
        }
    }
}
