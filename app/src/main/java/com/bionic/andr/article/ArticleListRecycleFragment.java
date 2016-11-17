package com.bionic.andr.article;

import com.bionic.andr.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**  */
public class ArticleListRecycleFragment extends Fragment {
    private static final String TAG = ArticleListRecycleFragment.class.getSimpleName();

    private static final String DETAILS_TAG = "article_details";

    private static final String STATE_SELECTED_TITLE = "state:title";

    private static final char[] TITLES = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private RecyclerView recycleView;

    private ArticleAdapter adapter;

    private String selectedTitle = null;

    @Nullable
    private OnArticleSelectionListener listener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            listener = (OnArticleSelectionListener) getActivity();
        } catch (Exception e) {
            Log.e(TAG, "Activity is not OnArticleSelectionListener", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SELECTED_TITLE, selectedTitle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_recycle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycleView = (RecyclerView) view.findViewById(android.R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        recycleView.addItemDecoration(dividerItemDecoration);

        adapter = new ArticleAdapter(getActivity(), TITLES);
        recycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                setArticle(adapter.getItem(position));
            }
        });

        if (savedInstanceState != null) {
            selectedTitle = savedInstanceState.getString(STATE_SELECTED_TITLE);
        }

        if (selectedTitle != null) {
            setArticle(selectedTitle);
        }
    }

    private void setArticle(String title) {
        selectedTitle = title;
        boolean twoPanel = getResources().getBoolean(R.bool.two_panel_mode);
        if (twoPanel) {
            ArticleDetailsFragment f = (ArticleDetailsFragment) getFragmentManager().findFragmentByTag(DETAILS_TAG);
            if (f == null) {
                f = new ArticleDetailsFragment();
                Bundle args = new Bundle();
                args.putString(ArticleDetailsFragment.ARG_TTITLE, selectedTitle);
                f.setArguments(args);
                getFragmentManager().beginTransaction()
                        .replace(R.id.acrticle_details_container, f, DETAILS_TAG)
                        .commit();
            } else {
                f.setTitle(selectedTitle);
            }
        } else {
            final Intent i = new Intent(getActivity(), ArticleDetailsActivity.class);
            i.putExtra(ArticleDetailsFragment.ARG_TTITLE, selectedTitle);
            startActivity(i);
        }
    }

    public interface OnArticleSelectionListener {
        void onArticleSelected(final String title);
    }

    public static class AlphabetViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public AlphabetViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.article_title);
        }

        public void bind(String data) {
            title.setText(data);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class ArticleAdapter extends RecyclerView.Adapter<AlphabetViewHolder> {

        private final LayoutInflater inflater;
        private final List<String> items;

        @Nullable
        private OnItemClickListener listener;

        public ArticleAdapter(Context context, char[] item) {
            inflater = LayoutInflater.from(context);
            items = new ArrayList<>();
            for (char i : item) {
                items.add(Character.toString(i));
            }
        }

        public String getItem(int position) {
            return items.get(position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public AlphabetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.li_article, parent, false);
            final AlphabetViewHolder holder = new AlphabetViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    int position = holder.getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(AlphabetViewHolder holder, int position) {
            String data = items.get(position);
            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }



}
