package com.jwkj.smartlinkdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.mediatek.elian.ElianNative;

import java.util.List;

/**
 * Created by xiyingzhu on 2017/5/12.
 */
public class SmartLink {
    ElianNative elain;
    String ssid;
    String pwd="";
    Context context;
    boolean isRegFilter=false;
    boolean isSend=false;
    boolean is5GWifi=false;
    boolean isWifiEncrypt=false;
    //初始化时调用
    public  SmartLink(Context context,Deal deal){
        this.context = context;
        this.deal = deal;
        regFilter();
        currenWifi();
    }

    public void regFilter(){
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(br,filter);
        isRegFilter=true;
    }

    BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
                currenWifi();
            }
        }
    };


    //判断是不是5Gwifi
    public  boolean is5GWifi(int frequency){
        String str=String.valueOf(frequency);
        if(str.length()>0){
            char a=str.charAt(0);
            if(a=='5'){
                return true;
            }
        }
        return false;
    }

    //获取当前连接wifi的WifiInfo
    public WifiInfo getConnectWifiInfo(){
        if(!isWifiConnected()){
            return null;
        }
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager==null){
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }

    //WiFi是否加密
    public static boolean isWifiEncrypt(ScanResult result) {
        return !(result.capabilities.toLowerCase().indexOf("wep") != -1
                || result.capabilities.toLowerCase().indexOf("wpa") != -1);
    }

    //开始发包
    public void sendWifi(String pwd){
        this.pwd = pwd;
        isSend=true;
        if (!isWifiConnected()||ssid == null || ssid.equals("")||ssid.equals("<unknown ssid>")) {
            Toast.makeText(context,"请先将手机连接到WiFi",Toast.LENGTH_SHORT).show();
            return;
        }

        if(is5GWifi){
            Toast.makeText(context,"设备不支持5G网络",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isWifiEncrypt) {
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(context,"请输入WiFi密码",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (elain == null) {
            elain = new ElianNative();
        }
        if (null != ssid && !"".equals(ssid)) {
            elain.InitSmartConnection(null, 1, 1);
            //wifi名  wifi密码  加密方式
            elain.StartSmartConnection(ssid, pwd, "", mAuthMode);
        }
    }

    //停止发包
    public void stopSendWifi() {
        if(!isSend){
            return;
        }
        if (elain != null) {
            elain.StopSmartConnection();
        }
        isSend=false;
    }

    public interface Deal {
        void onNoSsid();
        void currentSsid(String ssid);
    }

    private Deal deal;
    private byte mAuthMode;
    private byte AuthModeAutoSwitch = 2;
    private byte AuthModeOpen = 0;
    private byte AuthModeShared = 1;
    private byte AuthModeWPA = 3;
    private byte AuthModeWPA1PSKWPA2PSK = 9;
    private byte AuthModeWPA1WPA2 = 8;
    private byte AuthModeWPA2 = 6;
    private byte AuthModeWPA2PSK = 7;
    private byte AuthModeWPANone = 5;
    private byte AuthModeWPAPSK = 4;
    //获取当前连接wifi
    public void currenWifi(){
        WifiInfo wifiInfo = getConnectWifiInfo();
        if (wifiInfo == null && deal !=null) {
            ssid="";
            deal.onNoSsid();
            return;
        }
        ssid = wifiInfo.getSSID();
        if (ssid == null) {
            return;
        }
        if (ssid.equals("")) {
            return;
        }
        if (ssid.length() <= 0) {
            return;
        }
        int a = ssid.charAt(0);
        if (a == 34) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        if (!ssid.equals("<unknown ssid>") && !ssid.equals("0x")&& deal != null) {
            deal.currentSsid(ssid);
        }
        List<ScanResult> wifiList = getLists(context);
        if (wifiList == null) {
            return;
        }
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult result = wifiList.get(i);
            if (!result.SSID.equals(ssid)) {
                continue;
            }
            is5GWifi=is5GWifi(result.frequency);
            isWifiEncrypt=isWifiEncrypt(result);
            boolean bool1, bool2, bool3, bool4;
            bool1 = result.capabilities.contains("WPA-PSK");
            bool2 = result.capabilities.contains("WPA2-PSK");
            bool3 = result.capabilities.contains("WPA-EAP");
            bool4 = result.capabilities.contains("WPA2-EAP");
            if (result.capabilities.contains("WEP")) {
                this.mAuthMode = this.AuthModeOpen;
            }
            if ((bool1) && (bool2)) {
                mAuthMode = AuthModeWPA1PSKWPA2PSK;
            } else if (bool2) {
                this.mAuthMode = this.AuthModeWPA2PSK;
            } else if (bool1) {
                this.mAuthMode = this.AuthModeWPAPSK;
            } else if ((bool3) && (bool4)) {
                this.mAuthMode = this.AuthModeWPA1WPA2;
            } else if (bool4) {
                this.mAuthMode = this.AuthModeWPA2;
            } else {
                if (!bool3)
                    break;
                this.mAuthMode = this.AuthModeWPA;
            }
        }
    }
    //判断是否连接上wifi
    public  boolean isWifiConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected()){
            return true ;
        }
        return false ;
    }

    //获取wifi列表
    public List<ScanResult> getLists(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        List<ScanResult> lists = wifiManager.getScanResults();
        return lists;
    }

    //必须调用
    public void stop(){
        if(isRegFilter){
            context.unregisterReceiver(br);
            isRegFilter=false;
        }
        if(isSend){
            stopSendWifi();
            isSend=false;
        }
    }

}
