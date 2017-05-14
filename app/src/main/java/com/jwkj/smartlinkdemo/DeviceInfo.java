package com.jwkj.smartlinkdemo;

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


}
