package uf.cnt5517.g21.heatmap;

import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, BeaconConsumer, MonitorNotifier, RangeNotifier{

    public static final String TAG = "Heatmap.MainActivity";

    GoogleApiClient mGoogleApiClient;
    Networking restful;

    BeaconManager beaconManager;



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

        restful = new Networking(this, "http://192.168.0.30:8080", "Android_"+System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.addRangeNotifier(this);
        beaconManager.addMonitorNotifier(this);
        beaconManager.bind(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        toast("[onConnected] bundle: " + bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("[onConnectionSuspended] i: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("[onConnectionFailed] Result: " + connectionResult);
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("BlueCats", null, null, null);

        /*beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!\n\t"+region);
                restful.foundBeacon(region.getId1().toString()+":"+region.getId2());
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon\n\t"+region);
                restful.lostBeacon(region.getId1().toString()+":"+region.getId2());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });*/

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
            Log.d(TAG, "started monitoring beacons");
        } catch (RemoteException e) {
            Log.e(TAG, "Something went wrong trying to monitor beacons", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for (Beacon beacon: beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                Identifier namespaceId = beacon.getId1();
                Identifier instanceId = beacon.getId2();
                Log.d("RangingActivity", "I see a beacon transmitting namespace id: " + namespaceId +
                        " and instance id: " + instanceId +
                        " approximately " + beacon.getDistance() + " meters away.");
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        ((TextView)MainActivity.this.findViewById(R.id.message)).setText("Hello world, and welcome to Eddystone!");
//                    }
//                });
            }
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.i(TAG, "I just saw an beacon for the first time!\n\t"+region);
        restful.foundBeacon(region.getId1().toString()+":"+region.getId2());
    }

    @Override
    public void didExitRegion(Region region) {
        Log.i(TAG, "I no longer see an beacon\n\t"+region);
        restful.lostBeacon(region.getId1().toString()+":"+region.getId2());
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
    }
}
