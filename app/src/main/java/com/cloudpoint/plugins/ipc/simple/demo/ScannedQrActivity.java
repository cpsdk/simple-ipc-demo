package com.cloudpoint.plugins.ipc.simple.demo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cloudpoint.plugins.lipstick.qr.IScannedResult;
import com.cloudpoint.plugins.lipstick.qr.ScannedQrConnectState;
import com.cloudpoint.plugins.lipstick.qr.ScannedQrLightState;
import com.cloudpoint.plugins.lipstick.qr.ScannedQrScannState;
import com.cloudpoint.plugins.lipstick.qr.ScannedQrSdk;

public class ScannedQrActivity extends AppCompatActivity  implements IScannedResult{


    boolean enabled  = false;
    boolean enableLight = false;


    TextView tvDevState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_qr);
        com.cloudpoint.plugins.lipstick.qr.Pkg_Info.setDebug(true);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ScannedQrSdk.closeDev();
//                ScannedQrSdk.openDev(ScannedQrActivity.this);
//
//                if(ScannedQrSdk.connectState()!=null){
//                    boolean r = ScannedQrSdk.connectState().state== ScannedQrConnectState.Connected.state?true:false;
//                    ScannedQrSdk.enableScan(r);
//                    ScannedQrSdk.enableLight(r);
//                }
//
//                show(null);
//            }
//        },3000l);

        findViewById(R.id.openDev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannedQrSdk.closeDev();
                ScannedQrSdk.openDev(ScannedQrActivity.this);

                if(ScannedQrSdk.connectState()!=null){
                    boolean r = ScannedQrSdk.connectState().state== ScannedQrConnectState.Connected.state?true:false;
                    ScannedQrSdk.enableScan(r);
                    ScannedQrSdk.enableLight(r);
                }

                show(null);

            }
        });

        findViewById(R.id.closeDev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannedQrSdk.enableScan(false);
                ScannedQrSdk.enableLight(false);
                ScannedQrSdk.closeDev();
                show(null);
            }
        });

        findViewById(R.id.enableDev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enabled = !enabled;
                if(ScannedQrSdk.scannState()!=null)
                    enabled = !(ScannedQrSdk.scannState().state== ScannedQrScannState.StartScanning.state)?true:false;
                else
                    enabled=false;
                ScannedQrSdk.enableScan(enabled);

                show(null);
            }
        });

        findViewById(R.id.enableLight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enabled = !enabled;
                if(ScannedQrSdk.lightState()!=null) {
                    enableLight = !(ScannedQrSdk.lightState().state == ScannedQrLightState.LightOn.state) ? true : false;
                }else{
                    enableLight = false;
                }
                ScannedQrSdk.enableLight(enableLight);
                show(null);
            }
        });

        tvDevState = findViewById(R.id.devState);

    }


    private void show(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvDevState.setText("");
                StringBuffer buf = new StringBuffer();
                if(ScannedQrSdk.connectState()!=null)
                    buf.append(" connection:").append(ScannedQrSdk.connectState().message).append("\n");
                if(ScannedQrSdk.lightState()!=null)
                    buf.append(" light     :").append(ScannedQrSdk.lightState().message).append("\n");
                if(ScannedQrSdk.scannState()!=null)
                    buf.append(" scanning  :").append(ScannedQrSdk.scannState().message).append("\n");
                if(s!=null)
                    buf.append(" scanned   :  ").append(s).append("\n");

                tvDevState.setText(buf.toString());
            }
        });
    }

    @Override
    public void onReceived(final String s) {

        show(s);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ScannedQrSdk.closeDev();

    }
}
