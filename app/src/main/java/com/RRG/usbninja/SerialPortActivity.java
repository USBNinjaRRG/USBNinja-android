package com.RRG.usbninja;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.RRG.usbninja.app.MainApplication;
import com.RRG.usbninja.ble.ConvertData;
import com.RRG.usbninja.ble.HolloBluetooth;
import com.RRG.usbninja.config.ConfigInfo;
import com.wise.wisekit.activity.BaseActivity;
import com.wise.wisekit.dialog.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

public class SerialPortActivity extends BaseActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private HolloBluetooth mble;

    private String mDeviceName;
    private String mDeviceAddress;

    private TextView mNum1Btn;
    private TextView mNum2Btn;
    private TextView mNum3Btn;
    private TextView mNum4Btn;
    private TextView mNum5Btn;
    private TextView mNum6Btn;
    private TextView mNum7Btn;
    private TextView mNum8Btn;
    private TextView mNum9Btn;

    private Button mEditBtn;

    private ButtonModel mNum1Info;
    private ButtonModel mNum2Info;
    private ButtonModel mNum3Info;
    private ButtonModel mNum4Info;
    private ButtonModel mNum5Info;
    private ButtonModel mNum6Info;
    private ButtonModel mNum7Info;
    private ButtonModel mNum8Info;
    private ButtonModel mNum9Info;
    private ImageView a;
    private LoadingDialog loadingDialog = null;

    //is Editing
    private boolean bEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_serial_port, topContentView);

        initView();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        mble = HolloBluetooth.getInstance(getApplicationContext());	//get BLEContext

        enableButton(false);

        connectBle();

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateButton();
    }

    private void initView() {
        a=findViewById(R.id.imgproc);
        topLeftBtn.setVisibility(View.VISIBLE);
        setTitle("BLE Control Panel");
        topRightBtn.setVisibility(View.VISIBLE);
        setRightText("Log");

        topRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(SerialPortActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });

        LoadingDialog.Builder loadBuilder=new LoadingDialog.Builder(this)
                .setCancelable(true)
                .setShowMessage(false)
                .setCancelOutside(false)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        SerialPortActivity.this.finish();
                    }
                });
        loadingDialog = loadBuilder.create();

        initButtons();

        initButtonListener();
    }

    //初始化按钮
    void initButtons() {

        mEditBtn = findViewById(R.id.edit);

        mNum1Btn = findViewById(R.id.num1);
        mNum2Btn = findViewById(R.id.num2);
        mNum3Btn = findViewById(R.id.num3);
        mNum4Btn = findViewById(R.id.num4);
        mNum5Btn = findViewById(R.id.num5);
        mNum6Btn = findViewById(R.id.num6);
        mNum7Btn = findViewById(R.id.num7);
        mNum8Btn = findViewById(R.id.num8);
        mNum9Btn = findViewById(R.id.num9);

//        updateButton();
    }

    void updateButton() {

        mNum1Info = new ButtonModel(ConfigInfo.getInstance().getNum1());
        mNum2Info = new ButtonModel(ConfigInfo.getInstance().getNum2());
        mNum3Info = new ButtonModel(ConfigInfo.getInstance().getNum3());
        mNum4Info = new ButtonModel(ConfigInfo.getInstance().getNum4());
        mNum5Info = new ButtonModel(ConfigInfo.getInstance().getNum5());
        mNum6Info = new ButtonModel(ConfigInfo.getInstance().getNum6());
        mNum7Info = new ButtonModel(ConfigInfo.getInstance().getNum7());
        mNum8Info = new ButtonModel(ConfigInfo.getInstance().getNum8());
        mNum9Info = new ButtonModel(ConfigInfo.getInstance().getNum9());

        mNum1Btn.setText(mNum1Info.getName());
        mNum2Btn.setText(mNum2Info.getName());
        mNum3Btn.setText(mNum3Info.getName());
        mNum4Btn.setText(mNum4Info.getName());
        mNum5Btn.setText(mNum5Info.getName());
        mNum6Btn.setText(mNum6Info.getName());
        mNum7Btn.setText(mNum7Info.getName());
        mNum8Btn.setText(mNum8Info.getName());
        mNum9Btn.setText(mNum9Info.getName());

    }

    void initButtonListener() {

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bEdit = !bEdit;
                if (bEdit) {
                    Drawable switchOn = getResources().getDrawable(R.mipmap.switch_on);
                    switchOn.setBounds(0, 0, switchOn.getMinimumWidth(), switchOn.getMinimumHeight());
                    mEditBtn.setCompoundDrawables(null, null, switchOn, null);
                }
                else {
                    Drawable switchOff = getResources().getDrawable(R.mipmap.switch_off);
                    switchOff.setBounds(0, 0, switchOff.getMinimumWidth(), switchOff.getMinimumHeight());
                    mEditBtn.setCompoundDrawables(null, null, switchOff, null);
                }
            }
        });

        mNum1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num1");
                    startActivity(intent);
                }
            }
        });

        mNum1Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum1Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum1Info, false);
                }

                return false;
            }
        });

        mNum2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num2");
                    startActivity(intent);
                }
            }
        });

        mNum2Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum2Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum2Info, false);
                }

                return false;
            }
        });

        mNum3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num3");
                    startActivity(intent);
                }
            }
        });

        mNum3Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum3Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum3Info, false);
                }

                return false;
            }
        });

        mNum4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num4");
                    startActivity(intent);
                }
            }
        });

        mNum4Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum4Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum4Info, false);
                }

                return false;
            }
        });

        mNum5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num5");
                    startActivity(intent);
                }
            }
        });

        mNum5Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum5Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum5Info, false);
                }

                return false;
            }
        });

        mNum6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num6");
                    startActivity(intent);
                }
            }
        });

        mNum6Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum6Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum6Info, false);
                }

                return false;
            }
        });

        mNum7Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num7");
                    startActivity(intent);
                }
            }
        });

        mNum7Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum7Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum7Info, false);
                }

                return false;
            }
        });

        mNum8Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num8");
                    startActivity(intent);
                }
            }
        });

        mNum8Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum8Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum8Info, false);
                }

                return false;
            }
        });

        mNum9Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bEdit) {
                    final Intent intent = new Intent(SerialPortActivity.this, SetButtonActivity.class);
                    intent.putExtra(SetButtonActivity.EXTRAS_BUTTON_KEY, "num9");
                    startActivity(intent);
                }
            }
        });

        mNum9Btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bEdit) return false;

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendInfo(mNum9Info, true);

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendInfo(mNum9Info, false);
                }

                return false;
            }
        });
    }

    /*
    * Send Message，
    * btnModel Button Information
    * bDown is Pushed
    * */
    void sendInfo(ButtonModel btnModel, boolean bDown) {

        byte[] tempData = null;
        if (bDown && !btnModel.getTouchDown().isEmpty()) {
            tempData = ConvertData.hexStringToBytes(btnModel.getTouchDown());
        }
        else  if (!bDown && !btnModel.getTouchUp().isEmpty()) {
            tempData = ConvertData.hexStringToBytes(btnModel.getTouchUp());
        }

        if (tempData == null) return;


        final byte[] sendData = tempData;
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                sendBleData(sendData);

            }
        }).start();

    }

    synchronized void sendBleData(byte[] data) {

        if(!mble.sendData(data)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SerialPortActivity.this, "Send Failed！", Toast.LENGTH_SHORT).show();
                    LogInfo info = new LogInfo();
                    info.setType("error");
                    info.setContent("Send Failed");
                    MainApplication.instance.logList.add(info);
                }
            });

        }

        LogInfo info = new LogInfo();
        info.setType("send");
        info.setContent(ConvertData.bytesToHexString(data, false));
        MainApplication.instance.logList.add(info);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    //Enable Buttons
    void enableButton(boolean enable) {

        mEditBtn.setEnabled(enable);

        mNum1Btn.setEnabled(enable);
        mNum2Btn.setEnabled(enable);
        mNum3Btn.setEnabled(enable);
        mNum4Btn.setEnabled(enable);
        mNum5Btn.setEnabled(enable);
        mNum6Btn.setEnabled(enable);
        mNum7Btn.setEnabled(enable);
        mNum8Btn.setEnabled(enable);
        mNum9Btn.setEnabled(enable);

    }

    //Connect Bluetooth
    void connectBle() {


        loadingDialog.show();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int i;
                for (i = 0; i < 5; i++)
                {
                    if(mble.connectDevice(mDeviceAddress,bleCallBack))	//Connect Bluetooth Device
                        break;
                }
                if(i == 5)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.hide();
                            Toast.makeText(SerialPortActivity.this,"The connection fails", Toast.LENGTH_SHORT).show();
                            LogInfo info = new LogInfo();
                            info.setType("error");
                            info.setContent("The connection fails");
                            MainApplication.instance.logList.add(info);
                        }
                    });

                    return ;
                }

                try {
                    Thread.sleep(200,0);//200ms
                }
                catch (Exception e){

                }


                if(!mble.wakeUpBle())
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.hide();
                            Toast.makeText(SerialPortActivity.this,"The connection fails",Toast.LENGTH_SHORT).show();
                            LogInfo info = new LogInfo();
                            info.setType("error");
                            info.setContent("Failure to open notification");
                            MainApplication.instance.logList.add(info);
                        }
                    });
                }
                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            LogInfo info = new LogInfo();
                            info.setType("");
                            info.setContent("Successful bluetooth connection");
                            MainApplication.instance.logList.add(info);
                        }
                    });

                    String password = ConfigInfo.getInstance().getPassword();
                    if (password != null && password.length() != 0) {

                        LogInfo info = new LogInfo();
                        info.setType("");
                        info.setContent("Sending the password");
                        MainApplication.instance.logList.add(info);
                        sendBleData(ConvertData.unicodeToBytes(password));

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            loadingDialog.hide();
                            enableButton(true);
                        }
                    });

                }

            }
        }).start();
    }

    HolloBluetooth.OnHolloBluetoothCallBack bleCallBack = new HolloBluetooth.OnHolloBluetoothCallBack()
    {

        @Override
        public void OnHolloBluetoothState(int state)
        {
            if(state == HolloBluetooth.HOLLO_BLE_DISCONNECTED)
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SerialPortActivity.this, "Bluetooth disconnected", Toast.LENGTH_SHORT).show();
                        LogInfo info = new LogInfo();
                        info.setType("");
                        info.setContent("Bluetooth disconnected");
                        MainApplication.instance.logList.add(info);
                        finish();
                    }
                });
            }
        }

        @Override
        public void OnReceiveData(byte[] recvData)
        {
            final int sleep_time=100;
            LogInfo info = new LogInfo();
            info.setType("receive");
            info.setContent(ConvertData.bytesToHexString(recvData, false));
            MainApplication.instance.logList.add(info);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    a.setImageResource(R.drawable.ic_red);
                }
            });
            try {
                Thread.sleep(sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    a.setImageResource(R.drawable.ic_black);
                }
            });
            try {
                Thread.sleep(sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mble.disconnectDevice();
        Log.d(TAG, "destroy");
        mble.disconnectLocalDevice();
    }
}