package com.cloudpoint.plugins.ipc.simple.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cloudpoint.plugins.ipc.simple.IIpcResponse;
import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;
import com.cloudpoint.plugins.ipc.simple.IpcIntentProxy;
import com.cloudpoint.plugins.ipc.simple.IpcIntentReceiver;
import com.cloudpoint.plugins.ipc.simple.domain.GameEnd;
import com.cloudpoint.plugins.ipc.simple.domain.GameEndState;
import com.cloudpoint.plugins.ipc.simple.domain.GameStart;
import com.cloudpoint.plugins.ipc.simple.domain.GameStartState;

public class IpcMasterService extends Service  implements IIpcResponse<GameEnd>{



    IpcIntentProxy<GameEnd> gameEndIpcIntentProxy;
    IpcIntentProxy<GameStartState> gameStateIpcIntentProxy;



    final IIpcResponse<GameStartState> iIpcResponse =new IIpcResponse<GameStartState>() {
        @Override
        public void onData(GameStartState gameState) {

            //TODO: to deal with game state


        }
    };

    public IpcMasterService() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        gameEndIpcIntentProxy = new IpcIntentProxy<>(getApplicationContext(),GameEnd.class);
        gameEndIpcIntentProxy.setCallback(this);

        gameStateIpcIntentProxy = new IpcIntentProxy<>(getApplicationContext(),GameStartState.class);
        gameStateIpcIntentProxy.setCallback(iIpcResponse);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onData(GameEnd gameEnd) {

        // TODO: do some thing

        GameEndState state =new GameEndState(gameEnd.getOrderId(),gameEnd.getGameId(),1,"ok",System.currentTimeMillis());
        state.send(getApplicationContext());



    }
}
