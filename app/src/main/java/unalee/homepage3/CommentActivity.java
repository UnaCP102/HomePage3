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


public class CommentActivity extends AppCompatActivity{
    final static String TAG = "CommentActivity";
    private Button btCommentCancel, btCommentOK;
    private EditText etCommentText;
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
        etCommentText = (EditText) findViewById(R.id.etCommentText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        context = CommentActivity.this;


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

                int star = ratingBar.getNumStars();
                String commentDetail = etCommentText.getText().toString();

                if (Common.networkConnected(CommentActivity.this)){
                    String url = Common.URL +  "/CustomerServlet";
                    Comment comment = new Comment(0, "", star, commentDetail);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "commentInsert");
                    jsonObject.addProperty("comment", new Gson().toJson(comment));
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
                etCommentText.setText("");
                Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
            }

        });

    }
}