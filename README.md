# Smartlink
[![](https://jitpack.io/v/lianshangyangguang/Smartlink.svg)](https://jitpack.io/#lianshangyangguang/Smartlink)  
SmartLink配网   

示例代码如下：  
1.初始化 smartLink：
```
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
```
2. 使用
```
 @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_wifi:
                pwd = et_pwd.getText().toString().trim();
                //开始发包
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
                break;
            case R.id.bt_stop:
                //停止发包
                smartLink.stopSendWifi();
                break;
        }
    }
    
```
3.关闭资源
```
   @Override
    protected void onDestroy() {
        super.onDestroy();
        smartLink.stop();
    }
```
