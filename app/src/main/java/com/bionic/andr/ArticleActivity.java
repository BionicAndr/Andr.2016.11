package com.bionic.andr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**  */
public class ArticleActivity extends AppCompatActivity implements
        ArticleListFragment.OnArticleSelectionListener {

    private static final String LIST_TAG = "article_list";
    private static final String DETAILS_TAG = "article_details";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

    }

    @Override
    public void onArticleSelected(final String title) {
        Toast.makeText(this, "Item " + title + " clicked", Toast.LENGTH_SHORT).show();

        ArticleDetailsFragment f = (ArticleDetailsFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_TAG);
        if (f == null) {
            f = new ArticleDetailsFragment();
            Bundle args = new Bundle();
            args.putString(ArticleDetailsFragment.ARG_TTITLE, title);
            f.setArguments(args);


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, f, DETAILS_TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            f.setTitle(title);
        }
    }

}
