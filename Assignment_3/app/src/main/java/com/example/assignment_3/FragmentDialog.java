package com.example.assignment_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class FragmentDialog extends DialogFragment {

    public interface DialogClickListener {
        void dialogListenerWithQuestion(String nQuestion);
        void dialogListenerWithCancel();
    }

    public DialogClickListener listener;

    public static final String Tag = "tag";
    private static final String ARG_PARAM1 = "param1";
    private String dialogTitle;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("Listener", (Serializable) listener);
        outState.putString("Tag", Tag);
        outState.putString("argumentParam1", ARG_PARAM1);
        outState.putString("Title", dialogTitle);
        super.onSaveInstanceState(outState);
    }

    

    public static FragmentDialog newInstance(String param1) {
        FragmentDialog fragment = new FragmentDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            dialogTitle = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment, container, false);

        TextView textView = v.findViewById(R.id.dialog_Fragment_Title);
        EditText questionText = v.findViewById(R.id.edtTextNumberOfQuestion);
        Button ok = v.findViewById(R.id.btnFragmentOK);
        Button cancel = v.findViewById(R.id.btnFragmentCANCEL);

        textView.setText(dialogTitle);

        ok.setOnClickListener(view -> {
            if(!questionText.getText().toString().isEmpty()){
                if(Integer.parseInt(questionText.getText().toString()) > 0 && Integer.parseInt(questionText.getText().toString()) <= 8){
                    listener.dialogListenerWithQuestion(questionText.getText().toString());
                    dismiss();
                }
                else{
                    Toast.makeText(getContext(), R.string.questionSelectionFragmentError1, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getContext(), R.string.questionSelectionFragmentError2, Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(view -> {
            listener.dialogListenerWithCancel();
            dismiss();
        });

        return v;
    }
}
