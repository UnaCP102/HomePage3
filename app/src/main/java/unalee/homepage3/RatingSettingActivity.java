package unalee.homepage3;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

public class RatingSettingActivity extends AppCompatActivity {
    final static String TAG = "RatingSettingActivity";
    private Button btCommentSettingCancel, btCommentSettingOK;
    private EditText etCommentSettingOpinion;
    private RatingBar rbCommentSettingRating;
    private Context context;
    private CommonTask ratingSettingTask;
    private SharedPreferences preferences;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_comment_setting);
        activity = RatingSettingActivity.this;

        findview();
        fillprofile();
    }

    private void findview() {
        btCommentSettingCancel = (Button) findViewById(R.id.btCommentSettingCancel);
        btCommentSettingOK = (Button) findViewById(R.id.btCommentSettingOK);
        etCommentSettingOpinion = (EditText) findViewById(R.id.etCommentSettingOpinion);
        rbCommentSettingRating = (RatingBar) findViewById(R.id.rbCommentSettingRating);

    }

    protected void onStart() {
        super.onStart();


        preferences = activity.getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
    }

    private void fillprofile() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final Object object = bundle.getSerializable("rating");
            if (object != null) {
                final Rating rating = (Rating) object;

                float ratingStar = rating.getRatingStar();
                String opinion = rating.getOpinion();

                rbCommentSettingRating.setRating(ratingStar);
                etCommentSettingOpinion.setText(opinion);

                btCommentSettingOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float ratingStar = rbCommentSettingRating.getRating();
                        String opinion = etCommentSettingOpinion.getText().toString();
                        int IdRoomReservation = rating.getIdRoomReservation();
                        if (Common.networkConnected(RatingSettingActivity.this)) {

                            String url = Common.URL + "/RatingServlet";
                            Rating comment = new Rating(IdRoomReservation, ratingStar, opinion);
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("action", "updateOpinion");
                            jsonObject.addProperty("rating", new Gson().toJson(comment));
                            int count = 0;
                            String result = null;
                            try {
                                result = new CommonTask(url, jsonObject.toString()).execute().get();
                                count = Integer.valueOf(result);
                            } catch (Exception e) {
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


                btCommentSettingCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setResult(4);
                        finish();
                    }
                });
            }
        }
    }
}
