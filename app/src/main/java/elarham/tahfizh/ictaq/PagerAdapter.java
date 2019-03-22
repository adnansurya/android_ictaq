package elarham.tahfizh.ictaq;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    Fragment fragment;
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
       fragment = null;

        switch (position) {
            case 0:
                fragment = new SurahFragment();
                break;

            case 1:
                fragment = new JuzFragment();
                break;


        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
