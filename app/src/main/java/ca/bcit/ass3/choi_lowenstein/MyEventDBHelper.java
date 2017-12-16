package ca.bcit.ass3.choi_lowenstein;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Phili on 10/31/2017.
 */

public class MyEventDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "PotLuck.db";
    private static final int DB_VERSION = 1;
    private Context context;

    public MyEventDBHelper(Context context) {
        // The 3'rd parameter (null) is an advanced feature relating to cursors
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
//        if (context.deleteDatabase(DB_NAME)) {
//            Toast.makeText(context, "database deleted", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getCreateEventTableSql());
        sqLiteDatabase.execSQL(getCreateDetailTableSql());
        init(sqLiteDatabase);
    }

    private void init(SQLiteDatabase db)
    {
        Potluck[] defaultEvent = {
                new Potluck("Halloween Party", "October 30, 2017", "6:30PM"),
                new Potluck("Christmas Party", "December 20, 2017", "12:30PM"),
                new Potluck("New Year Eve", "December 31, 2017", "8:00 PM")
        };

        EventDetail[] eventDetails = {
                new EventDetail("Coca Cola", "6 packs", "5"),
                new EventDetail("Pizza", "Large", "3"),
                new EventDetail("Potato Chips", "Large Bag", "5"),
                new EventDetail("Napkins", "Pieces", "100")
        };

        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();
        for (Potluck p : defaultEvent) {
            values.put("Name", p.getmName());
            values.put("Date", p.getmDate());
            values.put("Time", p.getmTime());
            db.insert("Event_Master", null, values);
            Cursor cursor = db.rawQuery("SELECT _id FROM Event_Master WHERE Name = '" + p.getmName() + "'", null);
            for (EventDetail e : eventDetails) {
                values2.put("ItemName", e.get_itemName());
                values2.put("ItemUnit", e.get_itemUnit());
                values2.put("ItemQuantity", e.get_itemQuantity());
                if (cursor.moveToFirst()) {
                    System.out.println(Integer.parseInt(cursor.getString(0)));
                    values2.put("_eventId", Integer.parseInt(cursor.getString(0)));
                }
                db.insert("Event_Detail", null, values2);
            }
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Event_Master");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Event_Detail");
    }

    private String getCreateEventTableSql() {
        String sql = "";
        sql += "CREATE TABLE Event_Master (";
        sql += "_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "Name TEXT, ";
        sql += "Date TEXT, ";
        sql += "Time TEXT);";

        return sql;
    }

    private String getCreateDetailTableSql() {
        String sql = "";
        sql += "CREATE TABLE Event_Detail (";
        sql += "_detailId INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "ItemName TEXT, ";
        sql += "ItemUnit TEXT, ";
        sql += "ItemQuantity TEXT, ";
        sql += "_eventId INTEGER, FOREIGN KEY(_eventId) REFERENCES Event_Master(_id));";

        return sql;
    }

    public void insertEvent(Potluck event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", event.getmName());
        values.put("Date", event.getmDate());
        values.put("Time", event.getmTime());

        db.insert("Event_Master", null, values);
    }

    public void insertDetail(EventDetail detail, String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("SELECT _id FROM Event_Master WHERE Name = '" + eventName + "'", null);
        values.put("ItemName", detail.get_itemName());
        values.put("ItemUnit", detail.get_itemUnit());
        values.put("ItemQuantity", detail.get_itemQuantity());
        if (cursor.moveToFirst()) {
            values.put("_eventId", Integer.parseInt(cursor.getString(0)));
        }
        db.insert("Event_Detail", null, values);
    }

    public void deleteEvent(String event_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        deleteItems(event_name);
        db.delete("Event_Master", "Name = ?", new String[] {event_name});
    }

    public void deleteDetail(String itemName, String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM Event_Master WHERE Name = '" + eventName + "'", null);
        if (cursor.moveToFirst()) {
            db.delete("Event_Detail", "_eventId = ? AND ItemName = ?", new String[] {cursor.getString(0), itemName});
        }
    }

    public void updateEvent(Potluck event, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM Event_Master WHERE Name = '" + name + "'", null);
        ContentValues values = new ContentValues();
        values.put("Name", event.getmName());
        values.put("Date", event.getmDate());
        values.put("Time", event.getmTime());
        if (cursor.moveToFirst()) {
            //int ndx=0;
            db.update("Event_Master", values, "_id = ?", new String[] {cursor.getString(0)});
        }
    }

    public void updateDetail(EventDetail detail, String name, String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM Event_Master WHERE name = '" + eventName + "'", null);
        int id = 0;
        if (cursor.moveToFirst()) {
            //int ndx=0;
            id = Integer.parseInt(cursor.getString(0));
        }

        cursor = db.rawQuery("SELECT _detailId FROM Event_Detail WHERE ItemName = '" + name + "'"
                + " AND _eventId = '" + id +"'" , null);
        ContentValues values = new ContentValues();
        values.put("ItemName", detail.get_itemName());
        values.put("ItemUnit", detail.get_itemUnit());
        values.put("ItemQuantity", detail.get_itemQuantity());
        if (cursor.moveToFirst()) {
            //int ndx=0;
            db.update("Event_Detail", values, "_detailId = ?", new String[] {cursor.getString(0)});
        }
    }

    private void deleteItems(String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM Event_Master WHERE Name = '" + eventName + "'", null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = Integer.parseInt(cursor.getString(0));
        }
        cursor = db.rawQuery("SELECT * FROM Event_Detail WHERE _eventId = '" + id + "'", null);
        if (cursor.moveToFirst()) {
            do {
                db.delete("Event_Detail", "_eventId = ?", new String[] {Integer.toString(id)});
            }while(cursor.moveToNext());
        }
    }
}
