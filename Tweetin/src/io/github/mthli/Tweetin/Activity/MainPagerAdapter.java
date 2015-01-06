package io.github.mthli.Tweetin.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import io.github.mthli.Tweetin.Flag.Flag;
import io.github.mthli.Tweetin.Fragment.MainFragment;
import io.github.mthli.Tweetin.R;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private MainActivity mainActivity;

    private String[] titles;

    private SparseArray<MainFragment> sparseArray;

    public MainPagerAdapter(
            MainActivity mainActivity
    ) {
        super(mainActivity.getSupportFragmentManager());

        this.mainActivity = mainActivity;

        this.titles = mainActivity.getResources().getStringArray(R.array.tabs);

        this.sparseArray = new SparseArray<MainFragment>();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        MainFragment mainFragment = new MainFragment();

        Bundle bundle = new Bundle();

        switch (position) {
            case Flag.IN_TIMELINE_FRAGMENT:
                bundle.putInt(mainActivity.getString(R.string.bundle_fragment_flag), Flag.IN_TIMELINE_FRAGMENT);
                break;
            case Flag.IN_MENTION_FRAGMENT:
                bundle.putInt(mainActivity.getString(R.string.bundle_fragment_flag), Flag.IN_MENTION_FRAGMENT);
                break;
            case Flag.IN_FAVORITE_FRAGMENT:
                bundle.putInt(mainActivity.getString(R.string.bundle_fragment_flag), Flag.IN_FAVORITE_FRAGMENT);
                break;
            default:
                bundle.putInt(mainActivity.getString(R.string.bundle_fragment_flag), Flag.IN_TIMELINE_FRAGMENT);
                break;
        }

        mainFragment.setArguments(bundle);

        sparseArray.put(position, mainFragment);

        return mainFragment;
    }

    public MainFragment getFragmentFromPosition(int position) {
        return sparseArray.get(position);
    }
}