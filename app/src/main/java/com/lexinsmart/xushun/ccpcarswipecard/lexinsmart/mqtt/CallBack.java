package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.mqtt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class CallBack implements MqttCallbackExtended {
	private String instanceData = "";
	private Handler handler;
	public CallBack(String instance, Handler handler) {
		instanceData = instance;
		this.handler = handler;
	}
	@Override
	public void connectionLost(Throwable cause) {
		try{
			Message msg = new Message();
			msg.what = 3;
			handler.sendMessage(msg);


		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try {
			System.out.println("MQTT:message:"+message.toString());
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			bundle.putString("content", message.toString());
			msg.what = 2;
			msg.setData(bundle);
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}
	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		if (reconnect) {
			System.out.println("Reconnected to : " + serverURI);
			// Because Clean Session is true, we need to re-subscribe
			MqttV3Service.subscribeToTopic(MqttV3Service.topicList);

			Message msg = new Message();
			msg.what = 1;
			msg.obj = "strresult";
			handler.sendMessage(msg);
		} else {
			System.out.println("Connected to: " + serverURI);
		}
	}
}