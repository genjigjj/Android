package com.gjj.avgle.ui.video;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.avgle.R;
import com.gjj.avgle.adapter.VideoFragmentPagerAdapter;
import com.gjj.avgle.ui.base.BaseFragment;
import com.gjj.avgle.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentFragment extends BaseFragment {

    public static final String TAG = "ContentFragment";

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private int currentSelectPosition = 0;

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void setUp(View view) {
        VideoFragmentPagerAdapter adapter = new VideoFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(AppConstants.CATEGORY_DEFAULT_AVGLE_VALUE.length);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentSelectPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
