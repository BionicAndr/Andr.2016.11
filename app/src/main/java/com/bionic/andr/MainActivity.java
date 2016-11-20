package com.bionic.andr;

import com.bionic.andr.article.ArticleListActivity;
import com.bionic.andr.db.DbContract;
import com.bionic.andr.db.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_DETAILS = 10050;

    private TextView messsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messsage = (TextView) findViewById(R.id.message);
        messsage.setText("New text");

        findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Intent i = new Intent(context, ArticleListActivity.class);
                startActivityForResult(i, REQUEST_DETAILS);
            }
        });

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

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (requestCode == REQUEST_DETAILS) {
            if (resultCode == RESULT_OK && data != null) {
                String message = data.getStringExtra(DetailsActivity.DATA_MESSAGE);
                this.messsage.setText(message);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
