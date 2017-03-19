package uf.cnt5517.g21.heatmap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final String TAG = "Heatmap.MainActivity";

    GoogleApiClient mGoogleApiClient;
    Networking mqtt;

    public void toast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Log.d(TAG, "toast: " + text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();

        mqtt = new Networking(this, "http://10.136.4.205:8080", "Android_"+System.currentTimeMillis());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        toast("[onConnected] bundle: " + bundle);
        mqtt.foundBeacon("something");
    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("[onConnectionSuspended] i: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("[onConnectionFailed] Result: " + connectionResult);
    }
}
