package uf.cnt5517.g21.heatmap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final String TAG = "Heatmap.MainActivity";

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int SCAN_PERIOD = 3000;

    GoogleApiClient mGoogleApiClient;
    Networking broker;

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner beaconFinder;

    // Initializes Bluetooth adapter.
    BluetoothManager bluetoothManager;

    // Device scan callback.
    private ScanCallback mLeScanCallback;


    private boolean mScanning;
    private Handler mHandler;

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

        broker = new Networking(this, "http://192.168.0.30:8080", "Android_"+System.currentTimeMillis());


        bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();

        beaconFinder = bluetoothAdapter.getBluetoothLeScanner();

        mHandler = new Handler();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mLeScanCallback =
                new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        MainActivity.this.toast("I Here's what I found: " + result);
                    }
                };

    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    beaconFinder.stopScan(mLeScanCallback);
                    MainActivity.this.toast("I couldn't find anything");
                }
            }, SCAN_PERIOD);

            mScanning = true;
            beaconFinder.startScan(mLeScanCallback);
        } else {
            mScanning = false;
            beaconFinder.stopScan(mLeScanCallback);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        toast("[onConnected] bundle: " + bundle);
        Nearby.Messages.subscribe(mGoogleApiClient, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Found : " + message);
                broker.foundBeacon("message");
            }

            @Override
            public void onLost(Message message) {
                Log.i(TAG, "Lost : " + message);
                broker.lostBeacon("message");
            }
        }, new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build());
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
    protected void onResume() {
        super.onResume();
        scanLeDevice(false);
        toast("scanning");
    }
}
