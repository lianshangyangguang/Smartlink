package com.gwell.view.smalllink;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gwell.view.library.UDPBroadcastHelper;
import com.jwkj.smartlinkdemo.DeviceInfo;
import com.jwkj.smartlinkdemo.SmartLink;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    TextView tx_wifiName, tx_receive;
    Button bt_send_wifi,bt_send_msg, bt_stop;
    EditText et_pwd,et_msg;
    String pwd = "";
    String msg = "";
//    public UDPBroadcastHelper mHelper;
    private SmartLink smartLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initUI();
        //监听UDP广播
//        mHelper = new UDPBroadcastHelper(context);
//        listen();
        smartLink = new SmartLink(this, new SmartLink.OnDealSsid() {
            @Override
            public void onNoSsid() {
                tx_wifiName.setText("请先连接wifi");
            }

            @Override
            public void onCurrentSsid(String ssid) {
                tx_wifiName.setText(ssid);
            }

        });
//        mHelper.receive(9988, handle);
    }

    public void initUI() {
        tx_wifiName = (TextView) findViewById(R.id.tx_wifiName);
        tx_receive = (TextView) findViewById(R.id.tx_receive);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_msg = (EditText) findViewById(R.id.et_msg);
        bt_send_wifi = (Button) findViewById(R.id.bt_send_wifi);
        bt_send_msg = (Button) findViewById(R.id.bt_send_msg);
        bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_send_wifi.setOnClickListener(this);
        bt_send_msg.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_wifi:
                pwd = et_pwd.getText().toString().trim();
                smartLink.sendWifi(pwd, handle);
                tx_receive.append("开始发包......\n");
                break;
            case R.id.bt_stop:
                smartLink.stopSendWifi();
                tx_receive.append("停止发包\n");
                break;
            case R.id.bt_send_msg:
                msg = et_msg.getText().toString().trim();
                smartLink.send("196.128.1.3", 9988, msg, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case UDPBroadcastHelper.SEND_MSG_ERROR:
                                Log.e("zxy", "HANDLER_MESSAGE_BIND_ERROR");
                                break;
                            case UDPBroadcastHelper.SEND_MSG_SUCCESS:
                                Log.e("zxy", "HANDLER_MESSAGE_BIND_ERROR");
                                break;
                        }
                    }
                });
                break;

        }
    }
        Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case UDPBroadcastHelper.RECEIVE_MSG_ERROR:
                        Log.e("my", "HANDLER_MESSAGE_BIND_ERROR");
                        break;
                    case UDPBroadcastHelper.RECEIVE_MSG_SUCCESS:
                        Bundle bundle = msg.getData();
                        com.gwell.view.library.ReceiveDatagramPacket receiveData = (com.gwell.view.library.ReceiveDatagramPacket) bundle.getSerializable("receiveData");
                        parseData(receiveData);
                        break;
                }
            }
        };

    private void parseData(com.gwell.view.library.ReceiveDatagramPacket receiveData) {
        DeviceInfo deviceInfo = DeviceInfo.parseReceiveData(receiveData);
        String info = deviceInfo.toString();
        if (Integer.parseInt(deviceInfo.getFrag()) == 0) {
            info = info + "无密码";
        } else {
            info = info + "有密码";
        }
        tx_receive.append(info + "\n\n");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        smartLink.stop();
//        if (mHelper != null) {
//            mHelper.StopListen();
//        }
    }
}
