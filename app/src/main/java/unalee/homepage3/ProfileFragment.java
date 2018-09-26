package unalee.homepage3;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public static final String  TAG = "ProfileFragment";
    private View view;
    private ViewPager viewPager;
    private myPageChangeListener myPageChange;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ProfileInformationFragment(), "會員資訊");
        adapter.addFragment(new ProfileReceiptFragment(), "消費明細");
        adapter.addFragment(new ProfileCommentFragment(), "評論");
        viewPager.setAdapter(adapter);
        myPageChange = new myPageChangeListener();
        viewPager.addOnPageChangeListener(myPageChange);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            return mFragmentTitleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private class myPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.d("scroll to page =", "here");
        }

        @Override
        public void onPageSelected(int arg0) {
            Log.d("select to page =", Integer.toString(arg0));
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.d("change to page =", Integer.toString(arg0));
        }
    }
}





