package unalee.homepage3;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ProfileSettingActivity extends AppCompatActivity{
    private static final String TAG = "JoinActivity";
    private EditText etProfileSettingPassword, etProfileSettingReenterPassword,
            etProfileSettingPhone, etProfileSettingAddress;
    private TextView txProfileSettingBDay, txProfileSettingEmail, txProfileSettingName;
    private Button btProfileSettingCancel, btProfileSettingChecked;
    private Activity activity;
    private CommonTask userFindTask;
    private SharedPreferences preferences;

    @Override
    protected void  onStart() {
        super.onStart();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Common.askPermissions(activity, permissions, Common.REQ_EXTERNAL_STORAGE);
        fillprofile();

         preferences = activity.getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        activity = ProfileSettingActivity.this;

        findViews();
    }

    private void findViews() {
        txProfileSettingBDay = (TextView) findViewById(R.id.txProfileSettingBDay);
        txProfileSettingEmail = (TextView) findViewById(R.id.txProfileSettingEmail);
        txProfileSettingName = (TextView) findViewById(R.id.txProfileSettingName);
        etProfileSettingPassword = (EditText) findViewById(R.id.etProfileSettingPassword);
        etProfileSettingReenterPassword = (EditText) findViewById(R.id.etProfileSettingReenterPassword);
        etProfileSettingPhone = (EditText) findViewById(R.id.etProfileSettingPhone);
        etProfileSettingAddress = (EditText) findViewById(R.id.etProfileSettingAddress);
        btProfileSettingCancel = (Button) findViewById(R.id.btProfileSettingCancel);
        btProfileSettingChecked = (Button) findViewById(R.id.btProfileSettingChecked);


        btProfileSettingChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etProfileSettingPassword.getText().toString().trim();
                String rePassword = etProfileSettingReenterPassword.getText().toString().trim();
//                if (password.equals(rePassword)){
//                    return;
//                }else {
//                    Common.showToast(activity, R.string.msa_PasswordNoRight);
//                }
                String phoneNo = etProfileSettingPhone.getText().toString().trim();
                String address = etProfileSettingAddress.getText().toString().trim();
                int idCustomer = preferences.getInt("idCustomer", 0);
                if (Common.networkConnected(ProfileSettingActivity.this)){
                    String url = Common.URL + "/CustomerServlet";
                    Customer customer = new Customer(idCustomer, password, phoneNo, address);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "customerUpdate");
                    jsonObject.addProperty("customer", new Gson().toJson(customer));
                    int count = 0;
                    String result = null;
                    try {
                        result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    if (result == null){
                        Common.showToast(activity, R.string.msg_UpdateFail);
                    }else {
                        Common.showToast(activity, R.string.msg_UpdateSuccess);
                    }
                }else{
                    Common.showToast(activity, R.string.msg_NoNetwork);
                }
                finish();
            }
        });


        btProfileSettingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage();
            }
        });
    }

    private void showMessage() {
        new AlertDialog.Builder(activity)
                .setTitle("SS Hotel")
                .setMessage("確認取消修改")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(2);
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void fillprofile() {
        preferences = activity.getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            int idCustomer = preferences.getInt("idCustomer", 0);

            if (Common.networkConnected(activity)) {
                String url = Common.URL + "/CustomerServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "findById");
                jsonObject.addProperty("idCustomer", idCustomer);

                String jsonOut = jsonObject.toString();
                userFindTask = new CommonTask(url, jsonOut);
                Customer customer = null;
                try {
                    String result = userFindTask.execute().get();
                    Log.e(TAG, "result:" + result);
                    customer = new Gson().fromJson(result, Customer.class);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (customer == null) {
                    Common.showToast(activity, R.string.msg_NoProfileFound);

                } else {
                    txProfileSettingName.setText(customer.getName());
                    txProfileSettingEmail.setText(customer.getEmail());
                    txProfileSettingBDay.setText(customer.getBirthday());
                    etProfileSettingPhone.setText(customer.getPhone());
                    etProfileSettingAddress.setText(customer.getAddress());
                }
            }else {
                Common.showToast(activity, R.string.msg_NoNetwork);
            }
    }
}
