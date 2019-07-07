package com.example.sava_.wiadomosci;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class message extends AppCompatActivity {

    EditText key;
    String number;
    int on_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);
        Switch switch1 = (Switch) findViewById(R.id.switch1);
        TextView numbertx = (TextView) findViewById(R.id.Number);
        key = (EditText) findViewById(R.id.key);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert switch1 != null;

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    key.setVisibility(View.VISIBLE);
                    on_off = 1;
                }
                else {
                    key.setVisibility(View.INVISIBLE);
                    on_off = 0;
                }
            }
        });
        number = (String) bundle.get("number");
        numbertx.setText(number);
        Messages();
    }

    public void onBackPressed() {

        Context context;
        context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    public void Messages() {
        ListView List = (ListView) findViewById(R.id.List2);
        List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
        Map<String, String> record;
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        String PhoneNumber;
        String smsDate;
        String smsBody;

        while (c.moveToNext()) {
            smsBody = c.getString(c.getColumnIndex("body"));
            PhoneNumber = c.getString(c.getColumnIndex("address"));
            smsDate = c.getString(c.getColumnIndex("date"));
            long milliseconds = Long.parseLong(smsDate);
            DateFormat formatting = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);
            String date = formatting.format(calendar.getTime());

            if (PhoneNumber.equals(number)) {
                record = new HashMap<String, String>(1);
                record.put("message", smsBody);
                record.put("date", date);
                record.put("number",PhoneNumber);
                list2.add(record);
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list2, R.layout.messages,
                new String[]{"message", "date"},
                new int[]{R.id.message, R.id.date});

        List.setAdapter(adapter);
        List.setOnItemClickListener(new ItemMessage());
        c.close();
    }

    class ItemMessage implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
            Context context;
            String n = (String) map.get("number");
            String m = (String) map.get("message");
            String message;
            String number;
            context = getApplicationContext();
                    Intent intent = new Intent(context, decode.class);
            number = n.toString();
            message= m.toString();

            intent.putExtra("number", number);
            intent.putExtra("message", message);
            startActivity(intent);
        }
    }

    public void Send(View v) {

        TextView Number = (TextView) findViewById(R.id.Number);
        TextView Message = (TextView) findViewById(R.id.sms);
        String number = Number.getText().toString();
        String message = Message.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();

         if (!message.isEmpty()) {
             if (on_off == 1) {
                 String key1 =key.getText().toString();
                if (key1.isEmpty())
                    Toast.makeText(message.this, "Wpisz klucz", Toast.LENGTH_LONG).show();
                else {
                    if(!Pattern.matches("[a-zA-Z]+", key1))
                        Toast.makeText(message.this, "Niepoprawny klucz", Toast.LENGTH_LONG).show();
                    else {
                        String message2 = Encryption(key1, message);
                        smsManager.sendTextMessage(number, null, message2, null, null);
                        Message.setText("");
                        key.setText("");
                    }
                }
           }
            else {

                smsManager.sendTextMessage(number, null, message, null, null);
                Message.setText("");
           }
        }
        else
           Toast.makeText(message.this, "Wpisz wiadomość", Toast.LENGTH_LONG).show();
    }

   public  String Encryption(String key,String sms){
       String[] Alphabet=new String[]{"A","B","C","D","E","F","G","H","I","K","L","M","N","O",
               "P","Q","R","S","T","U","V","W","X","Y","Z"};

       StringBuffer result;
       List<String> cyphertext= new ArrayList<String>();
       List<String> array= new ArrayList<String>();
       List<String> array2= new ArrayList<String>();
       List<String> tmp= new ArrayList<String>();
       String[][] matrix=new String[5][5];
       String tmp2;
       String tmp3;
       sms=sms.toUpperCase();
       sms=sms.replace('J', 'I');
       sms=sms.replace('Ł', 'L');
       sms=sms.replace('Ó', 'O');
       sms=sms.replace('Ń', 'N');
       sms=sms.replace('Ą', 'A');
       sms=sms.replace('Ę', 'E');
       sms=sms.replace('Ś', 'S');
       sms=sms.replace('Ć', 'C');
       sms=sms.replace('Ż', 'Z');
       sms=sms.replace('Ź', 'Z');

       key=key.toUpperCase();
       key=key.replace('J', 'I');

       for(int i=0;i<key.length();i++){
          if(!tmp.contains(Character.toString(key.charAt(i))) ){
             tmp.add(Character.toString(key.charAt(i)));
          }
       }

       for(int i=0;i<25;i++){
           if(!tmp.contains(Alphabet[i])){
               tmp.add(Alphabet[i]);
           }
       }

       int k=0;
       for(int i=0;i<5;i++){
          for(int j=0;j<5;j++) {
              matrix[i][j] = tmp.get(k);
              k++;
          }
       }

       k=0;
       while(k<sms.length()) {
           char x = sms.charAt(k);
           if(Character.isLetter(x)) {
               tmp3 = Character.toString(x);
               array.add(tmp3);
           }
           k++;
       }

       k=0;
       while(k<array.size()){
           array2.add(array.get(k));
           if(k+1<array.size()) {
               if (array.get(k).equals(array.get(k + 1))) {
                   if (array.get(k) != "X")
                       array2.add("X");
                   else
                       array2.add("Q");

                   k++;
               }
               else {
                   array2.add(array.get(k + 1));
                   k += 2;
               }
           }
           else
               k++;
       }

       if((array2.size())%2!=0){
           if(!array2.get(array2.size()-1).equals("X"))
               array2.add("X");
           else
               array2.add("Q");
       }
       k=0;
       int ind11=0;
       int ind12=0;
       int ind21=0;
       int ind22=0;
       int spr1=0;
       int spr2=0;

       while(k<array2.size()) {
           spr1=0;
           spr2=0;
           String x1 = array2.get(k);
           String x2 = array2.get(k + 1);

           for (int i = 0; i < 5; i++) {
               for (int j = 0; j < 5; j++) {
                   if (matrix[i][j].equals(x1)) {

                       ind11 = i;
                       ind12 = j;
                       spr1 = 1;
                   }
                   if (matrix[i][j].equals(x2)) {
                       ind21 = i;
                       ind22 = j;
                       spr2 = 1;
                   }

                   if (spr1 == 1 && spr2 == 1)
                      i=j=5;
               }
           }
           if(ind12==ind22) {
               if(ind11==4)
                   cyphertext.add(matrix[0][ind12]);
               else
                   cyphertext.add(matrix[ind11+1][ind12]);

               if(ind21==4)
                   cyphertext.add(matrix[0][ind22]);
               else
                   cyphertext.add(matrix[ind21+1][ind22]);

           }
           else if(ind11==ind21) {
               if(ind12==4)
                   cyphertext.add(matrix[ind11][0]);
               else
                   cyphertext.add(matrix[ind11][ind12+1]);
               if(ind22==4)
                   cyphertext.add(matrix[ind21][0]);
               else
                   cyphertext.add(matrix[ind21][ind22+1]);
           }
           else if(ind12!=ind22 && ind11!=ind21){
               cyphertext.add(matrix[ind11][ind22]);
               cyphertext.add(matrix[ind21][ind12]);
           }
           k+=2;
       }

       result = new StringBuffer();
       for (int i = 0; i <cyphertext.size(); i++) {
           result.append( cyphertext.get(i));
       }
       tmp2 = result.toString();
       if(cyphertext.size()==0)
           tmp2=" ";

       return tmp2;
   }
}




