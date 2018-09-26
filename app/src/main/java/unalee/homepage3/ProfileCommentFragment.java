package unalee.homepage3;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProfileCommentFragment extends Fragment {
    private static final String TAG = "ProfileCommentFragment";
    private FragmentActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonTask ratingGetAllTask, ratingDeleteTask;
    private SwipeMenuRecyclerView rvRating;





    public ProfileCommentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public void onStart() {
        super.onStart();
        showAllRatings();

    }

    private void showAllRatings() {
        SharedPreferences preferences = activity.getSharedPreferences
                (Common.PREF_FILE, MODE_PRIVATE);
        int idCustomer = preferences.getInt("IdCustomer", 0);
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/RatingServlet";
            List<Rating> ratings = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllById");
            jsonObject.addProperty("IdCustomer", idCustomer);
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
                rvRating.setAdapter(new RatingRecyclerViewAdapter(activity, ratings));
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
    }



    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup parent,
                             final Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
//        rvRating.getItemAnimator().setChangeDuration(300);
//        rvRating.getItemAnimator().setMoveDuration(300);
        View view = inflater.inflate(R.layout.fragment_profile_comment, parent, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showAllRatings();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        rvRating = view.findViewById(R.id.rvRatings);
        rvRating.setLayoutManager(new LinearLayoutManager(activity));
//        rvRating.setSwipeMenuCreator(new SwipeMenuCreator() {
//            @Override
//            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
//                if (viewType == RatingRecyclerViewAdapter.VIEW_TYPE_NON_STICKY){
//                    int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
//                    int height = ViewGroup.LayoutParams.MATCH_PARENT;
//
//                    SwipeMenuItem deleteItem = new SwipeMenuItem(activity)
//                            .setBackground(R.drawable.style_delete)
//                            .setImage(R.drawable.delete128)
//                            .setText("Delete")
//                            .setTextColor(Color.WHITE)
//                            .setTextSize(16)
//                            .setWeight(width)
//                            .setHeight(height);
//                    swipeRightMenu.addMenuItem(deleteItem);
//
//                }
//            }
//        });

//        rvRating.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
//            @Override
//            public void onItemClick(SwipeMenuBridge menuBridge) {
//
//                int idCustomer = ;
//
//            }
//        });


////刪除留言
//        FloatingActionButton btCommentAdd = view.findViewById(R.id.btCommentAdd);
//        btCommentAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(activity, RatingActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;



    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        activity.getMenuInflater().inflate(R.menu.all_activity_menu, menu);
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            activity.finish();
//        } else if (item.getItemId() == R.id.menu_open_rv_menu) {
//            rvRating.smoothOpenRightMenu(0);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }

//    private void switchFragment(Fragment fragment) {
//        if (getFragmentManager() != null) {
//            getFragmentManager().beginTransaction().replace(R.id.pager, fragment).addToBackStack(null).commit();
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (ratingGetAllTask != null) {
            ratingGetAllTask.cancel(true);
            ratingGetAllTask = null;
        }
    }

    private class RatingRecyclerViewAdapter extends SwipeMenuRecyclerView.Adapter<RatingRecyclerViewAdapter.MyViewHolder> {

//        static final int VIEW_TYPE_NON_STICKY = R.layout.item_view_comment;
        private LayoutInflater layoutInflater;
        private List<Rating> ratings;
        private RelativeLayout layout;
        private LinearLayout llReview;
        private int opened = -1;



        RatingRecyclerViewAdapter(Context context, List<Rating> ratings) {
            this.layoutInflater = LayoutInflater.from(context);
            this.ratings = ratings;
        }


        @Override
        public MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
            View itemView = layoutInflater.inflate(R.layout.item_view_comment, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder( final MyViewHolder myViewHolder, int position) {
            final Rating rating = ratings.get(position);
            final int IdRoomReservation = rating.getIdRoomReservation();

            myViewHolder.tvIdRoomReservation.setText(String.valueOf(IdRoomReservation));
            myViewHolder.rbCardStar.setRating(rating.getRatingStar());
            myViewHolder.tvRatingOpinion.setText(rating.getOpinion());
            myViewHolder.tvRatingReview.setText(rating.getReview());

            myViewHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(activity)
                            .setTitle("SS Hotel")
                            .setMessage("刪除留言")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.e(TAG, "ibDelete onClick");
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL + "/RatingServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "delete");
                                        jsonObject.addProperty("IdRoomReservation", IdRoomReservation);
                                        int count = 0;
                                        try {
                                            ratingDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = ratingDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.msg_DeleteFail);
                                        } else {
                                            ratings.remove(rating);
                                            RatingRecyclerViewAdapter.this.notifyDataSetChanged();
                                            Common.showToast(activity, R.string.msg_DeleteSuccess);
                                        }
                                    }
                                }
                            }).show();

                }



            });

            if (position == opened) {
                llReview.setVisibility(View.VISIBLE);
            } else {
                llReview.setVisibility(View.GONE);
            }

            //展開評論
            layout.setOnClickListener(new View.OnClickListener() {
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



            layout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    Bundle bundle = new Bundle();
                    Rating rating1 = new Rating(rating.getIdRoomReservation(), rating.getRatingStar(), rating.getOpinion());
                    bundle.putSerializable("rating", rating1);
                    Intent intent = new Intent(activity, RatingSettingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return false;
                }
            });
        }


        @Override
        public int getItemCount() {
            return ratings.size();
        }

        class MyViewHolder extends SwipeMenuRecyclerView.ViewHolder {
            TextView tvRatingOpinion, tvRatingReview, tvIdRoomReservation;
            RatingBar rbCardStar;
            SwipeMenuRecyclerView rvRatings;
            ImageButton ibDelete;


            public MyViewHolder(View itemView) {
                super(itemView);
                tvRatingOpinion = itemView.findViewById(R.id.tvRatingOpinion);
                rbCardStar = itemView.findViewById(R.id.rbCardStar);
                tvRatingReview = itemView.findViewById(R.id.tvRatingReview);
                tvIdRoomReservation = itemView.findViewById(R.id.tvIdRoomReservation);
                layout = itemView.findViewById(R.id.layout);
                rvRatings = itemView.findViewById(R.id.rvRatings);
                llReview = itemView.findViewById(R.id.llReview);
                ibDelete = itemView.findViewById(R.id.ibDelete);
            }
        }
    }

}







