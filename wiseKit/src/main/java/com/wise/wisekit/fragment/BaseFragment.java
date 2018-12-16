package com.wise.wisekit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wise.wisekit.R;

public class BaseFragment extends Fragment {

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
    protected View separateLineView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topContentView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_base, null);
        topBar = (ViewGroup) topContentView.findViewById(R.id.topbar);
        topTitleTxt = (TextView) topContentView.findViewById(R.id.base_activity_title);
        topLeftBtn = (ImageView) topContentView.findViewById(R.id.left_btn);
        topRightBtn = (ImageView) topContentView.findViewById(R.id.right_btn);
        topTitelImage = (ImageView) topContentView.findViewById(R.id.title_image);
        leftTitleTxt = (TextView) topContentView.findViewById(R.id.left_txt);
        rightTitleTxt = (TextView) topContentView.findViewById(R.id.right_txt);
        baseRoot = (LinearLayout)topContentView.findViewById(R.id.act_base_root);
        separateLineView = topContentView.findViewById(R.id.separate_line);

        topTitleTxt.setVisibility(View.GONE);
        topRightBtn.setVisibility(View.GONE);
        leftTitleTxt.setVisibility(View.GONE);
        rightTitleTxt.setVisibility(View.GONE);
        topLeftBtn.setVisibility(View.GONE);
        topTitelImage.setVisibility(View.GONE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != topContentView) {
            ((ViewGroup) topContentView.getParent()).removeView(topContentView);
            return topContentView;
        }
        return topContentView;
    }

    public void willShowFragment(){

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
        if (title.length() > 12) {
            title = title.substring(0, 11) + "...";
        }
        topTitleTxt.setText(title);
        topTitleTxt.setVisibility(View.VISIBLE);
    }

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
