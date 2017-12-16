package ca.bcit.ass3.choi_lowenstein;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {
    private MyEventDBHelper myDb;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor cursor2;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        myDb = new MyEventDBHelper(this);

        eventName = (String) getIntent().getExtras().get("name");
        final ListView detailList = (ListView) findViewById(R.id.detail_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<EventDetail> details = getEventDetail();

        EventDetailAdapter adapter = new EventDetailAdapter(EventDetailActivity.this, details);

        detailList.setAdapter(adapter);

        detailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView name = (TextView) view.findViewById(R.id.item_Name);
                final String itemName = name.getText().toString();

                TextView unit = (TextView) view.findViewById(R.id.item_Unit);
                final String itemUnit = unit.getText().toString();

                TextView quantity = (TextView) view.findViewById(R.id.item_Quantity);
                final String itemQuantity = quantity.getText().toString();

                final AlertDialog.Builder addDialog = new AlertDialog.Builder(EventDetailActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.delete_or_viewdetail, null);

                Button edit = (Button) view1.findViewById(R.id.editEvent);
                Button viewDetail = (Button) view1.findViewById(R.id.viewDetail);
                viewDetail.setVisibility(View.INVISIBLE);
                final Button delete = (Button) view1.findViewById(R.id.deleteEvent);
                addDialog.setView(view1);
                final AlertDialog dialog = addDialog.create();
                dialog.show();

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        final AlertDialog.Builder addDialogBox = new AlertDialog.Builder(EventDetailActivity.this);
                        View editView = getLayoutInflater().inflate(R.layout.edit_item_layout, null);
                        final EditText item_name = (EditText) editView.findViewById(R.id.item_Name);
                        final EditText item_unit = (EditText) editView.findViewById(R.id.item_Unit);
                        final EditText item_quantity = (EditText) editView.findViewById(R.id.item_Quantity);

                        item_name.setText(itemName);
                        item_unit.setText(itemUnit);
                        item_quantity.setText(itemQuantity);

                        Button save = (Button) editView.findViewById(R.id.save_button);
                        addDialogBox.setView(editView);
                        final AlertDialog dialogBox = addDialogBox.create();
                        dialogBox.show();

                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!item_name.getText().toString().isEmpty() && !item_unit.getText().toString().isEmpty()
                                        && !item_quantity.getText().toString().isEmpty()) {
                                    EventDetail detail = new EventDetail(item_name.getText().toString(), item_unit.getText().toString(),
                                            item_quantity.getText().toString());

                                    myDb.updateDetail(detail, itemName, eventName);

                                    String msg1 = "Saved";

                                    Toast t = Toast.makeText(EventDetailActivity.this, msg1, Toast.LENGTH_LONG);
                                    t.show();

                                    ArrayList<EventDetail> details1 = getEventDetail();

                                    EventDetailAdapter adapter = new EventDetailAdapter(EventDetailActivity.this, details1);

                                    //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
                                    detailList.setAdapter(adapter);

                                    dialogBox.dismiss();

                                } else {
                                    String msg1 = "Can't have empty field";

                                    Toast t = Toast.makeText(EventDetailActivity.this, msg1, Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                        });
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        myDb.deleteDetail(itemName, eventName);

                        String msg1 = "Deleted";

                        Toast t = Toast.makeText(EventDetailActivity.this, msg1, Toast.LENGTH_LONG);
                        t.show();

                        ListView detailList = (ListView) findViewById(R.id.detail_list);

                        ArrayList<EventDetail> details2 = getEventDetail();

                        EventDetailAdapter adapter = new EventDetailAdapter(EventDetailActivity.this, details2);

                        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
                        detailList.setAdapter(adapter);

                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = null;
        Toast t = null;
        switch (item.getItemId()) {
            case R.id.add:
                final AlertDialog.Builder addDialog = new AlertDialog.Builder(EventDetailActivity.this);
                View view = getLayoutInflater().inflate(R.layout.add_detail_layout, null);
                final EditText item_name = (EditText) view.findViewById(R.id.itemName);
                final EditText item_unit = (EditText) view.findViewById(R.id.itemUnit);
                final EditText item_quantity = (EditText) view.findViewById(R.id.itemQuantity);
                Button save = (Button) view.findViewById(R.id.save_button);
                addDialog.setView(view);
                final AlertDialog dialog = addDialog.create();
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!item_name.getText().toString().isEmpty() && !item_unit.getText().toString().isEmpty()
                                && !item_quantity.getText().toString().isEmpty()) {
                            EventDetail detail = new EventDetail(item_name.getText().toString(), item_unit.getText().toString(),
                                    item_quantity.getText().toString());

                            myDb.insertDetail(detail, eventName);

                            String msg1 = "Saved";

                            Toast t = Toast.makeText(EventDetailActivity.this, msg1, Toast.LENGTH_LONG);
                            t.show();

                            ListView detailList = (ListView) findViewById(R.id.detail_list);

                            ArrayList<EventDetail> details = getEventDetail();

                            EventDetailAdapter adapter = new EventDetailAdapter(EventDetailActivity.this, details);

                            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
                            detailList.setAdapter(adapter);

                            dialog.dismiss();

                        } else {
                            String msg1 = "Can't have empty field";

                            Toast t = Toast.makeText(EventDetailActivity.this, msg1, Toast.LENGTH_LONG);
                            t.show();
                        }
                    }
                });
                return true;

            case R.id.search:
                msg = "Search Function Unavailable For Items";

                t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
                t.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cursor != null)
            cursor.close();
        if (cursor2 != null) {
            cursor2.close();
        }
        if (db != null)
            db.close();
    }

    private ArrayList<EventDetail> getEventDetail() {
        SQLiteOpenHelper helper = new MyEventDBHelper(this);
        ArrayList<EventDetail> eventDetail = null;
        try {
            db = helper.getReadableDatabase();
            int id = 0;
            cursor = db.rawQuery("SELECT DISTINCT _id FROM Event_Master WHERE name = '" + eventName + "'", null);
            System.out.println(eventName);
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(0));
            }

            cursor2 = db.rawQuery("SELECT * from Event_Detail WHERE _eventId = '" + id + "'", null);
            int count = cursor2.getCount();
            eventDetail = new ArrayList<>(count);

            if (cursor2.moveToFirst()) {
                //int ndx=0;
                do {
                    eventDetail.add(new EventDetail(cursor2.getString(1), cursor2.getString(2), cursor2.getString(3)));
                } while (cursor2.moveToNext());
            }
        } catch (SQLiteException sqlex) {
            String msg = "[EventDetailActivity / getEventDetail] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        return eventDetail;
    }
}