//package unalee.homepage3;
//
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//public class ProfileSettingActivity extends AppCompatActivity{
//    private static final String TAG = "JoinActivity";
//    private EditText etProfileSettingPassword, etProfileSettingReenterPassword,
//            etProfileSettingPhone, etProfileSettingAddress;
//    private Button btProfileSettingCancel, btProfileSettingCecked;
//    private Context context;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile_setting);
//
//        findViews();
//    }
//
//    private void findViews() {
//        etProfileSettingPassword = (EditText) findViewById(R.id.etProfileSettingPassword);
//        etProfileSettingReenterPassword = (EditText) findViewById(R.id.etProfileSettingReenterPassword);
//        etProfileSettingPhone = (EditText) findViewById(R.id.etProfileSettingPhone);
//        etProfileSettingAddress = (EditText) findViewById(R.id.etProfileSettingAddress);
//        btProfileSettingCancel = (Button) findViewById(R.id.btProfileSettingCancel);
//        btProfileSettingCecked = (Button) findViewById(R.id.btProfileSettingCecked);
//        context = ProfileSettingActivity.this;
//
//        btProfileSettingCecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences preferences = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
//                String password = etProfileSettingPassword.getText().toString();
//                String rePassword = etProfileSettingReenterPassword.getText().toString();
//                if (password.equals(rePassword)){
//                    return;
//                }else {
//                    Common.showToast(context, R.string.msa_PasswordNoRight);
//                }
//                String phoneNo = etProfileSettingPhone.getText().toString().trim();
//                String address = etProfileSettingAddress.getText().toString().trim();
//                preferences.edit()
//                        .putBoolean("login", true)
//                        .putString("password", password)
//                        .putString("phoneNo", phoneNo)
//                        .putString("address", address)
//                        .apply();
//
////                if (Common.networkConnected(ProfileSettingActivity.this)){
////                    String url = Common.URL + "/CustomerServlet";
////                    Customer customer = new Customer(0, "", "", password, "",
////                            "", phoneNo, address);
////
////                }
////
////            }
//        });
//
//
//        btProfileSettingCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showMessage();
//            }
//        });
//    }
//
//    private void showMessage() {
//        new AlertDialog.Builder(context)
//                .setTitle("SS Hotel")
//                .setMessage("確認取消修改")
//                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        setResult(2);
//                        finish();
//                    }
//                })
//                .setNegativeButton("取消", null)
//                .show();
////    }
//    }
//
//    @Override
//    protected void  onStart() {
//        super.onStart();
//        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
//    }
//}
