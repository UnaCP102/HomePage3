package unalee.homepage3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EmployeeHomeActivity extends AppCompatActivity {
    private Window window;
    private LinearLayout employHomeBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_employee_home);
        initialization();
        insertDepartmentButton();
    }

    private boolean isLoggedIn() {
        // 以後判斷登入用
        return true;
    }

    private void initialization() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        employHomeBottom = findViewById(R.id.employHomeBottom);
        employHomeBottom.setPadding(5, 5, 5,5);
    }

    /**
     * 部門功能
     */
    private void insertDepartmentButton() {
        // 這裡要判斷部門的種類 用switch case

        // 設定按鈕尺寸/Margin
        LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(240, 240);
        param.leftMargin = 20;

        // 動態生成按鈕
        Button btn = new Button(this);
        btn.setText("工作進度");
        btn.setBackgroundColor(Color.parseColor("#F7DF96"));
        btn.setLayoutParams(param);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EmployeeHomeActivity.this, "工作進度", Toast.LENGTH_SHORT).show();
            }
        });
        employHomeBottom.addView(btn);

    }
}
