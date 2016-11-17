package com.bionic.andr.article;

import com.bionic.andr.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**  */
public class ArticleDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_fragment);

        boolean twoPanel = getResources().getBoolean(R.bool.two_panel_mode);
        if (twoPanel) {
            finish();
            return;
        } else if (savedInstanceState == null) {
            final ArticleDetailsFragment f = new ArticleDetailsFragment();
            f.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        }

    }
}
