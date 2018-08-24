package com.cloudpoint.plugins.ipc.simple.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cloudpoint.plugins.ipc.simple.DES;
import com.cloudpoint.plugins.ipc.simple.IIpcResponse;
import com.cloudpoint.plugins.ipc.simple.IpcGameSdk;
import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;
import com.cloudpoint.plugins.ipc.simple.IpcIntentProxy;
import com.cloudpoint.plugins.ipc.simple.IpcLipstickSdk;
import com.cloudpoint.plugins.ipc.simple.IpcPkgInfo;
import com.cloudpoint.plugins.ipc.simple.domain.GameEnd;
import com.cloudpoint.plugins.ipc.simple.domain.GameEndState;
import com.cloudpoint.plugins.ipc.simple.domain.GameStart;
import com.cloudpoint.plugins.ipc.simple.domain.GameStartState;
import com.cloudpoint.plugins.ipc.simple.protocol.BaseResponse;
import com.cloudpoint.plugins.ipc.simple.protocol.IIpcCallback;
import com.cloudpoint.plugins.ipc.simple.protocol.gl.GL0001;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0001;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0002;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0003;
import com.cloudpoint.plugins.ipc.simple.protocol.lg.LG0004;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GameActivity extends AppCompatActivity {


    private void l(String message){
        Log.d("IpcGameSdk-demo",message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //TODO: 1. 设置日志输出
        IpcPkgInfo.setDebug(true);
        //TODO: 2. 初始化IpcGameSdk
        IpcGameSdk.init(getApplicationContext(),"119","504107060920821");
        //TODO: 3. 游戏EventBus
        EventBus.getDefault().register(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放IpcGameSdk及EventBus
        IpcGameSdk.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
