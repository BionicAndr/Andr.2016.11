package com.bionic.andr.core;

import com.bionic.andr.AndrApp;
import com.bionic.andr.api.data.Weather;
import com.bionic.andr.article.ArticleDetailsFragment;

import org.w3c.dom.Text;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**  */
public class UpdateService extends Service {
    private static final String TAG = UpdateService.class.getSimpleName();

    private final LocalBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final Handler mainHadler = new Handler(Looper.getMainLooper());

    public void sync(final UpdateServiceListener listener) {
        AndrApp.getInstance().getApi().getWeatherByCity("London").enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.d(TAG, "Get weather success: " + response.code());

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, "Get weather failed", t);
            }
        });
    }

    public interface UpdateServiceListener {
        void onDataSynchronized();
    }

    public class LocalBinder extends Binder {

        public UpdateService getService() {
            return UpdateService.this;
        }

    }

    public static abstract class UpdateServiceBroadcastReciever extends BroadcastReceiver {

        public static final String ACTION = UpdateService.class.getSimpleName() + ":reciever";

        public void register(Context context) {
            IntentFilter filter = new IntentFilter(ACTION);
            context.registerReceiver(this, filter);
        }

        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION.equals(action)) {
                onDataUpdated();
            }
        }

        public abstract void onDataUpdated();
    }

}
