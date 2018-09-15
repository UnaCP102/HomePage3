package unalee.homepage3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;




public class MainActivity extends AppCompatActivity {
    boolean login = false;


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.item_Reserved:
                    fragment = new ReservedFragment();
                    changeFragment(fragment);
                    setTitle(R.string.textReserved);
                    return true;
                case R.id.item_Date:
                    fragment = new BookingFragment();
                    changeFragment(fragment);
                    setTitle(R.string.textDate);
                    return true;
                case R.id.item_Profile:
                    if (!login) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        fragment = new ProfileFragment();
                        changeFragment(fragment);
                        setTitle(R.string.textProfile);
                    }
                    return true;

                case R.id.item_HomePage:
                    fragment = new HomePageFragment();
                    changeFragment(fragment);
                    setTitle(R.string.textHomePage);
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

        String user = getIntent().getStringExtra("LOGIN_USERID");
        String password = getIntent().getStringExtra("LOGIN_PASSWORD");






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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            if (requestCode == 1){
//                String user = data.getStringExtra("LOGIN_USERID");
//                String password = data.getStringExtra("LOGIN_PASSWORD");
                ProfileFragment profileFragment = new ProfileFragment();
                changeFragment(profileFragment);
                // 建立並切換到profile fragment
            }else {
                finish();
            }

            if (requestCode == 2){
                //在ProfileSettingActivity裡設定
                ProfileFragment profileFragment = new ProfileFragment();
                changeFragment(profileFragment);
            }

            if (requestCode == 3){
                //在LoginActivity裡設定
                HomePageFragment homePageFragment = new HomePageFragment();
                changeFragment(homePageFragment);
            }
        }
    }
}