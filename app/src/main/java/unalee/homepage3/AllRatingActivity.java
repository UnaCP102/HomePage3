package unalee.homepage3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import java.lang.reflect.Type;
import java.util.List;

public class AllRatingActivity extends AppCompatActivity {
    final static String TAG = "AllRatingActivity";
    private SwipeRefreshLayout srAllRating;
    private CommonTask ratingGetAllTask;
    private SwipeMenuRecyclerView rvAllRatings;
    private Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rating);
        activity = AllRatingActivity.this;
        findView();
        rvAllRatings.setLayoutManager(new LinearLayoutManager(activity));
        //下拉更新
        srAllRating.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srAllRating.setRefreshing(true);
                showAllRatings();
                srAllRating.setRefreshing(false);
            }
        });
    }


    private void findView() {
        srAllRating = (SwipeRefreshLayout) findViewById(R.id.srAllRating);
        rvAllRatings = (SwipeMenuRecyclerView) findViewById(R.id.rvAllRatings);
    }


    @Override
    public void onStart() {
        super.onStart();
        showAllRatings();
    }

    //由DB獲得資料
    private void showAllRatings() {
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/RatingServlet";
            List<Rating> ratings = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            ratingGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = ratingGetAllTask.execute().get();
                Type listType = new TypeToken<List<Rating>>() {
                }.getType();
                ratings = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (ratings == null || ratings.isEmpty()) {
                Common.showToast(activity, R.string.msg_NoCommentsFound);
            } else {
                rvAllRatings.setAdapter(new RatingRecyclerViewAdapter(activity, ratings));
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (ratingGetAllTask != null) {
            ratingGetAllTask.cancel(true);
            ratingGetAllTask = null;
        }
    }
}


    class RatingRecyclerViewAdapter extends SwipeMenuRecyclerView.Adapter<RatingRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Rating> ratings;
        private RelativeLayout alllRatingayout;
        private LinearLayout llAllReview;
        private int opened = -1;

        public RatingRecyclerViewAdapter(Context context, List<Rating> ratings) {
            this.layoutInflater = LayoutInflater.from(context);
            this.ratings = ratings;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View itemView = layoutInflater.inflate(R.layout.item_view_all_rating, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {
            final Rating rating = ratings.get(position);
            myViewHolder.tvAllIdRoomReservation.setText(String.valueOf(rating.getIdRoomReservation()));
            myViewHolder.rbAllCardStar.setRating(rating.getRatingStar());
            myViewHolder.tvAllRatingOpinion.setText(rating.getOpinion());
            myViewHolder.tvAllRatingReview.setText(rating.getReview());
            myViewHolder.tvRatingName.setText(rating.getName());
            myViewHolder.tvRatingTime.setText(rating.getTime());

            if (position == opened) {
                llAllReview.setVisibility(View.VISIBLE);
            } else {
                llAllReview.setVisibility(View.GONE);
            }

            //展開評論
            alllRatingayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (opened == myViewHolder.getAdapterPosition()) {
                        opened = -1;
                        notifyItemChanged(myViewHolder.getAdapterPosition());

                    } else {
                        int oldOpened = opened;
                        opened = myViewHolder.getAdapterPosition();
                        notifyItemChanged(oldOpened);
                        notifyItemChanged(opened);
                    }
                }

            });
        }

        @Override
        public int getItemCount() {
            return ratings.size();
        }

        static class MyViewHolder extends SwipeMenuRecyclerView.ViewHolder {
            TextView tvAllRatingOpinion, tvAllRatingReview, tvAllIdRoomReservation, tvRatingName, tvRatingTime;
            RatingBar rbAllCardStar;
            private RelativeLayout alllRatingayout;
            private LinearLayout llAllReview;


            public MyViewHolder(View itemView) {
                super(itemView);
                tvAllRatingOpinion = itemView.findViewById(R.id.tvAllRatingOpinion);
                rbAllCardStar = itemView.findViewById(R.id.rbAllCardStar);
                tvAllRatingReview = itemView.findViewById(R.id.tvAllRatingReview);
                tvAllIdRoomReservation = itemView.findViewById(R.id.tvAllIdRoomReservation);
                alllRatingayout = itemView.findViewById(R.id.alllRatingayout);
                tvRatingTime = itemView.findViewById(R.id.tvRatingTime);
                llAllReview = itemView.findViewById(R.id.llAllReview);
                tvRatingName = itemView.findViewById(R.id.tvRatingName);

            }
        }
    }

