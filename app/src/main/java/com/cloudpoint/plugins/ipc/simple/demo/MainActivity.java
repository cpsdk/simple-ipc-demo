package com.cloudpoint.plugins.ipc.simple.demo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private void l(String message){
        Log.d("IpcLipstickSdk-demo",message);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: 1. enable log verbose
        IpcPkgInfo.setDebug(true);
        //TODO: 2. initialize Lipstick SDK
        IpcLipstickSdk.init(getApplicationContext(),"123456712","119","504107060920821");
        //TODO: 3. register event bus
        EventBus.getDefault().register(this);


        Button startApp=(Button)findViewById(R.id.startApp);
        startApp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                GameStart start =new GameStart();
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
                },10000);
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


        findViewById(R.id.lg0001).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                testLG0001Request();
            }
        });

        findViewById(R.id.lg0002).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                testLG0002();
            }
        });

        findViewById(R.id.lg0003).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                testLG0003();
            }
        });





    }



    private void testLG0001Request(){
        LG0001.LG0001Req req = new LG0001.LG0001Req();
        req.setBackgroud("/storage/emulated/0/bg.png");
        req.setQr("/storage/emulated/0/qr.png");

        ArrayList<BoxMeta> boxes =new ArrayList<>();

        boxes.add(new BoxMeta(1, "image", "brand", " colorSerial", "description" ));
        ArrayList<String> icons =new ArrayList<>();
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

    private void testLG0002(){
        LG0002.LG0002Req req =new LG0002.LG0002Req();
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


    private void testLG0003(){
        LG0003.LG0003Req req =new LG0003.LG0003Req();
        req.setOrderId("order123939393");
        req.setProbility(1);
        req.setTimeout(30);
        req.tx(getApplicationContext(), new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                // invoke
                l(baseResponse.toString());

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void handleGL0001(GL0001.GL0001Req req){

        l(req.toString());


        // TODO: handle request ,then respone the state.
        BaseResponse.tx(getApplicationContext(),req,0,"ok");
        testLG0002();

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


}
