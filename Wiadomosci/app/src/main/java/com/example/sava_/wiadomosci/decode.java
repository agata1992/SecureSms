package com.example.sava_.wiadomosci;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class decode extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.decoded);
        TextView Number;
        TextView Message;
        String number;
        String message;
        Number =(TextView) findViewById(R.id.Number);
        Message=(TextView) findViewById(R.id.message);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        number=(String) bundle.get("number");
        message=(String) bundle.get("message");
        Number.setText(number);
        Message.setText(message);
    }

    public void Decrypt(View v) {

        EditText Key;
        Key = (EditText) findViewById(R.id.key);
        String key=Key.getText().toString();
        TextView decrypted=(TextView)findViewById(R.id.decrypted);

        if (key.isEmpty()) {
            Toast.makeText(decode.this, "Wpisz klucz", Toast.LENGTH_LONG).show();

        }
        else{
            TextView Message=(TextView)findViewById(R.id.message);
            String message=Message.getText().toString();

            if(!Pattern.matches("[a-zA-Z]+", message) || message.length()%2!=0)
                Toast.makeText(decode.this, "Niepoprawna wiadomość", Toast.LENGTH_LONG).show();
            else if(!Pattern.matches("[a-zA-Z]+", key))
                Toast.makeText(decode.this, "Niepoprawny klucz", Toast.LENGTH_LONG).show();
            else {
                String message2 = Decryption(message, key);
                decrypted.setText(message2);
            }
        }
    }

    public String Decryption(String message,String key){

        String[] Alfabet=new String[]{"A","B","C","D","E","F","G","H","I","K","L","M","N","O",
                "P","Q","R","S","T","U","V","W","X","Y","Z"};
        List<String> tmp= new ArrayList<String>();
        List<String> array2= new ArrayList<String>();
        List<String> decrypted= new ArrayList<String>();
        String[][] matrix=new String[5][5];
        List<String> array= new ArrayList<String>();
        StringBuffer result;
        String tmp2;

        message=message.toUpperCase();
        message=message.replace('J','I');
        key=key.toUpperCase();
        key=key.replace('J','I');

        for(int i=0;i<key.length();i++){
            if(!tmp.contains(Character.toString(key.charAt(i))) ){
                tmp.add(Character.toString(key.charAt(i)));
            }
        }

        for(int i=0;i<25;i++){
            if(!tmp.contains(Alfabet[i])){
                tmp.add(Alfabet[i]);
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
        while(k<message.length()) {
            char x = message.charAt(k);
            if(Character.isLetter(x)) {
                array.add(Character.toString(x));
            }
            k++;
        }

        k=0;
        int ind11=0;
        int ind12=0;
        int ind21=0;
        int ind22=0;
        int spr1=0;
        int spr2=0;

        while(k<message.length()) {
            spr1=0;
            spr2=0;
            String x1 = array.get(k);
            String x2 = array.get(k + 1);

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
                if(ind11==0)
                    array2.add(matrix[4][ind12]);
                else
                    array2.add(matrix[ind11-1][ind12]);

                if(ind21==0)
                    array2.add(matrix[4][ind22]);
                else
                    array2.add(matrix[ind21-1][ind22]);
            }
            else if(ind11==ind21) {
                if(ind12==0)
                    array2.add(matrix[ind11][4]);
                else
                    array2.add(matrix[ind11][ind12-1]);
                if(ind22==0)
                    array2.add(matrix[ind21][4]);
                else
                    array2.add(matrix[ind21][ind22-1]);
            }
            else if(ind12!=ind22 && ind11!=ind21){
                array2.add(matrix[ind11][ind22]);
                array2.add(matrix[ind21][ind12]);
            }
            k+=2;
        }

        k=0;
        while(k<array2.size()){
          if(!array2.get(k).equals("X") && !array2.get(k).equals("Q")){
              decrypted.add(array2.get(k));
          }
          else{
          if(k!=0 && k!=array2.size()-1) {
              if (!array2.get(k - 1).equals(array2.get(k + 1)))
                  decrypted.add(array2.get(k));
              }
              else
                 decrypted.add(array2.get(k));

          }
        k++;
        }


        result = new StringBuffer();

        for (int i = 0; i <decrypted.size(); i++) {
            result.append( decrypted.get(i));
        }

        tmp2 = result.toString();

        return tmp2;

    }

}