package unalee.homepage3;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class RatingActivity extends AppCompatActivity{
    final static String TAG = "CommentActivity";
    private Button btCommentCancel, btCommentOK;
    private EditText etOpinion;
    private RatingBar ratingBar;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_comment);

        findView();
    }


    private void findView() {
        btCommentCancel = (Button) findViewById(R.id.btCommentCancel);
        btCommentOK = (Button) findViewById(R.id.btCommentOK);
        etOpinion = (EditText) findViewById(R.id.etOpinion);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        context = RatingActivity.this;


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            }

        });

        btCommentOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0) {
                    new AlertDialog.Builder(context)
                            .setTitle("SS Hotel")
                            .setMessage("請給予分數")
                            .setPositiveButton("確定", null)
                            .show();
                    return;
                }

                //獲得點擊確認後之時間
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                String time = dateFormat.format(date);

                Float ratingStar = ratingBar.getRating();
                String opinion = etOpinion.getText().toString();


                if (Common.networkConnected(RatingActivity.this)){
                    String url = Common.URL +  "/RatingServlet";
                    Rating comment = new Rating(0, ratingStar, time, opinion, "", 1);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "ratingInsert");
                    jsonObject.addProperty("rating", new Gson().toJson(comment));
                    int count =0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0){
                        Common.showToast(context, R.string.msg_InsertFail);
                    }else {
                        Toast.makeText(context, "評論已送出，感謝您的支持。", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Common.showToast(context, R.string.msg_NoNetwork);
                }
                finish();
            }
        });

        btCommentCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                ratingBar.setRating(0);
                etOpinion.setText("");
                Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                finish();
            }

        });

    }
}