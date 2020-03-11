package com.example.android;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfiloFragment extends Fragment {

    private onFragmentBtnSelected listner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilo_fragment, container, false);
        Button clickme = view.findViewById(R.id.load);
        clickme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onButtonSelected();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super .onAttach(context);
        if(context instanceof onFragmentBtnSelected){
            listner = (onFragmentBtnSelected) context;
        }else{
            throw new ClassCastException((context.toString() + " ciao"));
        }
    }

    public interface onFragmentBtnSelected{
        public void onButtonSelected();
    }
}
