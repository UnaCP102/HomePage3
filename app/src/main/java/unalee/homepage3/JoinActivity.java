package unalee.homepage3;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "JoinActivity";
    private int year, month, day;
    private LinearLayout llDate;
    private StringBuffer date;
    private Context context;
    private Button btJoinChecked, btJoinCancel, btDate;
    private EditText etJoinName, etJoinEmail, etJoinPassword, etJoinReenterPassword, etJoinPhone, etJoinAddress;
    private RadioButton rbgender;
    private RadioGroup rgGroup;
    private boolean customerExist = false;
    private CommonTask userExistTask, userRegisterTask;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_join);


        context = this;
        date = new StringBuffer();
        handleView();

        etJoinEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (Common.networkConnected(JoinActivity.this)) {
                        String url = Common.URL + "/CustomerServlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "userExist");
                        jsonObject.addProperty("email", etJoinEmail.getText().toString());
                        String jsonOut = jsonObject.toString();
                        userExistTask = new CommonTask(url, jsonOut);
                        try {
                            String result = userExistTask.execute().get();
                            customerExist = Boolean.valueOf(result);
                            Log.d(TAG, "" + customerExist);
                            if(customerExist) {
                                AlertDialog.Builder jump = new AlertDialog.Builder(context);
                                jump.setTitle("SS Hotel");
                                jump.setMessage("Email已存在。");
                                jump.setPositiveButton("確定", null);
                                jump.show();
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }
                }
            }
        });

        btJoinChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etJoinName.getText().toString().trim();
                if (name.length() <= 0) {
                    Common.showToast(context, R.string.msg_NameIsInvalid);
                    return;
                }
                String email = etJoinEmail.getText().toString().trim();
                String password = etJoinPassword.getText().toString();
                String rePassword = etJoinReenterPassword.getText().toString();
                rbgender = (RadioButton) findViewById(rgGroup.getCheckedRadioButtonId());
                String gender = rbgender.getText().toString();
                String birthDay = btDate.getText().toString();
                String phoneNo = etJoinPhone.getText().toString().trim();
                String address = etJoinAddress.getText().toString().trim();
                if (!password.equals(rePassword)) {
                    new AlertDialog.Builder(context)
                            .setTitle("SS Hotel")
                            .setMessage("請確認密碼。")
                            .setPositiveButton("確定", null);
                }
                if (Common.networkConnected(JoinActivity.this)){
                    String url = Common.URL + "/CustomerServlet";
                    Customer customer = new Customer(0, email, name, email, password, gender, birthDay, phoneNo, address);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "customerInsert");
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
                        Common.showToast(context, R.string.msg_InsertFail);
                    }else {
                        Common.showToast(context, R.string.msg_InsertSuccess);
                    }
                }else{
                    Common.showToast(context, R.string.msg_NoNetwork);
                }
                finish();
            }
        });

        btJoinCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("SS Hotel")
                        .setMessage("取消註冊？")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("我後悔了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("resId", view.getId());
                datePickerFragment.setArguments(bundle);
                FragmentManager fm = getSupportFragmentManager();
                datePickerFragment.show(fm, "datePicker");
            }
        });


    }

    private void handleView() {
        llDate = (LinearLayout) findViewById(R.id.llDate);
        btDate = (Button) findViewById(R.id.btDate);
        btJoinCancel = (Button) findViewById(R.id.btJoinCancel);
        btJoinChecked = (Button) findViewById(R.id.btJoinChecked);
        etJoinName = (EditText) findViewById(R.id.etJoinName);
        etJoinEmail = (EditText) findViewById(R.id.etJoinEmail);
        etJoinPassword = (EditText) findViewById(R.id.etJoinPassword);
        etJoinReenterPassword = (EditText) findViewById(R.id.etJoinReenterPassword);
        etJoinPhone = (EditText) findViewById(R.id.etJoinPhone);
        etJoinAddress = (EditText) findViewById(R.id.etJoinAddress);
        rgGroup = (RadioGroup) findViewById(R.id.rgGender);



    }


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        FragmentActivity activity;
        Bundle bundle;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            activity = getActivity();
            bundle = getArguments();
            if (activity == null || bundle == null) {
                Log.e(TAG, "activity or bundle is null");
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int resId = bundle.getInt("resId");
            Button button = activity.findViewById(resId);

            // 以下為DatePicker
            int year, month, day;
            Calendar calendar = Calendar.getInstance();
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(button.getText().toString());
                calendar.setTime(date);
            } catch (ParseException e) {
                Log.e(TAG, e.toString());
            } finally {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
            return new DatePickerDialog(
                    activity, this, year, month, day);
        }

        @Override
        // display the date on the clicked button
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int resId = bundle.getInt("resId");
            updateDisplay(activity, resId, year, month, day);
        }

        private static void updateDisplay(Activity activity, int resId, int year, int month, int day) {
            Button button = activity.findViewById(resId);
            button.setText(new StringBuilder().append(year).append("-")
                    .append(pad(month + 1)).append("-").append(pad(day)));
        }

        private static String pad(int number) {
            if (number >= 10)
                return String.valueOf(number);
            else
                return "0" + String.valueOf(number);
        }
    }



    //以下為生日日期選單(無使用)
//    private void initDateTime() {
//        Calendar calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH) + 1;
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.llDate:
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setPositiveButton("設置", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (date.length() > 0) {
//                            date.delete(0, date.length());
//                        }
//                        tvDate.setText(date.append(String.valueOf(year)).append("-").append(String.valueOf(month)).append("-").append(String.valueOf(day)));
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                final AlertDialog dialog = builder.create();
//                View dialogView = View.inflate(context, R.layout.dialog_date, null);
//                final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
//
//                dialog.setTitle("設置日期");
//                dialog.setView(dialogView);
//                dialog.show();
//                datePicker.init(year, month - 1, day, this);
//        }
//    }
//
//    @Override
//    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        this.year = year;
//        this.month = monthOfYear;
//        this.day = dayOfMonth;
//    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
}


