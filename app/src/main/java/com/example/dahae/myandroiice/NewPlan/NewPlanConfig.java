package com.example.dahae.myandroiice.NewPlan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dahae.myandroiice.R;

/**
 * Created by Dahae on 2015-08-05.
 */
public class NewPlanConfig extends Fragment {

    EditText planName;

    public NewPlanConfig() {
    }

    public static NewPlanConfig init(int pageNumber) {
        NewPlanConfig fragment2 = new NewPlanConfig();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment2.setArguments(args);
        return fragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.frag_new_plan_config, null);
        planName = (EditText) view.findViewById(R.id.planNameInput);

        return view;
    }
}
