package com.gwell.view.smalllink;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gwell.view.library.ReceiveDatagramPacket;
import com.gwell.view.library.UDPBroadcastHelper;
import com.jwkj.smartlinkdemo.DeviceInfo;
import com.jwkj.smartlinkdemo.SmartLink;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    TextView tx_wifiName, tx_receive;
    Button bt_send_wifi, bt_stop;
    EditText et_pwd;
    String pwd = "";
    private SmartLink smartLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initUI();
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
    }

    public void initUI() {
        tx_wifiName = (TextView) findViewById(R.id.tx_wifiName);
        tx_receive = (TextView) findViewById(R.id.tx_receive);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_send_wifi = (Button) findViewById(R.id.bt_send_wifi);
        bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_send_wifi.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_wifi:
                pwd = et_pwd.getText().toString().trim();
                smartLink.sendWifi(pwd, new UDPBroadcastHelper.OnReceive() {
                    @Override
                    public void onReceive(int state, Bundle bundle) {
                        if (state == UDPBroadcastHelper.RECEIVE_MSG_ERROR){
                            Log.e("zxy", "HANDLER_MESSAGE_BIND_ERROR");
                        }else if (state == UDPBroadcastHelper.RECEIVE_MSG_SUCCESS){
                            ReceiveDatagramPacket receiveData = (ReceiveDatagramPacket) bundle.getSerializable("receiveData");
                            parseData(receiveData);
                        }
                    }
                });
                tx_receive.append("开始发包......\n");
                break;
            case R.id.bt_stop:
                smartLink.stopSendWifi();
                tx_receive.append("停止发包\n");
                break;
        }
    }

    private void parseData(ReceiveDatagramPacket receiveData) {
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
        smartLink.stop();
        super.onDestroy();
    }
}
