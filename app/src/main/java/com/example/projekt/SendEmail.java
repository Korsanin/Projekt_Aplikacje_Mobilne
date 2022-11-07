package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.projekt.R;
import com.google.android.material.textfield.TextInputEditText;

public class SendEmail extends AppCompatActivity {
    private String email;
    private String subject;
    private String body;
    private TextInputEditText emailInput;
    private TextInputEditText subjectInput;
    private TextInputEditText bodyInput;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        emailInput = findViewById(R.id.emailInput);
        subjectInput = findViewById(R.id.emailSubjectInput);
        bodyInput = findViewById(R.id.emailBodyInput);

        button = findViewById(R.id.sendEmail);

        button.setOnClickListener(v ->{
            email = String.valueOf(emailInput.getText());
            subject = String.valueOf(subjectInput.getText());
            body = String.valueOf(bodyInput.getText());

            sendEmail(email,subject,body);
        });
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