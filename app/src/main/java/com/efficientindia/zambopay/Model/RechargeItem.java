package com.efficientindia.zambopay.Model;

public class RechargeItem {

    String id, userId,number,type,providerId, amount,chargedAmount,openingBalance,closingBalance,
            operatorId,transId,status,message,date,source;

    public void RechargeItem(String id,String userId,String number,String type,String providerId,String amount,
                         String chargedAmount,String openingBalance,String closingBalance,String operatorId,
                         String transId,String status,String message,String date,String source) {
        this.id = id;
        this.userId = userId;
        this.number = number;
        this.type=type;
        this.providerId=providerId;
        this.amount =amount;
        this.chargedAmount=chargedAmount;
        this.openingBalance=openingBalance;
        this.closingBalance=closingBalance;
        this.operatorId = operatorId;
        this.transId=transId;
        this.status=status;
        this.message =message;
        this.date=date;
        this.source=source;
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

    public String getNumber(){
        return number;
    }
    public void setNumber(String number){
        this.number=number;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

    public String getProviderId(){
        return providerId;
    }
    public void setProviderId(String providerId){
        this.providerId=providerId;
    }

    public String getAmount(){
        return amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getChargedAmount(){
        return chargedAmount;
    }
    public void setChargedAmount(String chargedAmount){
        this.chargedAmount=chargedAmount;
    }

    public String getOpeningBalance(){
        return openingBalance;
    }
    public void setOpeningBalance(String openingBalance){
        this.openingBalance=openingBalance;
    }

    public String getClosingBalance(){
        return closingBalance;
    }
    public void setClosingBalance(String closingBalance){
        this.closingBalance=closingBalance;
    }

    public String getOperatorId(){
        return operatorId;
    }
    public void setOperatorId(String operatorId){
        this.operatorId=operatorId;
    }

    public String getTransId(){
        return transId;
    }
    public void setTransId(String transId){
        this.transId=transId;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }

    public String getSource(){
        return source;
    }
    public void setSource(String source){
        this.source=source;
    }

}
