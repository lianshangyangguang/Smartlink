package com.jwkj.smartlinkdemo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mediatek.elian.ElianNative;

import java.net.InetAddress;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    TextView tx_wifiName, tx_receive;
    Button bt_send, bt_stop;
    EditText et_pwd;
    ElianNative elain;
    String ssid;
    String pwd = "";
    boolean isRegFilter = false;
    //    boolean is5GWifi=false;
//    boolean isWifiEncrypt=false;
    public UDPHelper mHelper;
    WifiManager wifiManager;
    boolean isSend = false;
    private SmartLink smartLink;

    static {
        System.loadLibrary("elianjni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initUI();
//        currenWifi();
//        regFilter();
        //监听UDP广播
        mHelper = new UDPHelper(context);
//        listen();
        smartLink = new SmartLink(this, new SmartLink.Deal() {
            @Override
            public void onNoSsid() {
                tx_wifiName.setText("请先连接wifi");
            }

            @Override
            public void currentSsid(String ssid) {
                tx_wifiName.setText(ssid);
            }
        });
         mHelper.receive(9988, handle);
    }


    private void parseReceiveData(ReceiveDatagramPacket receiveData) {

        DeviceInfo deviceInfo = parseData(receiveData);
        String info = deviceInfo.toString();
        if (Integer.parseInt(deviceInfo.getFrag()) == 0) {
            info = info + "无密码";
        } else {
            info = info + "有密码";
        }
        tx_receive.append(info + "\n\n");

    }


    public void initUI() {
        tx_wifiName = (TextView) findViewById(R.id.tx_wifiName);
        tx_receive = (TextView) findViewById(R.id.tx_receive);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_send = (Button) findViewById(R.id.bt_send);
        bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_send.setOnClickListener(this);
        bt_stop.setOnClickListener(this);

    }

    //    public void regFilter(){
//        IntentFilter filter=new IntentFilter();
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        registerReceiver(br,filter);
//        isRegFilter=true;
//    }
//    BroadcastReceiver br=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
//                currenWifi();
//            }
//        }
//    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                pwd = et_pwd.getText().toString().trim();
//                if (!isWifiConnected()||ssid == null || ssid.equals("")||ssid.equals("<unknown ssid>")) {
//                    Toast.makeText(context,"请先将手机连接到WiFi",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(is5GWifi){
//                    Toast.makeText(context,"设备不支持5G网络",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (!isWifiEncrypt) {
//                    if (TextUtils.isEmpty(pwd)) {
//                        Toast.makeText(context,"请输入WiFi密码",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                sendWifi();
//                isSend=true;
                smartLink.sendWifi(pwd);
                tx_receive.append("开始发包......\n");
                break;
            case R.id.bt_stop:
//                if(!isSend){
//                   return;
//                }
                stopSendWifi();
                tx_receive.append("停止发包\n");
//                isSend=false;
                break;
        }
    }

    //    //开始发包
//    private void sendWifi(){
//        smartLink.sendWifi();
//    }
    //停止发包
    private void stopSendWifi() {
        smartLink.stopSendWifi();
    }

    //    void listen() {
//        mHelper.setCallBack(
    Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case UDPHelper.RECEIVE_MSG_ERROR:
                    Log.e("my", "HANDLER_MESSAGE_BIND_ERROR");
                    break;
                case UDPHelper.RECEIVE_MSG_SUCCESS:
                    Bundle bundle = msg.getData();
                    ReceiveDatagramPacket receiveData = (ReceiveDatagramPacket) bundle.getSerializable("receiveData");
//                    String deviceId = bundle.getString("contactId");//设备ID
//                    String frag = bundle.getString("frag");//有无密码标记
//                    String ip = bundle.getString("ip");//id地址
//                    int type = bundle.getInt("type", 0);//设备主类型
//                    int subType = bundle.getInt("subType", 0);//设备子类型
//                    String deviceInfo = "deviceId=" + deviceId + " deviceType=" + type + " subType=" + subType + " ip=" + ip;
//                    if (Integer.parseInt(frag) == 0) {
//                        deviceInfo = deviceInfo + "无密码";
//                    } else {
//                        deviceInfo = deviceInfo + "有密码";
//                    }
//                    tx_receive.append(deviceInfo + "\n\n");
                    parseReceiveData(receiveData);
                    break;
            }
        }
//
    };
