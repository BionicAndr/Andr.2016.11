package com.bionic.andr;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**  */

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATA_MESSAGE = "message";

    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        input = (EditText) findViewById(R.id.input);

        findViewById(R.id.submit).setOnClickListener(this);

    }

    @Override
    public void onClick(final View view) {
        String message = input.getText().toString();

        Intent result = new Intent();
        result.putExtra(DATA_MESSAGE, message);
        setResult(RESULT_OK, result);

        finish();
    }
}
