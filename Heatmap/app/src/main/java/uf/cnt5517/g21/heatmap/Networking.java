package uf.cnt5517.g21.heatmap;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by kenkron on 3/18/17.
 */

public class Networking {

    public static final String TAG = "Heatmap.Networking";

    RequestQueue server;
    String clientID;
    String serverUri;

    public Networking(Context c, String serverUri, String clientID) {
        server = new Volley().newRequestQueue(c);
        this.serverUri = serverUri;
        this.clientID = clientID;
    }

    public void foundBeacon(String beaconUUID){
        Log.d(TAG, "found beacon");
        StringRequest request = new StringRequest(Request.Method.POST,
                serverUri +"/found?client=" + clientID + "&uuid=" + beaconUUID,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Server received beacon broadcast.");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Could not tell server about the new beacon", error);
                    }
                });
        server.add(request);
    }

    public void lostBeacon(String beaconUUID){
        StringRequest request = new StringRequest(Request.Method.POST,
                serverUri + "/lost?client=" + clientID + "& uuid=" + beaconUUID,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Server received beacon broadcast.");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Could not tell server about the old beacon", error);
                    }
                });
        server.add(request);
    }
}
