package com.bionic.andr;

import com.bionic.andr.article.ArticleListActivity;
import com.bionic.andr.core.UpdateService;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements UpdateService.UpdateServiceListener {

    private static final int REQUEST_DETAILS = 10050;

    private TextView message;

    private UpdateService service;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UpdateService.LocalBinder binder = (UpdateService.LocalBinder) iBinder;
            service = binder.getService();
            MainActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    private final UpdateService.UpdateServiceBroadcastReciever reciever =
            new UpdateService.UpdateServiceBroadcastReciever() {
                @Override
                public void onDataUpdated() {
                    message.setText("Service synced.. From reciever");
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = (TextView) findViewById(R.id.message);
        message.setText("New text");

        findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (service != null) {
                    service.sync(MainActivity.this);
                }
            }
        });


        Intent intent = new Intent(this, UpdateService.class);
        bindService(intent, connection, Service.BIND_AUTO_CREATE);


        /*
        DbHelper helper = new DbHelper(this);
        SQLiteDatabase wdb = helper.getWritableDatabase();

        ContentValues insertValues = new ContentValues(2);
        insertValues.put(DbContract.Person.NAME, "Bob");
        insertValues.put(DbContract.Person.AGE, 23);

        long result = wdb.insert(DbContract.Person.TABLE, null, insertValues);

        SQLiteDatabase rdb = helper.getReadableDatabase();
        String[] columns = new String[] {
                DbContract.Person.NAME,
                DbContract.Person.AGE
        };

        Cursor c = rdb.query(DbContract.Person.TABLE,
                columns,
                null,
                null,
                null,
                null,
                null
                );

        try {
            int cName = c.getColumnIndex(DbContract.Person.NAME);
            int cAge = c.getColumnIndex(DbContract.Person.AGE);
            while (c.moveToNext()) {
                String name = c.getString(cName);
                int age = c.getInt(cAge);
                Log.d("Database", "Name = " + name + " Age = " + age);
            }
        } finally {
            c.close();
        }
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        reciever.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        reciever.unregister(this);
        unbindService(connection);
    }

    private void onServiceConnected() {
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (requestCode == REQUEST_DETAILS) {
            if (resultCode == RESULT_OK && data != null) {
                String message = data.getStringExtra(DetailsActivity.DATA_MESSAGE);
                this.message.setText(message);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDataSynchronized() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                message.setText("Service synced.. Main thread says");
            }
        });
    }
}
