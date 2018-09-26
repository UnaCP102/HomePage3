package unalee.homepage3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePageFragment extends Fragment {
    public static final int FUNC_LOGIN = 1;
    private Button btToEmployee, btToJoin, btToLogIn;
    private HomePageFragment context;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_customer_homepage, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        btToEmployee = (Button) getActivity().findViewById(R.id.btToEmployee);
        btToJoin = (Button) getActivity().findViewById(R.id.btToJoin);
        btToLogIn = (Button) getActivity().findViewById(R.id.btToLogIn);
        context = HomePageFragment.this;

        btToEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EmployeeHomeActivity.class);
                startActivity(intent);
            }
        });

        btToJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllRatingActivity.class);
                startActivity(intent);
            }
        });

        btToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RatingReviewActivity.class);
                startActivity(intent);
            }
        });




    }


}