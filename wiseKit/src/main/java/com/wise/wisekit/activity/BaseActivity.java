package com.wise.wisekit.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wise.wisekit.R;
import com.wise.wisekit.widget.SearchEditText;

public class BaseActivity extends Activity {

    public String TAG =  this.getClass().getCanonicalName();

    protected ImageView topLeftBtn;
    protected ImageView topRightBtn;
    protected ImageView topTitelImage;
    protected TextView topTitleTxt;
    protected TextView leftTitleTxt;
    protected TextView rightTitleTxt;
    protected ViewGroup topBar;
    protected ViewGroup topContentView;
    protected LinearLayout baseRoot;
    protected LinearLayout topTitleLayout;

    protected SearchEditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorNavBackground));
        }


        topContentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_base, null);
        topBar = (ViewGroup) topContentView.findViewById(R.id.topbar);
        topTitleTxt = (TextView) topContentView.findViewById(R.id.base_activity_title);
        topTitleLayout = (LinearLayout)topContentView.findViewById(R.id.base_activity_title_layout);
        topLeftBtn = (ImageView) topContentView.findViewById(R.id.left_btn);
        topRightBtn = (ImageView) topContentView.findViewById(R.id.right_btn);
        topTitelImage = (ImageView) topContentView.findViewById(R.id.title_image);
        leftTitleTxt = (TextView) topContentView.findViewById(R.id.left_txt);
        rightTitleTxt = (TextView) topContentView.findViewById(R.id.right_txt);
        baseRoot = (LinearLayout)topContentView.findViewById(R.id.act_base_root);
        searchEditText = (SearchEditText)topContentView.findViewById(R.id.title_search);


        topTitleTxt.setVisibility(View.GONE);
        topRightBtn.setVisibility(View.GONE);
        leftTitleTxt.setVisibility(View.GONE);
        rightTitleTxt.setVisibility(View.GONE);
        topLeftBtn.setVisibility(View.GONE);
        topTitelImage.setVisibility(View.GONE);
        searchEditText.setVisibility(View.GONE);

        setContentView(topContentView);

        setLeftButton(R.mipmap.return_jiao);
        topLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void setLeftText(String text) {
        if (null == text) {
            return;
        }
        leftTitleTxt.setText(text);
        leftTitleTxt.setVisibility(View.VISIBLE);
    }

    protected void setRightText(String text) {
        if (null == text) {
            return;
        }
        rightTitleTxt.setText(text);
        rightTitleTxt.setVisibility(View.VISIBLE);
    }

    protected void setTitle(String title) {
        if (title == null) {
            return;
        }
        if (title.length() > 24) {
            title = title.substring(0, 23) + "...";
        }
        topTitleTxt.setText(title);
        topTitleTxt.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(int id) {
        String strTitle = getResources().getString(id);
        setTitle(strTitle);
    }

    public void setTitleImage(int resID) {
        if (resID <= 0) {
            return;
        }

        topTitelImage.setImageResource(resID);
        topTitelImage.setVisibility(View.VISIBLE);
    }

    protected void setLeftButton(int resID) {
        if (resID <= 0) {
            return;
        }

        topLeftBtn.setImageResource(resID);
        topLeftBtn.setVisibility(View.VISIBLE);
    }

    protected void setRightButton(int resID) {
        if (resID <= 0) {
            return;
        }

        topRightBtn.setImageResource(resID);
        topRightBtn.setVisibility(View.VISIBLE);
    }

    protected void setTopBar(int resID) {
        if (resID <= 0) {
            return;
        }
        topBar.setBackgroundResource(resID);
    }

}
