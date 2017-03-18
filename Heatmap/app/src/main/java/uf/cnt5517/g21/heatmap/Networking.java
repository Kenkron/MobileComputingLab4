package uf.cnt5517.g21.heatmap;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by kenkron on 3/18/17.
 */

public class Networking implements MqttCallbackExtended{

    public static final String TAG = "Heatmap.Networking";

    MqttAndroidClient mqtt;

    public Networking(Context c, String serverURI, String clientID) {
        mqtt = new MqttAndroidClient(c.getApplicationContext(), serverURI, clientID);
        mqtt.setCallback(this);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        Log.d(TAG, "connectComplete: " + serverURI);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete: " + token);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "messageArrived: " + topic + "\n\t" + message);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG, "connectionLost", cause);
    }
}
