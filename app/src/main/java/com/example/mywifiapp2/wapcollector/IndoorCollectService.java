package com.example.mywifiapp2.wapcollector;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;

/**
 * Must register in manifest file before using.
 */

public class IndoorCollectService extends Service {
    public static final String TAG = "IndoorCollectService";
    public static final String ACTION_BROADCAST_DATA = "IndoorCollectService.BroadcastRSSI";
    public static final String ACTION_BROADCAST_COMMAND = "IndoorCollectService.BroadcastCommand";

    private XWiFiScanner wifiScanner;

    private IntentFilter cmdFilter;
    private CommandReceiver cmdReceiver;

    public IndoorCollectService() {
    }

    //Only run once
    @Override
    public void onCreate() {
        super.onCreate();

        cmdFilter = new IntentFilter(ACTION_BROADCAST_COMMAND);
        cmdReceiver = new CommandReceiver();

        Log.d(TAG, "service onCreate() executed");
    }

    //Run when every time startService ready to receive start command
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.registerReceiver(cmdReceiver, cmdFilter);

        Log.d(TAG, "service onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    private void stopScanThread() {

        if (wifiScanner != null) {
            wifiScanner.stopScan();
            wifiScanner = null;
        }
    }

    private void startScanThread(boolean bWiFi) {
        stopScanThread();

        if (bWiFi && wifiScanner == null) {
            wifiScanner = new XWiFiScanner(this);
            wifiScanner.registerScanListener(new XWiFiScanner.WiFiScanListener() {
                @Override
                public void onWiFiDiscovered(ArrayList<ScanResult> listWifi) {
                    Intent broadcastDataIntent = new Intent(ACTION_BROADCAST_DATA);
                    broadcastDataIntent.putParcelableArrayListExtra(IndoorCollectManager.TAG_WIFI_DATA, listWifi);
                    sendBroadcast(broadcastDataIntent);
                }
            });
            wifiScanner.start();
        }
    }

    /**
     * It's not always running scan
     * just start scan thread when receive command and stop scan once result return to manager.
     */
    public class CommandReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean ifStart = intent.getBooleanExtra(IndoorCollectManager.TAG_START_STOP, false);
            if (ifStart) {

                boolean bWiFi = intent.getBooleanExtra(IndoorCollectManager.TAG_B_WIFI, false);

                Log.d(TAG, "startScanThread: " + bWiFi);
                if (bWiFi) {
                    startScanThread(bWiFi);
                }
            } else {
                stopScanThread();
            }
        }
    }


    @Override
    public void onDestroy() {
        this.unregisterReceiver(cmdReceiver);
        super.onDestroy();
        Log.d(TAG, "Service onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

