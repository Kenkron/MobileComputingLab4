package uf.cnt5517.g21.heatmap;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
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

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqtt.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    //mqtt.setBufferOpts(disconnectedBufferOptions);
                    //subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri);
                }
            });
            Log.d(TAG, "Initialized: " + clientID);
        } catch (MqttException e){
            Log.e(TAG, "Error connecting to server", e);
        }
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
