package unalee.homepage3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileReceiptFragment extends Fragment {
    public ProfileReceiptFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile_receipt, container, false);
        List<ProfileReceiptDetail> newsList = new ArrayList<>();
        newsList.add(new ProfileReceiptDetail("Title 1", "Detail 1"));
        newsList.add(new ProfileReceiptDetail("Title 2", "Detail 2"));
        newsList.add(new ProfileReceiptDetail("Title 3", "Detail 3"));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ReceiptAdapter(inflater, newsList));
        return view;
    }

    private class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private List<ProfileReceiptDetail> receiptList;
        private View visibleView;

        public ReceiptAdapter(LayoutInflater inflater, List<ProfileReceiptDetail> receiptList) {
            this.inflater = inflater;
            this.receiptList = receiptList;
        }

        @Override
        public int getItemCount() {
            return receiptList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvTitle, tvDetail;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = inflater.inflate(R.layout.receipt_item, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            ProfileReceiptDetail profileReceiptDetail = receiptList.get(position);
            viewHolder.tvTitle.setText(profileReceiptDetail.getTitle());
            viewHolder.tvDetail.setText(profileReceiptDetail.getDetail());
            viewHolder.tvTitle.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    switch (viewHolder.tvDetail.getVisibility()){
                        case View.VISIBLE:
                            viewHolder.tvDetail.setVisibility(View.GONE);
                            break;
                        case View.GONE:
                            if(visibleView != null){
                                viewHolder.tvDetail.setVisibility(View.GONE);
                            }
                            viewHolder.tvDetail.setVisibility(View.VISIBLE);
                            visibleView = viewHolder.tvDetail;
                            break;
                    }
                }
            });
        }
    }
}