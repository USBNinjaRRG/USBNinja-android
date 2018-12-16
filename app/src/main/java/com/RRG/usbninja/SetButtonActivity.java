package com.RRG.usbninja;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.RRG.usbninja.app.MainApplication;
import com.RRG.usbninja.ble.ConvertData;
import com.wise.wisekit.activity.BaseActivity;
import com.wise.wisekit.utils.SPUtils;

public class SetButtonActivity extends BaseActivity {

    public static final String EXTRAS_BUTTON_KEY = "button_key";

    private EditText mNameEditText;
    private EditText mTouchUpEditText;
    private EditText mTouchDownEditText;

    private String buttonKey;
    private ButtonModel btnModel;
    private Button mEditBtn;

    //is HEX Editing
    private boolean bHexEdit = true;

    KeyListener defaultKeyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_set_button, topContentView);

        final Intent intent = getIntent();
        buttonKey = intent.getStringExtra(EXTRAS_BUTTON_KEY);

        String buttonStr = (String) SPUtils.get(MainApplication.getAppContext(), buttonKey, "");
        btnModel = new ButtonModel(buttonStr);

        initView();

    }

    private void initView() {

        topLeftBtn.setVisibility(View.VISIBLE);
        topRightBtn.setVisibility(View.VISIBLE);

        setTitle("Set");
        setRightText("Done");

        mNameEditText = findViewById(R.id.name_edit);
        mTouchDownEditText = findViewById(R.id.touch_down_edit);
        mTouchUpEditText = findViewById(R.id.touch_up_edit);

        mEditBtn = findViewById(R.id.edit);

        mNameEditText.setText(btnModel.getName());
        mTouchUpEditText.setText(btnModel.getTouchUp());
        mTouchDownEditText.setText(btnModel.getTouchDown());

        defaultKeyListener = mTouchUpEditText.getKeyListener();

        mTouchUpEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefABCDEF"));
        mTouchDownEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefABCDEF"));

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bHexEdit = !bHexEdit;

                if (bHexEdit) {
                    Drawable switchOn = getResources().getDrawable(R.mipmap.switch_on);
                    switchOn.setBounds(0, 0, switchOn.getMinimumWidth(), switchOn.getMinimumHeight());
                    mEditBtn.setCompoundDrawables(null, null, switchOn, null);
                    mTouchUpEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefABCDEF"));
                    mTouchDownEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefABCDEF"));
                    mTouchDownEditText.setText("");
                    mTouchUpEditText.setText("");
                }
                else {
                    Drawable switchOff = getResources().getDrawable(R.mipmap.switch_off);
                    switchOff.setBounds(0, 0, switchOff.getMinimumWidth(), switchOff.getMinimumHeight());
                    mEditBtn.setCompoundDrawables(null, null, switchOff, null);
                    mTouchUpEditText.setKeyListener(defaultKeyListener);
                    mTouchDownEditText.setKeyListener(defaultKeyListener);
                }

            }
        });

        topRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //could`t divied by 2
                if (bHexEdit && mTouchDownEditText.getText().toString().length()%2 != 0) {
                    Toast.makeText(SetButtonActivity.this, "Press the command length to be an integer multiple of 2",Toast.LENGTH_SHORT).show();
                    return;
                }
                //could`t divied by 2
                if (bHexEdit && mTouchUpEditText.getText().toString().length()%2 != 0) {
                    Toast.makeText(SetButtonActivity.this, "The release command must be an integer multiple of 2",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mNameEditText.getText().toString().isEmpty()) {
                    btnModel.setName(mNameEditText.getText().toString());
                }

                if (!mTouchDownEditText.getText().toString().isEmpty()) {
                    if (bHexEdit) {
                        btnModel.setTouchDown(mTouchDownEditText.getText().toString());
                    }
                    else {
                        btnModel.setTouchDown(ConvertData.bytesToHexString(ConvertData.unicodeToBytes(mTouchDownEditText.getText().toString()),false));
                    }

                }

                if (!mTouchUpEditText.getText().toString().isEmpty()) {
                    if (bHexEdit) {
                        btnModel.setTouchUp(mTouchUpEditText.getText().toString());
                    }
                    else {
                        btnModel.setTouchUp(ConvertData.bytesToHexString(ConvertData.unicodeToBytes(mTouchUpEditText.getText().toString()),false));
                    }
                }

                SPUtils.put(MainApplication.getAppContext(), buttonKey, btnModel.toJsonObjectStr());

                finish();
            }
        });
    }
}
