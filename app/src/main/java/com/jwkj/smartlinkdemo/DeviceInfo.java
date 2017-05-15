package com.jwkj.smartlinkdemo;

import android.util.Log;

import java.net.InetAddress;

/**
 * Created by Administrator on 2017/5/14.
 */
public class DeviceInfo {
    private String contactId;
    private String frag;
    private String ipFlag;
    private String ip;
    private int type;
    private int rflag;
    private int subType;

    public DeviceInfo() {
    }

    public DeviceInfo(String contactId, String frag, String ipFlag, String ip, int type, int rflag, int subType) {
        this.contactId = contactId;
        this.frag = frag;
        this.ipFlag = ipFlag;
        this.ip = ip;
        this.type = type;
        this.rflag = rflag;
        this.subType = subType;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getFrag() {
        return frag;
    }

    public void setFrag(String frag) {
        this.frag = frag;
    }

    public String getIpFlag() {
        return ipFlag;
    }

    public void setIpFlag(String ipFlag) {
        this.ipFlag = ipFlag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRflag() {
        return rflag;
    }

    public void setRflag(int rflag) {
        this.rflag = rflag;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    @Override
    public String toString() {
        return  "deviceId=" + contactId + " deviceType=" + type + " subType=" + subType + " ip=" + ip;
    }

    public static DeviceInfo parseReceiveData(ReceiveDatagramPacket receiveData) {
        if (receiveData == null) {
            return null;
        }
        DeviceInfo deviceInfo = null;
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
        Log.d("zxy", "parseData== " + contactId + "," + rflag + "," + type + "," + curVersion);
        String ip_address = mInetAddress.getHostAddress();
        deviceInfo = new DeviceInfo(String.valueOf(contactId), String.valueOf(frag),
                ip_address.substring(
                        ip_address.lastIndexOf(".") + 1,
                        ip_address.length()),
                ip_address, type, rflag, subType);
        return deviceInfo;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24);
        return value;
    }


}
