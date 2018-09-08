package unalee.homepage3;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.Calendar;


public class JoinActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener, View.OnClickListener {
    private static final String TAG = "JoinActivity";
    private int year, month, day;
    private LinearLayout llDate;
    private TextView tvDate;
    private StringBuffer date;
    private Context context;
    private Button btJoinCecked, btJoinCancel;
    private EditText etJoinName, etJoinEmail, etJoinPassword, etJoinReenterPassword, etJoinPhone, etJoinAddress;
    private RadioButton rbgender;
    private RadioGroup rgGroup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_join);


        context = this;
        date = new StringBuffer();
        handleView();
        initDateTime();
        llDate.setOnClickListener(this);

        btJoinCecked.setOnClickListener(new View.OnClickListener() {
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
                if (password.equals(rePassword)){
                    return;
                }else {
                    Common.showToast(context, R.string.msa_PasswordNoRight);
                }
                String gender = rbgender.getText().toString();
                String birthDay = tvDate.getText().toString();
                String phoneNo = etJoinPhone.getText().toString().trim();
                String address = etJoinAddress.getText().toString().trim();

                if (Common.networkConnected(JoinActivity.this)){
                    String url = Common.URL + "/CustomerServlet";
                    Customer customer = new Customer(0, name, phoneNo, password, gender,
                            birthDay, phoneNo, address);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "customerInsert");
                    jsonObject.addProperty("customer", new Gson().toJson(customer));
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0){
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

        btJoinCecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void handleView() {
        llDate = (LinearLayout) findViewById(R.id.llDate);
        tvDate = (TextView) findViewById(R.id.tvDate);
        btJoinCancel = (Button) findViewById(R.id.btJoinCancel);
        btJoinCecked = (Button) findViewById(R.id.btJoinCecked);
        etJoinName = (EditText) findViewById(R.id.etJoinName);
        etJoinEmail = (EditText) findViewById(R.id.etJoinEmail);
        etJoinPassword = (EditText) findViewById(R.id.etJoinPassword);
        etJoinReenterPassword = (EditText) findViewById(R.id.etJoinReenterPassword);
        etJoinPhone = (EditText) findViewById(R.id.etJoinPhone);
        etJoinAddress = (EditText) findViewById(R.id.etJoinAddress);
        rbgender = (RadioButton) findViewById(rgGroup.getCheckedRadioButtonId());
        rgGroup = (RadioGroup) findViewById(R.id.rgGender);

    }


    //以下為生日日期選單
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDate:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("設置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) {
                            date.delete(0, date.length());
                        }
                        tvDate.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("設置日期");
                dialog.setView(dialogView);
                dialog.show();
                datePicker.init(year, month - 1, day, this);
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


