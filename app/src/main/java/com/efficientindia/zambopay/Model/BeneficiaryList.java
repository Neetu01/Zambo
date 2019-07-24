package com.efficientindia.zambopay.Model;

public class BeneficiaryList {

    String RECIPIENTID, NAME,ACCOUNT,BANK,IFSC, RESPONSESTATUS,SenderMobileNo;

    public void BeneficiaryList(String RECIPIENTID,String NAME,String ACCOUNT,String BANK,String IFSC,String RESPONSESTATUS,String SenderMobileNo) {
        this.RECIPIENTID = RECIPIENTID;
        this.NAME = NAME;
        this.ACCOUNT = ACCOUNT;
        this.BANK=BANK;
        this.IFSC=IFSC;
        this.RESPONSESTATUS =RESPONSESTATUS;
        this.SenderMobileNo=SenderMobileNo;
    }

    public String getRECIPIENTID(){
        return RECIPIENTID;
    }
    public void setRECIPIENTID(String RECIPIENTID){
        this.RECIPIENTID=RECIPIENTID;
    }

    public String getNAME(){
        return NAME;
    }
    public void setNAME(String NAME){
        this.NAME=NAME;
    }

    public String getACCOUNT(){
        return ACCOUNT;
    }
    public void setACCOUNT(String ACCOUNT){
        this.ACCOUNT=ACCOUNT;
    }

    public String getBANK(){
        return BANK;
    }
    public void setBANK(String BANK){
        this.BANK=BANK;
    }

    public String getIFSC(){
        return IFSC;
    }
    public void setIFSC(String IFSC){
        this.IFSC=IFSC;
    }

    public String getRESPONSESTATUS() {
        return RESPONSESTATUS;
    }

    public void setRESPONSESTATUS(String RESPONSESTATUS) {
        this.RESPONSESTATUS = RESPONSESTATUS;
    }

    public String getSenderMobileNo() {
        return SenderMobileNo;
    }

    public void setSenderMobileNo(String senderMobileNo) {
        this.SenderMobileNo = senderMobileNo;
    }
}
