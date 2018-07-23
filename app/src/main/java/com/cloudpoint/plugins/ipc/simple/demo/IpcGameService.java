package com.cloudpoint.plugins.ipc.simple.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cloudpoint.plugins.ipc.simple.IIpcResponse;
import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;
import com.cloudpoint.plugins.ipc.simple.IpcIntentProxy;

import com.cloudpoint.plugins.ipc.simple.domain.GameEndState;
import com.cloudpoint.plugins.ipc.simple.domain.GameStart;
import com.cloudpoint.plugins.ipc.simple.domain.GameEndState;
import com.cloudpoint.plugins.ipc.simple.domain.GameStartState;

public class IpcGameService extends Service implements IIpcResponse<GameStart> {


    IpcIntentProxy<GameStart> gameStartIpcIntentProxy;
    IpcIntentProxy<GameEndState> gameStateIpcIntentProxy;

    final IIpcResponse<GameEndState> gameStateIIpcResponse=new IIpcResponse<GameEndState>() {
        @Override
        public void onData(GameEndState gameState) {
            //TODO :logical
        }
    };


    public IpcGameService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gameStartIpcIntentProxy = new IpcIntentProxy<>(getApplicationContext(),GameStart.class);
        gameStartIpcIntentProxy.setCallback(this);

        gameStateIpcIntentProxy =new IpcIntentProxy<>(getApplicationContext(),GameEndState.class);
        gameStateIpcIntentProxy.setCallback(gameStateIIpcResponse);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onData(GameStart gameStart) {

        //TODO: game start

        //


        GameStartState state =new GameStartState(gameStart.getOrderId(),gameStart.getGameId(),1,"ok",System.currentTimeMillis());
        state.send(getApplicationContext());


    }
}
