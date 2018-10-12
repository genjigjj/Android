package com.gjj.avgle.adapter;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.app.FragmentTransaction;
import android.view.ViewGroup;

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

    @SuppressLint("CommitTransaction")
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (isDestroy) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            mCurTransaction.remove((Fragment) object);
        } else {
            super.destroyItem(container, position, object);
        }

    }

}
