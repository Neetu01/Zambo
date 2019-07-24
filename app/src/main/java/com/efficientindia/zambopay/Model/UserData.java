package com.efficientindia.zambopay.Model;

/**
 * Created by aftab on 4/28/2018.
 */

public class UserData {

    private String uid,userId,name,company,email,mobile,uType,kycStatus,uStatus,outletStatus,addressLine1,addressLine2,city,
            state,pin,uDistributor,photo,adharCard,panCard,aadharImage,panImage,walletStatus,wallet;

    public UserData(String uid,String userId,String name,String company,String email,String mobile,String uType,
                    String kycStatus,String uStatus,String outletStatus,String addressLine1,String addressLine2,String city,
                    String state,String pin,String uDistributor,String photo,String adharCard,String panCard,String aadharImage,
                    String panImage,String walletStatus,String wallet) {

        this.uid = uid;
        this.userId=userId;
        this.name=name;
        this.company=company;
        this.email=email;
        this.mobile=mobile;
        this.uType=uType;
        this.kycStatus=kycStatus;
        this.uStatus=uStatus;
        this.outletStatus=outletStatus;
        this.addressLine1=addressLine1;
        this.addressLine2=addressLine2;
        this.city =city;
        this.state=state;
        this.pin=pin;
        this.uDistributor=uDistributor;
        this.photo=photo;
        this.adharCard=adharCard;
        this.panCard=panCard;
        this.aadharImage=aadharImage;
        this.panImage=panImage;
        this.walletStatus=walletStatus;
        this.wallet=wallet;
    }

    public UserData() {

    }

    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId=userId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public String getMobileUser(){
        return mobile;
    }
    public void setMobileUser(String mobile){
        this.mobile=mobile;
    }

    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid=uid;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getCompany(){
        return company;
    }
    public void setCompany(String company){
        this.company=company;
    }

    public String getMobile(){
        return mobile;
    }
    public void setMobile(String mobile){
        this.mobile=mobile;
    }

    public String getuType(){
        return uType;
    }
    public void setuType(String uType){
        this.uType=uType;
    }

    public String getKycStatus(){
        return kycStatus;
    }
    public void setKycStatus(String kycStatus){
        this.kycStatus=kycStatus;
    }

    public String getuStatus(){
        return uStatus;
    }
    public void setuStatus(String uStatus){
        this.uStatus=uStatus;
    }


    public String getOutletStatus(){
        return outletStatus;
    }
    public void setOutletStatus(String outletStatus){
        this.outletStatus=outletStatus;
    }

    public String getAddressLine1(){
        return addressLine1;
    }
    public void setAddressLine1(String addressLine1){
        this.addressLine1=addressLine1;
    }

    public String getAddressLine2(){
        return addressLine2;
    }
    public void setAddressLine2(String addressLine2){
        this.addressLine2=addressLine2;
    }

    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city=city;
    }

    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state=state;
    }

    public String getPin(){
        return pin;
    }
    public void setPin(String pin){
        this.pin=pin;
    }

    public String getuDistributor(){
        return uDistributor;
    }
    public void setuDistributor(String uDistributor){
        this.uDistributor=uDistributor;
    }

    public String getPhoto(){
        return photo;
    }
    public void setPhoto(String photo){
        this.photo=photo;
    }

    public String getAdharCard(){
        return adharCard;
    }
    public void setAdharCard(String adharCard){
        this.adharCard=adharCard;
    }

    public String getPanCard(){
        return panCard;
    }
    public void setPanCard(String panCard){
        this.panCard=panCard;
    }

    public String getAadharImage(){
        return aadharImage;
    }
    public void setAadharImage(String aadharImage){
        this.aadharImage=aadharImage;
    }

    public String getPanImage(){
        return panImage;
    }
    public void setPanImage(String panImage){
        this.panImage=panImage;
    }

    public String getWalletStatus(){
        return walletStatus;
    }
    public void setWalletStatus(String walletStatus){
        this.walletStatus=walletStatus;
    }

    public String getWallet(){
        return wallet;
    }
    public void setWallet(String wallet){
        this.wallet=wallet;
    }

}
