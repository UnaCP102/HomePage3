package unalee.homepage3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Window window;
    private EditText etEmail;
    private EditText etPassword;
    private Context context;
    private Button btLogIn, btJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btLogIn = (Button) findViewById(R.id.btLogIn);
        btJoin = (Button) findViewById(R.id.btJoin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogIn.setOnClickListener(btLogInListener);
        btJoin.setOnClickListener(btJoinListener);
        context = LoginActivity.this;
    }


    private Button.OnClickListener btLogInListener = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            String uid = etEmail.getText().toString();
            String pw = etPassword.getText().toString();
            Log.e("Button", "onClick");
            if (uid.equals("xxx") && pw.equals("1234")){
                Toast.makeText(context, "登入成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                new AlertDialog.Builder(context)
                        .setTitle("SS Hotel")
                        .setMessage("登入失敗")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    };
    private Button.OnClickListener btJoinListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, JoinActivity.class);
            startActivity(intent);
        }
    };
}
