package com.bionic.andr.core;

import org.w3c.dom.Text;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

/**  */
public class UpdateService extends Service {

    private final LocalBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final Handler mainHadler = new Handler(Looper.getMainLooper());


    public void sync(final TextView view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mainHadler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setText("Service synced");
                    }
                });
            }
        }).start();
        view.setText("Service synchronization started");
    }


    public class LocalBinder extends Binder {

        public UpdateService getService() {
            return UpdateService.this;
        }

    }

}
