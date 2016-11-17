package com.bionic.andr;

import com.bionic.andr.article.ArticleListActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
