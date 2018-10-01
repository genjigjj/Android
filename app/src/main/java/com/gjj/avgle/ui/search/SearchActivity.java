package com.gjj.avgle.ui.search;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gjj.avgle.R;
import com.gjj.avgle.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUnBinder(ButterKnife.bind(this));
        setUp();
    }

    @Override
    protected void setUp() {
        Bundle bundle = this.getIntent().getExtras();
        setSupportActionBar(mToolbar);
        if (bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("query") + "的搜索结果");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.content_query, SearchFragment.newInstance(bundle), SearchFragment.TAG)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
