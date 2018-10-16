/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.gjj.avgle.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gjj.avgle.R;
import com.gjj.avgle.ui.base.BaseActivity;
import com.gjj.avgle.ui.custom.RoundedImageView;
import com.gjj.avgle.ui.custom.SimpleSearchView;
import com.gjj.avgle.ui.favorites.FavoriteFragment;
import com.gjj.avgle.ui.search.SearchActivity;
import com.gjj.avgle.ui.video.ContentFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by janisharali on 27/01/17.
 */

public class MainActivity extends BaseActivity implements MainMvpView {

    private Fragment currentFragment;

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar)
    public AppBarLayout mAppBarLayout;
    @BindView(R.id.search_view)
    public SimpleSearchView mSearchView;

    @BindView(R.id.drawer_view)
    DrawerLayout mDrawer;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private TextView mNameTextView;

    private TextView mEmailTextView;

    private RoundedImageView mProfileImageView;

    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment contentFragment, favoriteFragment;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        setUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        mSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
            return;
        }

        moveTaskToBack(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSearchView.closeSearch();
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    //.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
            unlockDrawer();
        }
    }

    @Override
    protected void setUp() {
        mSearchView.closeSearch();
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        setupNavMenu();
        initFragments();
        //mPresenter.onNavMenuCreated();
        //mPresenter.onViewInitialized();
    }

    private void initFragments() {
        contentFragment = ContentFragment.newInstance();
        favoriteFragment = FavoriteFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content, contentFragment, ContentFragment.TAG);
        transaction.commit();
        this.currentFragment = contentFragment;
    }


    void setupNavMenu() {
        View headerLayout = mNavigationView.getHeaderView(0);
        mProfileImageView = headerLayout.findViewById(R.id.iv_profile_pic);
        mNameTextView = headerLayout.findViewById(R.id.tv_name);
        mEmailTextView = headerLayout.findViewById(R.id.tv_email);
        //默认选择第一项
        MenuItem selectedItem = mNavigationView.getMenu().findItem(R.id.nav_home);
        getSupportActionBar().setTitle(R.string.home);
        mNavigationView.setCheckedItem(selectedItem.getItemId());
        mNavigationView.setNavigationItemSelectedListener(
                item -> {
                    mDrawer.closeDrawer(GravityCompat.START);
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            getSupportActionBar().setTitle(R.string.home);
                            mPresenter.onDrawerHomeClick();
                            return true;
                        case R.id.nav_favourite:
                            getSupportActionBar().setTitle(R.string.favorite);
                            mPresenter.onDrawerCollectionClick();
                            return true;
                        case R.id.nav_released:
                            getSupportActionBar().setTitle(R.string.home);
                            mPresenter.onDrawerCollectionClick();
                            return true;
                        case R.id.nav_actresses:
                            getSupportActionBar().setTitle(R.string.home);
                            mPresenter.onDrawerRateUsClick();
                            return true;
                        default:
                            return false;
                    }
                });
    }


    @Override
    public void showVideoFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!contentFragment.isAdded()) {
            transaction.add(R.id.content, contentFragment, ContentFragment.TAG);
        } else {
            transaction.show(contentFragment);
        }
        if (currentFragment.isAdded()) {
            transaction.hide(currentFragment);
        }
        transaction.commit();
        this.currentFragment = contentFragment;
    }


    @Override
    public void showFavoriteFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!favoriteFragment.isAdded()) {
            transaction.add(R.id.content, favoriteFragment, ContentFragment.TAG);
        } else {
            transaction.show(favoriteFragment);
        }
        if (currentFragment.isAdded()) {
            transaction.hide(currentFragment);
        }
        transaction.commit();
        this.currentFragment = favoriteFragment;
    }

    @Override
    public void openMyFeedActivity() {
        //startActivity(FeedActivity.getStartIntent(this));
    }

    @Override
    public void closeNavigationDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void lockDrawer() {
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer() {
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


}
