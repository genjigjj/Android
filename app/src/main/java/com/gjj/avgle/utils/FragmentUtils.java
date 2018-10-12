package com.gjj.avgle.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentUtils {

    /**
     * 切换
     *
     * @param fragmentManager fm管理器
     * @param currentFragment 当前
     * @param toShowFragment  将要显示
     * @param viewId          容器id
     * @param tag             position标识id
     * @param isInnerReplace  是否是同一位置替换
     * @return 当前显示
     */
    public static Fragment switchContent(FragmentManager fragmentManager, Fragment currentFragment, Fragment toShowFragment, int viewId, String tag, boolean isInnerReplace) {
        if (fragmentManager == null) {
            return null;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //如果当前为空，则直接加入
        if (currentFragment == null) {
            transaction.add(viewId, toShowFragment, tag).commit();
            return toShowFragment;
        }
        //如果当前等于要显示的，直接返回，其实并不会触发，因为点击相同位置，BottomNavigationBar 并不会回调点击事件
        if (currentFragment == toShowFragment) {
            return toShowFragment;
        }
        // 先判断当前位置是否已经存在一个add过的
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(currentFragment).add(viewId, toShowFragment, tag).commit();
            return toShowFragment;
        } else {
            //同一位置切换，则先移除旧，替换新的（例如：从91视频切换到朱古力视频，是同一位置）
            if (isInnerReplace) {
                transaction.remove(fragment);
                //再add新的
                transaction.add(viewId, toShowFragment, tag).commit();
            } else {
                transaction.hide(currentFragment).show(fragment).commit();
            }
            return toShowFragment;
        }

    }


}
