package com.bionic.andr.article;

import com.bionic.andr.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**  */
public class ArticleListActivity extends AppCompatActivity {

    private static final String LIST_TAG = "article_list";
    private static final String DETAILS_TAG = "article_details";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
    }

}
