package unalee.homepage3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginDialogActivity";
    private Window window;
    private EditText etEmail;
    private EditText etPassword;
    private Context context;
    private Button btLogIn, btJoin;
    private CommonTask loginTask;
    String idCustomer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setResult(RESULT_CANCELED);
        initialization();
        btLogIn.setOnClickListener(btLogInListener);
        btJoin.setOnClickListener(btJoinListener);
    }

    @Override
    protected void onStart () {
        super.onStart();
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        if (login) {
            String user = pref.getString("user", "");
            String password = pref.getString("password", "");
            if (isLogIn(user, password)) {
                setResult(RESULT_OK);
                finish();

            }
        }
    }

    private Button.OnClickListener btLogInListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            String user = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (user.length() <= 0 || password.length() <= 0) {
                showMessage();
                return;
            }

            if (isLogIn(user, password)) {
                SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                        MODE_PRIVATE);
                pref.edit()
                        .putBoolean("login", true)
                        .putInt("IdCustomer",Integer.valueOf(idCustomer))
                        .putString("email", user)
                        .putString("password", password)
//                        .putString("gender", gender)
//                        .putString("birthday", birthday)
//                        .putString("phoneNo", phoneNo)
//                        .putString("address", address)
                        .apply();
                Log.e("IdCustomer", idCustomer);
                Common.showToast(LoginActivity.this, "登入成功");
                setResult(RESULT_OK);
                finish();
            } else {
                showMessage();
            }
        }
    };

    private void showMessage () {
        new AlertDialog.Builder(context)
                .setTitle("SS Hotel")
                .setMessage("登入失敗")
                .setPositiveButton("OK", null)
                .show();
    }

    private boolean isLogIn (String user, String password){
        boolean isLogin = false;
        if (Common.networkConnected(LoginActivity.this)){
            String url = Common.URL + "/CustomerServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "userValid");
            jsonObject.addProperty("email", user);
            jsonObject.addProperty("password", password);
            String jsonOut = jsonObject.toString();
            loginTask = new CommonTask(url, jsonOut);
            try {
                String result = loginTask.execute().get();
                if(result.equals("0")) {
                    Common.showToast(LoginActivity.this, R.string.msg_NoProfileFound);
                } else {
                    isLogin = true;
                    idCustomer = result;
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isLogin = false;
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
        return isLogin;

    }


    private Button.OnClickListener btJoinListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, JoinActivity.class);
            startActivity(intent);
        }
    };

    private void initialization () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.setStatusBarColor(Color.parseColor("#01728B"));
        }
    }

    private void findViews() {
        btLogIn = (Button) findViewById(R.id.btLogIn);
        btJoin = (Button) findViewById(R.id.btJoin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogIn.setOnClickListener(btLogInListener);
        btJoin.setOnClickListener(btJoinListener);
        context = LoginActivity.this;
    }
}




