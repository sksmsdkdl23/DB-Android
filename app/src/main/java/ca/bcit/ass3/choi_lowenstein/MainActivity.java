package ca.bcit.ass3.choi_lowenstein;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {
    private MyEventDBHelper myDb;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new MyEventDBHelper(this);

        final ListView eventList = (ListView) findViewById(R.id.event_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Potluck> potlucks = getEvents();

        PotluckAdapter adapter = new PotluckAdapter(MainActivity.this, potlucks);

        eventList.setAdapter(adapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView name =  (TextView) view.findViewById(R.id.name);
                final String eventName = name.getText().toString();
                System.out.println(eventName);

                TextView date =  (TextView) view.findViewById(R.id.date);
                final String eventDate = date.getText().toString();

                TextView time =  (TextView) view.findViewById(R.id.time);
                final String eventTime = time.getText().toString();

                final AlertDialog.Builder addDialog = new AlertDialog.Builder(MainActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.delete_or_viewdetail, null);

                Button edit = (Button) view1.findViewById(R.id.editEvent);
                Button delete = (Button) view1.findViewById(R.id.deleteEvent);
                Button viewDetail = (Button) view1.findViewById(R.id.viewDetail);
                addDialog.setView(view1);
                final AlertDialog dialog = addDialog.create();
                dialog.show();

                viewDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, EventDetailActivity.class);
                        i.putExtra("name", eventName);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder addDialogBox = new AlertDialog.Builder(MainActivity.this);
                        View editView = getLayoutInflater().inflate(R.layout.edit_event_layout, null);
                        final EditText event_name = (EditText) editView.findViewById(R.id.event_name);
                        final EditText event_date = (EditText) editView.findViewById(R.id.event_date);
                        final EditText event_time = (EditText) editView.findViewById(R.id.event_time);
                        dialog.dismiss();
                        event_name.setText(eventName);
                        event_date.setText(eventDate);
                        event_time.setText(eventTime);

                        Button save = (Button) editView.findViewById(R.id.save_button);
                        addDialogBox.setView(editView);
                        final AlertDialog dialogBox = addDialogBox.create();
                        dialogBox.show();

                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!event_name.getText().toString().isEmpty() && !event_date.getText().toString().isEmpty()
                                        && !event_time.getText().toString().isEmpty()) {
                                    Potluck event = new Potluck(event_name.getText().toString(), event_date.getText().toString(),
                                            event_time.getText().toString());

                                    myDb.updateEvent(event, eventName);

                                    String msg1 = "Saved";

                                    Toast t = Toast.makeText(MainActivity.this, msg1, Toast.LENGTH_LONG);
                                    t.show();

                                    ListView eventList = (ListView) findViewById(R.id.event_list);

                                    ArrayList<Potluck> potlucks = getEvents();

                                    PotluckAdapter adapter = new PotluckAdapter(MainActivity.this, potlucks);

                                    //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
                                    eventList.setAdapter(adapter);

                                    dialogBox.dismiss();

                                } else {
                                    String msg1 = "Can't have empty field";

                                    Toast t = Toast.makeText(MainActivity.this, msg1, Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                        });
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDb.deleteEvent(eventName);
                        String msg1 = "Deleted";

                        Toast t = Toast.makeText(MainActivity.this, msg1, Toast.LENGTH_LONG);
                        t.show();

                        ListView eventList = (ListView) findViewById(R.id.event_list);

                        ArrayList<Potluck> potlucks = getEvents();

                        PotluckAdapter adapter = new PotluckAdapter(MainActivity.this, potlucks);

                        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
                        eventList.setAdapter(adapter);

                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = null;
        Toast t = null;
        switch (item.getItemId()) {
            case R.id.add:
                final AlertDialog.Builder addDialog = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.add_event_layout, null);
                final EditText event_name = (EditText) view.findViewById(R.id.event_name);
                final EditText event_date = (EditText) view.findViewById(R.id.event_date);
                final EditText event_time = (EditText) view.findViewById(R.id.event_time);
                Button save = (Button) view.findViewById(R.id.save_button);
                addDialog.setView(view);
                final AlertDialog dialog = addDialog.create();
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!event_name.getText().toString().isEmpty() && !event_date.getText().toString().isEmpty()
                                && !event_time.getText().toString().isEmpty()) {
                            Potluck event = new Potluck(event_name.getText().toString(), event_date.getText().toString(),
                                    event_time.getText().toString());

                            myDb.insertEvent(event);

                            String msg1 = "Saved";

                            Toast t = Toast.makeText(MainActivity.this, msg1, Toast.LENGTH_LONG);
                            t.show();

                            ListView eventList = (ListView) findViewById(R.id.event_list);

                            ArrayList<Potluck> potlucks = getEvents();

                            PotluckAdapter adapter = new PotluckAdapter(MainActivity.this, potlucks);

                            eventList.setAdapter(adapter);

                            dialog.dismiss();

                        } else {
                            String msg1 = "Can't have empty field";

                            Toast t = Toast.makeText(MainActivity.this, msg1, Toast.LENGTH_LONG);
                            t.show();
                        }
                    }
                });
                return true;

            case R.id.search:
                final SQLiteOpenHelper helper = new MyEventDBHelper(this);
                final AlertDialog.Builder addDialog2 = new AlertDialog.Builder(MainActivity.this);
                View view2 = getLayoutInflater().inflate(R.layout.search_dialog_layout, null);

                final EditText search_name = (EditText) view2.findViewById(R.id.searchInput);

                addDialog2.setView(view2);
                final AlertDialog dialog2 = addDialog2.create();
                dialog2.show();

                Button search = (Button) view2.findViewById(R.id.search);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!search_name.getText().toString().isEmpty()){
                            db = helper.getReadableDatabase();
                            ListView eventList = (ListView) findViewById(R.id.event_list);
                            ArrayList<Potluck> potlucks = null;
                            cursor = db.rawQuery("SELECT * FROM Event_Master WHERE Name LIKE '%" + search_name.getText().toString() + "%'", null);

                            int count = cursor.getCount();
                            potlucks = new ArrayList<>(count);

                            if (cursor.moveToFirst()) {
                                //int ndx=0;
                                do {
                                    potlucks.add(new Potluck(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                                } while (cursor.moveToNext());
                            }
                            if(potlucks.isEmpty()) {
                                String msg;
                                msg = "Nothing found";

                                Toast t = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                                t.show();
                            } else {
                                Intent i = new Intent(MainActivity.this, SearchListActivity.class);
                                i.putExtra("SearchedList", potlucks);
                                startActivity(i);
                            }
//                            PotluckAdapter adapter = new PotluckAdapter(MainActivity.this, potlucks);
//
//                            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
//                            eventList.setAdapter(adapter);
                            dialog2.dismiss();
                        } else {
                            String msg;
                            msg = "Can't be empty";

                            Toast t = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                            t.show();
                        }

                    }
                });

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cursor != null)
            cursor.close();

        if (db != null)
            db.close();
    }

    private ArrayList<Potluck> getEvents() {
        SQLiteOpenHelper helper = new MyEventDBHelper(this);
        ArrayList<Potluck> potlucks = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.rawQuery("SELECT DISTINCT * from Event_Master", null);

            int count = cursor.getCount();
            potlucks = new ArrayList<>(count);

            if (cursor.moveToFirst()) {
                //int ndx=0;
                do {
                    potlucks.add(new Potluck(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        return potlucks;
    }
}