package com.snur206.taskmaster.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snur206.taskmaster.R;

// TODO: 1-6: Make a Fragment class for the RecyclerView ViewHolders
    // Delete all references to Param1 and Param2
    // In the Component Tree on the left, right-click "FrameLayout" -> "Convert FrameLayout to ConstraintLayout", and then click "OK" on the dialog that comes up
public class TaskFragments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match


    public TaskFragments() {
        // Required empty public constructor
    }


    public static TaskFragments newInstance(String param1, String param2) {
        TaskFragments fragment = new TaskFragments();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_fragments, container, false);
    }
}