package ee.steffi.beardown.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import ee.steffi.beardown.db.Contracts;
import ee.steffi.beardown.db.DatabaseHelper;


/**
 * Created by rain on 4/18/16.
 */
public class ServerList implements Serializable{

    private ArrayList<String> servers;

    private String active;

    public ServerList() {
        super();
        servers = new ArrayList<String>();
        this.active = null;
    }

    public long saveServer(String url, DatabaseHelper helper) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contracts.ServerEntry.COL_URL, url);

        long newRowID;

        newRowID = db.insertWithOnConflict(
                Contracts.ServerEntry.TABLE_NAME,
                Contracts.ServerEntry.COL_URL,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );

        if(newRowID != -1) {

            setActive(url);
            servers.add(url);
        }

        return newRowID;
    }

    public void readServerList(DatabaseHelper helper) {

        servers.clear();

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                Contracts.ServerEntry.COL_URL
        };

        String sortOrder = Contracts.ServerEntry.COL_URL + " ASC";

        Cursor cursor = db.query(
                Contracts.ServerEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        for(cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
            this.addServer(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.ServerEntry.COL_URL)));
        }

        setActive(servers.get(0));
    }

    public void deleteServers(DatabaseHelper helper) {

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(Contracts.ServerEntry.SQL_DELETE_DATA);

        servers.clear();
        this.active = null;

    }

    public String getActive() {

        return this.active;
    }

    public void setActive(String url) {

        String u = "http://" + url + "/";

        this.active = u;
    }

    public ArrayList<String> getServers() {

        return this.servers;
    }

    private void addServer(String url) {
        if(servers.contains(url) == false) {
            servers.add(url);
        }
    }

}
