package com.example.projekt.Fragments.MessageSystemFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;
import com.google.android.material.textfield.TextInputEditText;

public class ShareFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private View rootView;

    private String email;
    private String subject;
    private String body;
    private TextInputEditText emailInput;
    private TextInputEditText subjectInput;
    private TextInputEditText bodyInput;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_share,container,false);

        emailInput = rootView.findViewById(R.id.emailInput);
        subjectInput = rootView.findViewById(R.id.emailSubjectInput);
        bodyInput = rootView.findViewById(R.id.emailBodyInput);

        button = rootView.findViewById(R.id.sendEmail);

        button.setOnClickListener(v ->{
            email = String.valueOf(emailInput.getText());
            subject = String.valueOf(subjectInput.getText());
            body = String.valueOf(bodyInput.getText());

            sendEmail(email,subject,body);
        });

        return rootView;
    }

    public void sendEmail(String email, String subject, String body){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Wybierz metodę wysyłania: "));
    }
}
