package com.jwkj.smartlinkdemo;

import android.util.Log;

import com.hdl.udpsenderlib.UDPResult;
import com.hdl.udpsenderlib.UDPResultCallback;
import com.hdl.udpsenderlib.UDPSender;

import java.util.Arrays;

/**
 * Created by xiyingzhu on 2017/5/12.
 */
public class UDPHelper2 {

    public void conn(){
        UDPSender.getInstance().setTargetPort(9988)
                .setInstructions(new byte[]{})
                .send(new UDPResultCallback() {
                    @Override
                    public void onNext(UDPResult udpResult) {
                        Log.d("zxy", "onNext: "+ Arrays.toString(udpResult.getResultData()));
                    }
                });
    }
}
