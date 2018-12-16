package com.RRG.usbninja;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.RRG.usbninja.config.ConfigInfo;
import com.wise.wisekit.activity.BaseActivity;

public class SetPasswordActivity extends BaseActivity {

    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_set_password, topContentView);

        initView();

    }

    private void initView() {

        topLeftBtn.setVisibility(View.VISIBLE);
        topRightBtn.setVisibility(View.VISIBLE);

        mPasswordEditText = findViewById(R.id.password);

        setTitle("Set Password");
        setRightText("Done");

        topRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Password
                if (mPasswordEditText.getText().toString().length() == 0) {
                    Toast.makeText(SetPasswordActivity.this, "Please enter the password for the connection",Toast.LENGTH_SHORT).show();
                    return;
                }

                ConfigInfo.getInstance().setPassword(mPasswordEditText.getText().toString());

                finish();
            }
        });
    }
}
