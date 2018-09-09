package unalee.homepage3;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProfileCommentFragment extends Fragment {
    private static final String TAG = "ProfileCommentFragment";
    private FragmentActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonTask personComment;
    private RecyclerView rvComments;

    public ProfileCommentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();


    }


    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile_comment, parent, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showPersonComments();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        rvComments = view.findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(activity));
        FloatingActionButton btCommentAdd = view.findViewById(R.id.btCommentAdd);
        btCommentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CommentActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void showPersonComments() {
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/CommentServlet";
            List<Comment> comments = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getPersonComment");
            String jsonOut = jsonObject.toString();
            personComment = new CommonTask(url, jsonOut);
            try {
                String jsonIn = personComment.execute().get();
                Type listType = new TypeToken<List<Comment>>() {
                }.getType();
                comments = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (comments == null || comments.isEmpty()) {
                Common.showToast(activity, R.string.msg_NoCommentsFound);
            } else {
                rvComments.setAdapter(new CommentsRecyclerViewAdapter(activity, comments));
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showPersonComments();
    }

    private class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Comment> comments;

        CommentsRecyclerViewAdapter(Context context, List<Comment> comments) {
            layoutInflater = LayoutInflater.from(context);
            this.comments = comments;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvCommentDate, tvCommentDetail;
            RatingBar rbCard;

            MyViewHolder(View itemView) {
                super(itemView);
                tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
                rbCard = itemView.findViewById(R.id.rbCard);
                tvCommentDetail = itemView.findViewById(R.id.tvCommentDetail);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_comment, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Comment comment = comments.get(position);
            String url = Common.URL + "/CommentServlet";
            int commentId = comment.getCustomerId();
            myViewHolder.tvCommentDate.setText(comment.getCommentDate());
            myViewHolder.rbCard.setRating(comment.getStar());
            myViewHolder.tvCommentDetail.setText(comment.getCommentDetail());
        }
    }

    private void switchFragment(Fragment fragment) {
        if (getFragmentManager() != null){
            getFragmentManager().beginTransaction().replace(R.id.pager, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (personComment != null){
            personComment.cancel(true);
            personComment = null;
        }
    }
}

