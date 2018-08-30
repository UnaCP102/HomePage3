package unalee.homepage3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainEmployeeActivity extends AppCompatActivity {
    boolean login = false;


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.item_DayList:
                    fragment = new EmployeeDayListFragment();
                    changeFragment(fragment);
                    setTitle("日報表");
                    return true;
                case R.id.item_Instantly:
                    fragment = new EmployeeInstantlyServiceFragment();
                    changeFragment(fragment);
                    setTitle("即時服務");
                    return true;
                case R.id.item_EmployeePage:
                    fragment = new EmployeePageFragment();
                    changeFragment(fragment);
                    setTitle("員工資訊");
                    return true;

                case R.id.item_HomePage:
                    fragment = new HomePageFragment();
                    changeFragment(fragment);
                    setTitle("首頁");
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        initContent();

    }

    private void initContent() {
        Fragment fragment = new HomePageFragment();
        changeFragment(fragment);
        setTitle(R.string.textHomePage);
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }


}

