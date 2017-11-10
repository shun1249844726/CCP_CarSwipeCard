package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.mqtt;

import android.os.Handler;
import android.os.Message;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttException;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttPersistenceException;
import com.ibm.micro.client.mqttv3.MqttSecurityException;
import com.ibm.micro.client.mqttv3.MqttTopic;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.MainActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class MqttV3Service {
    String addr = "";
    String port = "";
    public static MqttClient client = null;
    private static MqttTopic topic = null;
    private static ArrayList<MqttTopic> topicList = new ArrayList<MqttTopic>();
    private static MqttConnectOptions conOptions;
    private static ScheduledExecutorService scheduler;
    private static CallBack callback;


    public static boolean connectionMqttServer(Handler handler, String ServAddress, String ServPort, String userID, ArrayList<String> Topics) {
        String connUrl = "tcp://" + ServAddress + ":" + ServPort;
        try {
            client = new MqttClient(connUrl, userID, null);
            for (int i = 0; i < Topics.size(); i++) {
                topic = client.getTopic(Topics.get(i));
                topicList.add((MqttTopic) topic);
            }
            callback = new CallBack(userID, handler);
            client.setCallback(callback);
            conOptions = new MqttConnectOptions();
//			conOptions.setUserName(MyApplication.gUserName);
//			conOptions.setPassword(Pssword.toCharArray());
            conOptions.setCleanSession(true);            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
//			char[] ddd = conOptions.getPassword();
//			System.out.println(ddd);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            conOptions.setKeepAliveInterval(20);
            client.connect(conOptions);
            System.out.println("MQTT: first connection ok");
            for (int i = 0; i < Topics.size(); i++) {
                client.subscribe(Topics.get(i), 1);

            }
        } catch (MqttException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean closeMqtt() {
        try {
            client.disconnect();


        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean publishMsg(String msg, int Qos, int position) {
        System.out.println("MQTT: msg:"+msg);
        boolean successfulFlag = false;
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(Qos);
        MqttDeliveryToken token = null;
        try {
            token = topicList.get(position).publish(message);
            while (!token.isComplete()) {
                token.waitForCompletion(1000);
            }
            System.out.println("MQTT:send message ok!");
            successfulFlag = true;
        } catch (MqttPersistenceException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
//            return false;
            successfulFlag = false;
        } catch (MqttException e) {
            System.err.println("MQTT:Failed to send message");
            e.printStackTrace();
//            return false;
            successfulFlag = false;
        }
//        System.out.println("go on connect "+ token.isComplete());
//        if (token != null) {
//
//            boolean keepTrying = true;
//            do {
//                try {
//                    // Wait for the message delivery to complete
//                    token.waitForCompletion(1000);
//                    successfulFlag = true;
//                    System.out.println("Message delivery complete");
//                } catch (MqttException deliveryException) {
//                    int reasonCode = deliveryException.getReasonCode();
//
//                    // TODO: Retry the message, or decide to stop trying
//                    System.err.println("Message delivery failed");
//                    if (client.isConnected() == false) {
//                        try {
//                            // Re-connect to the server
//                            client.connect();
//                        } catch (MqttException connectException) {
//                            // Can't reconnect, so give up.  If and when the
//                            // client does reconnect, the message delivery
//                            // will automatically continue
//                            keepTrying = false;
//                        }
//                    }
//                }
//            } while (!token.isComplete() && keepTrying);
//        }
        return successfulFlag;

    }

    public static boolean isConnected() {
        if (client.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    //重新链接
    public static void startReconnect(final Handler handler) {
        System.out.println("MQTT:reconnection!");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("MQTT:reconnecting!");
                if (!client.isConnected()) {
                    try {
                        client.setCallback(callback);
                        client.connect(conOptions);
                        client.subscribe("ccp/remote_card/"+Constant.IMEI);
                        System.out.println("MQTT:reconnection ok!");

                        System.out.println(" :重连成功，通知更新界面");
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = "strresult";
                        handler.sendMessage(msg);

                        scheduler.shutdown();

                        System.out.println("结束重连进程");

                    } catch (MqttSecurityException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }else {
                    scheduler.shutdown();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

}

