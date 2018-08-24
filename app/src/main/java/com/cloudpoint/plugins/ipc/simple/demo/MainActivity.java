package com.cloudpoint.plugins.ipc.simple.demo;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudpoint.plugins.ipc.simple.IpcGameSdk;
import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;

import com.cloudpoint.plugins.ipc.simple.IpcLipstickSdk;
import com.cloudpoint.plugins.ipc.simple.IpcPkgInfo;

import com.cloudpoint.plugins.ipc.simple.domain.GameStart;

import com.cloudpoint.plugins.ipc.simple.protocol.BaseResponse;
import com.cloudpoint.plugins.ipc.simple.protocol.IIpcCallback;
import com.cloudpoint.plugins.ipc.simple.protocol.gl.GL0001;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.BoxMeta;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0001;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0002;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0003;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0004;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IpcLipstickSdk";

    private void l(String message) {
        Log.d("IpcLipstickSdk-demo", message);
    }

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    Map record = new HashMap();

    EditText etGameWin;
    EditText etGameTime;

    private void startRegistration() {
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        //  Create a string map containing information about your service.

        record.put("listenport", String.valueOf(9000));
        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
        record.put("available", "visible");

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                l("Succeed!!!!");
                // Unless you want to update the UI or add logging statements.
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                l("faluire"+arg0);
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: 1. enable log verbose
        IpcPkgInfo.setDebug(true);
        //TODO: 2. initialize Lipstick SDK
        IpcLipstickSdk.init(getApplicationContext(), "123456712", "119", "504107060920821");
        //TODO: 3. register event bus
        EventBus.getDefault().register(this);

        //TODO: 1. 设置日志输出
        IpcPkgInfo.setDebug(true);
        //TODO: 2. 初始化IpcGameSdk
        IpcGameSdk.init(getApplicationContext(),"119","504107060920821");
        //TODO: 3. 游戏EventBus
       // EventBus.getDefault().register(this);

        startRegistration();
        discoverService();

        Button startApp = (Button) findViewById(R.id.startApp);
        startApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                GameStart start = new GameStart();
                Intent i = IpcIntentAction.get(start);
                i.addCategory("119");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //i.setClassName("com.cloudpoint.plugins.ipc.simple.demo","GameActivity");
                getApplicationContext().startActivity(i);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        testLG0001Request();
                        //testLG0003();
                    }
                }, 3000);
//
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //testLG0003();
//                        testLG0002();
//                    }
//                },12000);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        testLG0003();
//                       // testLG0002();
//                    }
//                },15000);


            }
        });

        etGameWin = findViewById(R.id.etGameWin);
        etGameTime = findViewById(R.id.etGameTime);

        findViewById(R.id.lg0001).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                testLG0001Request();
            }
        });

        findViewById(R.id.lg0002).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                testLG0002();
            }
        });

        findViewById(R.id.lg0003).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                testLG0003();
            }
        });
        //getMACAddress();

        findViewById(R.id.lg0004).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                testLG0004();
            }
        });

    }



    private void testLG0001Request() {
        LG0001.LG0001Req req = new LG0001.LG0001Req();
        req.setBackgroud("/storage/emulated/0/bg.png");
        req.setQr("/storage/emulated/0/qr.png");

        ArrayList<BoxMeta> boxes = new ArrayList<>();

        boxes.add(new BoxMeta(1, "image", "brand", " colorSerial", "description",90));
        ArrayList<String> icons = new ArrayList<>();
        icons.add("/icons/icon.png");
        req.setBoxMeta(boxes);
        req.setPayIcons(icons);
        req.setPhone("400-1111-2222");
        req.setDeviceId("123456712");


        req.tx(getApplicationContext(), new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                // on received.
                l(baseResponse.toString());
                testLG0002();
            }
        });


    }

    private void testLG0002() {
        LG0002.LG0002Req req = new LG0002.LG0002Req();
        req.setBoxId(1);
        req.setState(1);
        req.tx(getApplicationContext(), new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                l(baseResponse.toString());
                testLG0003();
            }
        });
    }


    private void testLG0004(){
        LG0004.LG0004Req data = new LG0004.LG0004Req();
        data.setMessage("Hwllww");
        data.setTimeout(30);
        data.tx(getApplicationContext(), new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                Log.d("LG0004",baseResponse.toString());
            }
        });
    }

    private void testLG0003() {
        LG0003.LG0003Req req = new LG0003.LG0003Req();
        req.setOrderId("order123939393");

        int probility = 0;
        int time = 60;

        try {
            String gameWin = etGameWin.getText().toString();
            String gameTime = etGameTime.getText().toString();

            if(gameWin!=null){
                probility = Integer.parseInt(gameWin);
            }
            if(gameTime!=null){
                time = Integer.parseInt(gameTime);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        req.setProbility(probility);
        req.setTimeout(time);
        req.tx(getApplicationContext(), new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                // invoke
                l(baseResponse.toString());

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void handleGL0001(GL0001.GL0001Req req) {

        l(req.toString());


        // TODO: handle request ,then respone the state.
        BaseResponse.tx(getApplicationContext(), req, 0, "ok");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                testLG0003();
            }
        }, 12000);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //TODO: 4. destroy sdk and event bus
        IpcLipstickSdk.onDestroy();
        EventBus.getDefault().unregister(this);

    }


    final HashMap<String, String> buddies = new HashMap<String, String>();

    private void discoverService() {


        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
        /* Callback includes:
         * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         */

            public void onDnsSdTxtRecordAvailable(
                    String fullDomain, Map record, WifiP2pDevice device) {
                Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());
                //buddies.put(device.deviceAddress, record.get("buddyname"));
            }
        };


        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType) {

                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                resourceType.deviceName = buddies
                        .containsKey(resourceType.deviceAddress) ? buddies
                        .get(resourceType.deviceAddress) : resourceType.deviceName;

                // Add to the custom adapter defined specifically for showing

                Log.d(TAG, "onBonjourServiceAvailable " + instanceName);
            }
        };



        mManager.setDnsSdResponseListeners(mChannel,servListener,txtListener);



    }




    /**
     * 处理LG0001请求
     * @param req
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void handleLG0001(LG0001.LG0001Req req){

        l(req.toString());
        // TODO: handle request ,then respone the state.
        BaseResponse.tx(getApplicationContext(),req,0,"ok");

    }

    /**
     * 处理LG0002请求
     * @param req
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void handleLG0002(LG0002.LG0002Req req){

        l(req.toString());
        // TODO: handle request ,then respone the state.
        BaseResponse.tx(getApplicationContext(),req,0,"ok");

    }

    /**
     * 处理LG0003请求
     * @param req
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void handleLG0003(LG0003.LG0003Req req){

        l(req.toString());
        // TODO: handle request ,then respone the state.
        BaseResponse.tx(getApplicationContext(),req,0,"ok");


        testGL0001(req.getOrderId());
    }


    /**
     * 处理LG0003请求
     * @param req
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void handleLG0004(LG0004.LG0004Req req){

        l(req.toString());
        // TODO: handle request ,then respone the state.
        BaseResponse.tx(getApplicationContext(),req,0,"ok");



    }

    /**
     * 发出GL0001指令
     * @param orderId
     */
    private void testGL0001(String orderId){
        GL0001.GL0001Req req =new GL0001.GL0001Req();
        req.setOrderId(orderId);
        req.setState(1);
        req.tx(getApplicationContext(), new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                l(baseResponse.toString());
            }
        });
    }



}