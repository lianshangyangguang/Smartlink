# SmartlinkDemo
SmartLink配网  

示例代码如下：
1.初始化 smartLink：
'''
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
'''
2. 使用
'''
 @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_wifi:
                pwd = et_pwd.getText().toString().trim();
                //开始发包
                smartLink.sendWifi(pwd, wifiHandle);
                break;
            case R.id.bt_stop:
                //停止发包
                smartLink.stopSendWifi();
                break;
            case R.id.bt_send_msg:
                //与设备通信
                msg = et_msg.getText().toString().trim();
                smartLink.send("196.128.1.3", 9988, msg, msgHandle);
                break;

        }
    }
    
    //处理接收信息
    Handler wifiHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                
                switch (msg.what) {
                    case UDPBroadcastHelper.RECEIVE_MSG_ERROR:
                        Log.e("my", "HANDLER_MESSAGE_BIND_ERROR");
                        break;
                    case UDPBroadcastHelper.RECEIVE_MSG_SUCCESS:
                        Bundle bundle = msg.getData();
                        parseData(bundle);
                        break;
                }
            }
        };
        
    //处理发送信息
     Handler msgHandle = new Handler() {
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
        };
 '''
 3.关闭资源
 '''
   @Override
    protected void onDestroy() {
        super.onDestroy();
        smartLink.stop();
    }
 '''
