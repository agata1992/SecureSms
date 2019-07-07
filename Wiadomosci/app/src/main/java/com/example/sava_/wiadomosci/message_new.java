package com.example.sava_.wiadomosci;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Pattern;

public class message_new extends AppCompatActivity {
    Switch switch1;
    EditText key;
    int on_off;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message);
        key = (EditText) findViewById(R.id.key);
        switch1 = (Switch) findViewById(R.id.switch1);
        key = (EditText) findViewById(R.id.key);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    key.setVisibility(View.VISIBLE);
                    on_off = 1;
                } else {
                    key.setVisibility(View.INVISIBLE);
                    on_off = 0;
                }
            }
        });

    }

   public void Send(View v) {

        TextView Number = (TextView) findViewById(R.id.number);
        TextView Message = (TextView) findViewById(R.id.message);
        String number = Number.getText().toString();
        String message = Message.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();



        if(!number.isEmpty()) {
            if (!message.isEmpty()) {
                if (on_off == 1) {
                    String key1 = key.getText().toString();
                    if (key1.isEmpty())
                        Toast.makeText(message_new.this, "Wpisz klucz", Toast.LENGTH_LONG).show();
                    else {
                        if (!Pattern.matches("[a-zA-Z]+", key1))
                            Toast.makeText(message_new.this, "Niepoprawny klucz", Toast.LENGTH_LONG).show();
                        else {

                            message m = new message();
                            String message2 = m.Encryption(key1, message);
                            smsManager.sendTextMessage(number, null, message2, null, null);
                            Message.setText("");
                            key.setText("");
                            Number.setText("");
                        }
                    }
                } else {

                    smsManager.sendTextMessage(number, null, message, null, null);
                    Message.setText("");
                    Number.setText("");
                }
            } else
                Toast.makeText(message_new.this, "Wpisz wiadomość", Toast.LENGTH_LONG).show();
        }
       else
            Toast.makeText(message_new.this, "Wpisz numer", Toast.LENGTH_LONG).show();
    }

}
