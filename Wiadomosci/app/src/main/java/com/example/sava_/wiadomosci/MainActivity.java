package com.example.sava_.wiadomosci;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView List1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityVisible_Main=true;
        Inbox();
    }

    public void onResume() {
        super.onResume();
        activityVisible_Main = true;
        Inbox();
    }

    public void onStart() {
        super.onStart();
        activityVisible_Main = true;
        Inbox();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.message_new:
            {
                Intent intent = new Intent(this, message_new.class);
                startActivity(intent);
           }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onBackPressed() {

        moveTaskToBack(true);
        Inbox();
    }

   public void Inbox() {
        activityVisible_Main=true;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<String> list2 = new ArrayList<String>();
        Map<String, String> record;
        Uri uri = Uri.parse("content://sms/inbox");
        String PhoneNumber = null;
        String smsDate;
        Cursor c;
        List1 = (ListView) findViewById(R.id.Lista1);
        c= getContentResolver().query(uri, null, null, null, null);

        if (c.getCount() > 0) {

            while (c.moveToNext()) {

                PhoneNumber = c.getString(c.getColumnIndex("address"));
                smsDate = c.getString(c.getColumnIndex("date"));
                long milliseconds = Long.parseLong(smsDate);
                DateFormat formatting = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(milliseconds);
                String date = formatting.format(calendar.getTime());

                if (!list2.contains(PhoneNumber)) {
                    record = new HashMap<String, String>(1);
                    record.put("number", PhoneNumber);
                    record.put("date", date);
                    list.add(record);
                    list2.add(PhoneNumber);
                }
            }

            SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.conversation,
                    new String[]{"number", "date"},
                    new int[]{R.id.Number, R.id.Date});

            List1.setAdapter(adapter);
            List1.setOnItemClickListener(new ItemConv());
        }
        c.close();
    }

    class ItemConv implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
            Context context;
            Intent intent;
            String number;
            String n;
            n= (String) map.get("number");
            context = getApplicationContext();
            intent = new Intent(context, message.class);
            number = n.toString();
            intent.putExtra("number", number);
            startActivity(intent);
        }
    }

    public  void onPaused() {

        super.onPause();
        activityVisible_Main = false;
    }

    public  void onStop() {

        super.onStop();
        activityVisible_Main = false;
    }

    public static boolean isActivityVisible() {

        return activityVisible_Main;
    }

    private static boolean activityVisible_Main;

}