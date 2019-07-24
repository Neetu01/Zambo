package com.efficientindia.zambopay.Model;

public class BeneListDmt {

    String id, bankid,bankname,beneaccno,benemobile, benename,ifsc,status;

    public void BeneListDmt(String id,String bankid,String bankname,String beneaccno,String benemobile,
                            String benename,String ifsc,String status) {
        this.id = id;
        this.bankid = bankid;
        this.bankname = bankname;
        this.beneaccno=beneaccno;
        this.benemobile=benemobile;
        this.benename =benename;
        this.ifsc=ifsc;
        this.status=status;
    }

    public String getId() {
        return id;
    }

    public String getBankid() {
        return bankid;
    }

    public String getBankname() {
        return bankname;
    }

    public String getStatus() {
        return status;
    }

    public String getBeneaccno() {
        return beneaccno;
    }

    public String getBenemobile() {
        return benemobile;
    }

    public String getBenename() {
        return benename;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public void setBeneaccno(String beneaccno) {
        this.beneaccno = beneaccno;
    }

    public void setBenemobile(String benemobile) {
        this.benemobile = benemobile;
    }

    public void setBenename(String benename) {
        this.benename = benename;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
}
