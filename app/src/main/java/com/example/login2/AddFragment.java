package com.example.login2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    Button buttonIn;
    TextView Text_English, Text_Chinese, textView2;
    WordViewModel viewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        buttonIn = requireActivity().findViewById(R.id.buttonIn);
        Text_Chinese = requireActivity().findViewById(R.id.Text_Chinese);
        Text_English = requireActivity().findViewById(R.id.Text_English);
        viewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        buttonIn.setEnabled(false);
        Text_English.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Text_English, 0);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String english = Text_English.getText().toString().trim();
                String chinese = Text_Chinese.getText().toString().trim();
                buttonIn.setEnabled(!english.isEmpty() && !chinese.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        Text_English.addTextChangedListener(textWatcher);
        Text_Chinese.addTextChangedListener(textWatcher);
        buttonIn.setOnClickListener(v -> {
            String english = Text_English.getText().toString().trim();
            String chinese = Text_Chinese.getText().toString().trim();
            Word word = new Word(english, chinese);
            viewModel.insertWords(word);
            NavController navController = Navigation.findNavController(v);
            navController.navigateUp();
            InputMethodManager imm1 = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });
    }
}