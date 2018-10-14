package com.gjj.avgle.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;

import com.gjj.avgle.ui.video.VideoFragment;
import com.gjj.avgle.utils.AppConstants;

public class VideoFragmentPagerAdapter extends AppFragmentPagerAdapter {

    private boolean isDestroy = false;

    private FragmentManager mFragmentManager;

    private FragmentTransaction mCurTransaction;

    public VideoFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        VideoFragment videoFragment = VideoFragment.newInstance();
        videoFragment.setC(AppConstants.CATEGORY_DEFAULT_AVGLE_VALUE[position]);
        return videoFragment;
    }

    @Override
    public int getCount() {
        return AppConstants.CATEGORY_DEFAULT_AVGLE_NAME.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return AppConstants.CATEGORY_DEFAULT_AVGLE_NAME[position];
    }

}
