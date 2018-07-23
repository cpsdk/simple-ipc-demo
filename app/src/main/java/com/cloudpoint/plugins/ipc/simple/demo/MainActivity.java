package com.cloudpoint.plugins.ipc.simple.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cloudpoint.plugins.ipc.simple.IpcIntentAction;
import com.cloudpoint.plugins.ipc.simple.domain.GameStart;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       // IpcIntentAction.startService(IpcIntentAction.get(),IpcGameService.class);

       // IpcIntentAction.createExplicitFromImplicitIntent(IpcIntentAction.get(),);

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
}