//    }
//    private byte mAuthMode;
//    private byte AuthModeAutoSwitch = 2;
//    private byte AuthModeOpen = 0;
//    private byte AuthModeShared = 1;
//    private byte AuthModeWPA = 3;
//    private byte AuthModeWPA1PSKWPA2PSK = 9;
//    private byte AuthModeWPA1WPA2 = 8;
//    private byte AuthModeWPA2 = 6;
//    private byte AuthModeWPA2PSK = 7;
//    private byte AuthModeWPANone = 5;
//    private byte AuthModeWPAPSK = 4;
//    //获取当前连接wifi
//    public void currenWifi(){
//        WifiInfo wifiInfo = getConnectWifiInfo();
//        if (wifiInfo == null) {
//            ssid="";
//            tx_wifiName.setText("请先连接wifi");
//            return;
//        }
//        ssid = wifiInfo.getSSID();
//        if (ssid == null) {
//            return;
//        }
//        if (ssid.equals("")) {
//            return;
//        }
//        if (ssid.length() <= 0) {
//            return;
//        }
//        int a = ssid.charAt(0);
//        if (a == 34) {
//            ssid = ssid.substring(1, ssid.length() - 1);
//        }
//        if (!ssid.equals("<unknown ssid>") && !ssid.equals("0x")) {
//            tx_wifiName.setText(ssid);
//        }
//        List<ScanResult> wifiList = getLists(context);
//        if (wifiList == null) {
//            return;
//        }
//        for (int i = 0; i < wifiList.size(); i++) {
//            ScanResult result = wifiList.get(i);
//            if (!result.SSID.equals(ssid)) {
//                continue;
//            }
////            is5GWifi=is5GWifi(result.frequency);
////            isWifiEncrypt=isWifiEncrypt(result);
//            boolean bool1, bool2, bool3, bool4;
//            bool1 = result.capabilities.contains("WPA-PSK");
//            bool2 = result.capabilities.contains("WPA2-PSK");
//            bool3 = result.capabilities.contains("WPA-EAP");
//            bool4 = result.capabilities.contains("WPA2-EAP");
//            if (result.capabilities.contains("WEP")) {
//                this.mAuthMode = this.AuthModeOpen;
//            }
//            if ((bool1) && (bool2)) {
//                mAuthMode = AuthModeWPA1PSKWPA2PSK;
//            } else if (bool2) {
//                this.mAuthMode = this.AuthModeWPA2PSK;
//            } else if (bool1) {
//                this.mAuthMode = this.AuthModeWPAPSK;
//            } else if ((bool3) && (bool4)) {
//                this.mAuthMode = this.AuthModeWPA1WPA2;
//            } else if (bool4) {
//                this.mAuthMode = this.AuthModeWPA2;
//            } else {
//                if (!bool3)
//                    break;
//                this.mAuthMode = this.AuthModeWPA;
//            }
//        }
//    }
//    //判断是否连接上wifi
//    public  boolean isWifiConnected(){
//        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if(wifiNetworkInfo.isConnected()){
//            return true ;
//        }
//        return false ;
//    }
    //获取当前连接wifi的WifiInfo
//    public WifiInfo getConnectWifiInfo(){
//        if(!isWifiConnected()){
//            return null;
//        }
//        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//        if(wifiManager==null){
//            return null;
//        }
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        return wifiInfo;
//    }
//    //获取wifi列表
//    public List<ScanResult> getLists(Context context) {
//        wifiManager = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        wifiManager.startScan();
//        List<ScanResult> lists = wifiManager.getScanResults();
//        return lists;
//    }
//    //判断是不是5Gwifi
//    public static boolean is5GWifi(int frequency){
//        String str=String.valueOf(frequency);
//        if(str.length()>0){
//            char a=str.charAt(0);
//            if(a=='5'){
//                return true;
//            }
//        }
//        return false;
//    }

    //    //WiFi是否加密
//    public static boolean isWifiEncrypt(ScanResult result) {
//        return !(result.capabilities.toLowerCase().indexOf("wep") != -1
//                || result.capabilities.toLowerCase().indexOf("wpa") != -1);
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(isSend){
//            stopSendWifi();
//            isSend=false;
//        }
//        if(isRegFilter){
//            unregisterReceiver(br);
//            isRegFilter=false;
//        }
        smartLink.stop();
        if (mHelper != null) {
            mHelper.StopListen();
        }
    }

    public DeviceInfo parseData(ReceiveDatagramPacket receiveData) {
        if (receiveData == null) {
            return null;
        }
        DeviceInfo deviceInfo = null;
//        if (receiveData.getState() == ReceiveDatagramPacket.RECEIVE_MSG_SUCCESS) {
        InetAddress mInetAddress = receiveData.getmInetAddress();
        byte[] data = receiveData.getData();
        int subType = 0;
        int contactId = bytesToInt(data, 16);
        int rflag = bytesToInt(data, 12);
        int type = bytesToInt(data, 20);
        int frag = bytesToInt(data, 24);
        int curVersion = (rflag >> 4) & 0x1;
        if (curVersion == 1) {
            subType = bytesToInt(data, 80);
        }
        String ip_address = mInetAddress.getHostAddress();
        deviceInfo = new DeviceInfo(String.valueOf(contactId), String.valueOf(frag),
                ip_address.substring(
                        ip_address.lastIndexOf(".") + 1,
                        ip_address.length()),
                ip_address, type, rflag, subType);
//        } else if (receiveData.getState() == ReceiveDatagramPacket.RECEIVE_MSG_ERROR) {
//            Log.e("my", "RECEIVE_MSG_ERROR");
//        }
        return deviceInfo;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24);
        return value;
    }

}
