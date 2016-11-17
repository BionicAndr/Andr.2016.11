package com.bionic.andr.article;

import com.bionic.andr.R;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**  */
public class ArticleDetailsFragment extends Fragment {

    public static final String ARG_TTITLE = "arg:title";

    private TextView title;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_details, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.acticle_details_title);

        final Bundle args = getArguments();
        if (args != null) {
            setTitle(args.getString(ARG_TTITLE));
        }
    }

    public void setTitle(String title) {
        if (this.title != null) {
            this.title.setText(title);
        }
    }

}
