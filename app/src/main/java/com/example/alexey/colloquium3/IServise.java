package com.example.alexey.colloquium3;

/**
 * Created by Alexey on 01.12.2014.
 */

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

public class IServise extends IntentService {


    static String RECEIVER = "1";
    static int STATUS_RUNNING = 2;
    static String RECEIVER_DATA = "4";
    static int STATUS_FINISHED = 5;

    public IServise() {
        this("IServise");
    }

    public IServise(String name) {
        super(name);
    }


    public void onCreate() {
        super.onCreate();

        Log.i("Started", "IService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("started", "onhandle");
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);


       /* receiver.send(STATUS_RUNNING,
                Bundle.EMPTY);


        final Bundle data = new Bundle();

        String s = intent.getStringExtra("task");
        if (s.equals("dont_w")) {
            data.putString(RECEIVER_DATA, "Sample result data");
            receiver.send(STATUS_FINISHED, data);
            return;
        }

*/
    }
}




