package elarham.tahfizh.ictaq.ScheduleFragments;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {
    Fragment fragment;
    int mNumOfTabs;

    public SchedulePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = null;

        switch (position) {
            case 0:
                fragment = new RequestFragment();
                break;

            case 1:
                fragment = new ReadyFragment();
                break;


        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
