package com.efficientindia.zambopay.Activity;

import android.app.Activity;

import android.view.View;


import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.efficientindia.zambopay.R;

public class Enddrawertoggle implements DrawerLayout.DrawerListener {

private DrawerLayout drawerLayout;
private DrawerArrowDrawable arrowDrawable;
private AppCompatImageButton toggleButton;
private String openDrawerContentDesc;
private String closeDrawerContentDesc;

public Enddrawertoggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar,
                       String openDrawerContentDescRes, String closeDrawerContentDescRes) {

    this.drawerLayout = drawerLayout;
    this.openDrawerContentDesc =openDrawerContentDescRes;
    this.closeDrawerContentDesc =closeDrawerContentDescRes;

    arrowDrawable = new DrawerArrowDrawable(toolbar.getContext());
    arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_END);

    toggleButton = new AppCompatImageButton(toolbar.getContext(), null, R.attr.toolbarNavigationButtonStyle);
    toolbar.addView(toggleButton, new Toolbar.LayoutParams(GravityCompat.END));
    toggleButton.setImageDrawable(arrowDrawable);
    toggleButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggle();
        }
    });

}

    public void syncState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            setPosition(1f);
        }
        else {
            setPosition(0f);
        }
    }

    public void toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    public void setPosition(float position) {
        if (position == 1f) {
            arrowDrawable.setVerticalMirror(true);
            toggleButton.setContentDescription(closeDrawerContentDesc);
        }
        else if (position == 0f) {
            arrowDrawable.setVerticalMirror(false);
            toggleButton.setContentDescription(openDrawerContentDesc);
        }
        arrowDrawable.setProgress(position);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        setPosition(Math.min(1f, Math.max(0, slideOffset)));
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        setPosition(1f);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setPosition(0f);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

}
