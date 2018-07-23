package com.cloudpoint.plugins.ipc.simple.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;
import com.cloudpoint.plugins.ipc.simple.domain.GameStart;
import com.cloudpoint.plugins.ipc.simple.domain.GameStartState;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GameStart gameStart=IpcIntentAction.get(getIntent(),GameStart.class);


        GameStartState state =new GameStartState(gameStart.getOrderId(),gameStart.getGameId(),1,"ok",System.currentTimeMillis());
        state.send(getApplicationContext());

    }
}
