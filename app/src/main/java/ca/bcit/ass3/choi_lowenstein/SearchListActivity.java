package ca.bcit.ass3.choi_lowenstein;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchListActivity extends AppCompatActivity implements Serializable {
    private MyEventDBHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb = new MyEventDBHelper(this);
        final ArrayList<Potluck> potlucks = (ArrayList<Potluck>) getIntent().getSerializableExtra("SearchedList");

        final ListView eventList = (ListView) findViewById(R.id.searched_list);

        PotluckAdapter adapter = new PotluckAdapter(SearchListActivity.this, potlucks);

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

                final AlertDialog.Builder addDialog = new AlertDialog.Builder(SearchListActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.delete_or_viewdetail, null);

                Button edit = (Button) view1.findViewById(R.id.editEvent);
                Button delete = (Button) view1.findViewById(R.id.deleteEvent);
                Button viewDetail = (Button) view1.findViewById(R.id.viewDetail);
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
                addDialog.setView(view1);
                final AlertDialog dialog = addDialog.create();
                dialog.show();

                viewDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(SearchListActivity.this, EventDetailActivity.class);
                        i.putExtra("name", eventName);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
