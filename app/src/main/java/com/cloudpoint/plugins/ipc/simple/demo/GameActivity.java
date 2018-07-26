package com.cloudpoint.plugins.ipc.simple.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cloudpoint.plugins.ipc.simple.DES;
import com.cloudpoint.plugins.ipc.simple.IIpcResponse;
import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;
import com.cloudpoint.plugins.ipc.simple.IpcIntentProxy;
import com.cloudpoint.plugins.ipc.simple.domain.GameEnd;
import com.cloudpoint.plugins.ipc.simple.domain.GameEndState;
import com.cloudpoint.plugins.ipc.simple.domain.GameStart;
import com.cloudpoint.plugins.ipc.simple.domain.GameStartState;

public class GameActivity extends AppCompatActivity {

    IpcIntentProxy<GameEndState> gameStateIpcIntentProxy;

    final IIpcResponse<GameEndState> gameStateIIpcResponse=new IIpcResponse<GameEndState>() {
        @Override
        public void onData(GameEndState gameState) {
            //TODO
            //6.App告知游戏，已接收到消息
            
        }
    };
    GameStart gameStart=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //1.设置des密钥
        DES.des().setDesKey("deskey000202020202020");
        //2.初始化游戏结束状态回调接口
        gameStateIpcIntentProxy =new IpcIntentProxy<>(getApplicationContext(),GameEndState.class);
        gameStateIpcIntentProxy.setCallback(gameStateIIpcResponse);

        //3. app启动GameActivity,将接收到GameStart参数
        gameStart=IpcIntentAction.get(getIntent(),GameStart.class);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //4.游戏启动后，返回状态给app，说明游戏已启动，或游戏启动失败时返回失败状态。
        GameStartState state =new GameStartState(gameStart.getOrderId(),gameStart.getGameId(),1,"ok",System.currentTimeMillis());
        state.send(getApplicationContext());

    }

    //5.游戏结束时通知app游戏结果
    private void onGameEnd(){

        GameEnd end = new GameEnd(gameStart.getOrderId(), gameStart.getGameId(), true, System.currentTimeMillis());
        IpcIntentAction.broadcast(getApplicationContext(),end);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
