package com.efficientindia.zambopay.Model;

public class WalletHistory {

    String id, userId,transactionId,amount,type, description,remarks,date,status,oldBalance,
            newBalance;

    public void WalletHistory(String id,String userId,String transactionId,String amount,String type,String description,
                             String remarks,String date,String status,String oldBalance,
                             String newBalance) {
        this.id = id;
        this.userId = userId;
        this.transactionId = transactionId;
        this.amount=amount;
        this.type=type;
        this.description =description;
        this.remarks=remarks;
        this.date=date;
        this.status=status;
        this.oldBalance=oldBalance;
        this.newBalance=newBalance;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId=userId;
    }

    public String getTransactionId(){
        return transactionId;
    }
    public void setTransactionId(String transactionId){
        this.transactionId=transactionId;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

    public String getAmount(){
        return amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }

    public String getRemarks(){
        return remarks;
    }
    public void setRemarks(String remarks){
        this.remarks=remarks;
    }

    public String getOldBalance(){
        return oldBalance;
    }
    public void setOldBalance(String oldBalance){
        this.oldBalance=oldBalance;
    }

    public String getNewBalance(){
        return newBalance;
    }
    public void setNewBalance(String newBalance){
        this.newBalance=newBalance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

