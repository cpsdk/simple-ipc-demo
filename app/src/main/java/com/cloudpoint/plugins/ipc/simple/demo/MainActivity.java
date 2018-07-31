package com.cloudpoint.plugins.ipc.simple.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cloudpoint.plugins.ipc.simple.DES;
import com.cloudpoint.plugins.ipc.simple.IIpcResponse;
import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;
import com.cloudpoint.plugins.ipc.simple.IpcIntentProxy;
import com.cloudpoint.plugins.ipc.simple.IpcPkgInfo;
import com.cloudpoint.plugins.ipc.simple.domain.GameEnd;
import com.cloudpoint.plugins.ipc.simple.domain.GameEndState;
import com.cloudpoint.plugins.ipc.simple.domain.GameStart;
import com.cloudpoint.plugins.ipc.simple.domain.GameStartState;

public class MainActivity extends AppCompatActivity implements IIpcResponse<GameEnd> {



    IpcIntentProxy<GameStartState> gameStartStateIpcIntentProxy;

    final IIpcResponse<GameStartState> response = new IIpcResponse<GameStartState>() {
        @Override
        public void onData(GameStartState gameStartState) {
            Log.d("CPLogger",gameStartState.toString());
        }
    };

    IpcIntentProxy<GameEnd> gameEndIpcIntentProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IpcPkgInfo.setDebug(true);
        DES.des().setDesKey("123456789");

        gameStartStateIpcIntentProxy = new IpcIntentProxy<>(getApplicationContext(),GameStartState.class);
        gameStartStateIpcIntentProxy.setCallback(response);

        gameEndIpcIntentProxy = new IpcIntentProxy<>(getApplicationContext(),GameEnd.class);
        gameEndIpcIntentProxy.setCallback(this);
       // IpcIntentAction.startService(IpcIntentAction.get(),IpcGameService.class);

       // IpcIntentAction.createExplicitFromImplicitIntent(IpcIntentAction.get(),);
        //
        //DES.des().setDesKey("504107060920821");
        //
        // send a command to start
        GameStart start =new GameStart("id_129","199",100,30,System.currentTimeMillis());
        Intent i = IpcIntentAction.get(start);
        i.addCategory("119");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //i.setClassName("com.cloudpoint.plugins.ipc.simple.demo","GameActivity");
        getApplicationContext().startActivity(i);



        //getApplicationContext().st

        //start.send(getApplicationContext());

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameStartStateIpcIntentProxy.onDestory();
    }

    @Override
    public void onData(GameEnd gameEnd) {

        Log.d("CPLogger","GameEnd:"+gameEnd.toString());
        GameEndState state =new GameEndState(gameEnd.getOrderId(),gameEnd.getGameId(),1,"ok",System.currentTimeMillis());
        state.send(getApplicationContext());

    }
}
