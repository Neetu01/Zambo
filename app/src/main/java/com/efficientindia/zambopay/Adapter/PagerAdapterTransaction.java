package com.efficientindia.zambopay.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.efficientindia.zambopay.Fragment.FragmentWalletHistory;

public class PagerAdapterTransaction extends FragmentStatePagerAdapter {
    int tabCount;

    //Constructor to the class
    public PagerAdapterTransaction(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
           // case 0:
           /*     return new FragmentRechargeHistory();
            case 1:
                return new FragmentBillHistory();*/
            case 0:
                return new FragmentWalletHistory();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //if (position==0){
            return "Wallet Transaction";
        /*}else if (position==1){
            return "Bill Payment History";
        } else*/

    }
}
