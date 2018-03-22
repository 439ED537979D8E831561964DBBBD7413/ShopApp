package com.yj.shopapp.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yj.shopapp.R;

import java.util.List;

/**
 * Fragment管理工具
 */

public class FragmentUtil {
    //Fragment的集合
    private Fragment[] fragments;
    //要创建的Fragment的名称
    private Class<?>[] fragmentName; // HomeFragment.class
    //当前显示
    private List<Fragment> mFragments;
    private int currentIndex = -1;
    //Fragment添加到的容器id
    private int contentId;
    //Fragment管理器
    private FragmentManager fm;

    /**
     * @param fm           Fragment管理器
     * @param fragmentName 需要管理的Fragment的名称集合
     * @param contentId    Fragment需要显示的目标容器id
     */
    public FragmentUtil(FragmentManager fm, Class<?>[] fragmentName, int contentId) {
        this.fm = fm;
        this.fragmentName = fragmentName;
        fragments = new Fragment[fragmentName.length];
        this.contentId = contentId;
    }

    public FragmentUtil(FragmentManager fm, List<Fragment> mFragments, int contentId) {
        this.fm = fm;
        this.mFragments = mFragments;
        fragments = new Fragment[mFragments.size()];
        this.contentId = contentId;
    }

    /**
     * 显示Fragment方法
     *
     * @param index 显示的下标
     */
    public void showFragment(int index) {
        if (currentIndex != index) {
            FragmentTransaction ft = obtainFragmentTransaction(index);
            //隐藏当前
            if (currentIndex != -1) {
                ft.hide(fragments[currentIndex]);
            }
            //显示新的
            if (fragments[index] == null) {
                //创建Fragment对象添加到容器中
                fragments[index] = mFragments.get(index);
                ft.add(contentId, fragments[index]);
            } else {
                ft.show(fragments[index]);
            }
            ft.commit();
            currentIndex = index;
        }
    }

    /**
     * 基于Fragment的class对象创建Fragment对象
     *
     * @param index
     * @return
     */
    private Fragment createFragment(int index) {
        try {
            return (Fragment) fragmentName[index].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除所有Fragment
     */
    public void clearAll() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment f : fragments) {
            if (f != null) {
                ft.remove(f);
            }
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 获取某个Fragment对象
     *
     * @param index
     * @return
     */
    public Fragment getFragment(int index) {
        if (index >= 0 && index < fragments.length) {
            return fragments[index];
        }
        return null;
    }

    /**
     * 获取一个带动画的FragmentTransaction
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fm.beginTransaction();
        // 设置切换动画
        if (currentIndex != -1) {
            if (index > currentIndex) {
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
            } else {
                ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        }
        return ft;
    }
}
