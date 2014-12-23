package ru.ifmo.colloquium3;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class CourseServer extends IntentService {
    public CourseServer() {
        super("ru.ifmo.colloquium3.Courser");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
