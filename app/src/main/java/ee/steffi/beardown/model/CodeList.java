package ee.steffi.beardown.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import ee.steffi.beardown.R;
import ee.steffi.beardown.db.Contracts;
import ee.steffi.beardown.db.DatabaseHelper;

/**
 * Created by rain on 4/13/16.
 */
public class CodeList implements Serializable {

    private static final int PIN_SAVE_SUCCESS = 1;
    private static final int PIN_SAVE_FAIL = 0;


    private ArrayList<PinCode> codes;

    public CodeList() {
        super();
        this.codes = new ArrayList<PinCode>();

    }

    public int saveCode(String pin) {

        int status;

        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0; i < codes.size(); i++) {
            temp.add(codes.get(i).getPinCode());
        }

        if(temp.contains(pin) == false) {
            PinCode c = new PinCode(pin);
            codes.add(c);
            status = PIN_SAVE_SUCCESS;
        }
        else {
            status = PIN_SAVE_FAIL;
        }

        return status;
    }

    public void deleteCode(String pin) {

        for(int i = 0; i < codes.size(); i++) {
            if(codes.get(i).getPinCode().equalsIgnoreCase(pin)) {
                codes.remove(i);
            }
        }
    }

    public int getSize() {

        return codes.size();
    }

    public ArrayList<String> codesToString() {

        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0; i < codes.size(); i++) {
            temp.add(codes.get(i).getPinCode());
        }

        return temp;
    }

    public void readPinCodes(DatabaseHelper helper) {

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                Contracts.PinEntry.COL_CODE
        };

        String sortOrder = Contracts.PinEntry.COL_CODE + " ASC";

        Cursor cursor = db.query(
                Contracts.PinEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
            PinCode code = new PinCode(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.PinEntry.COL_CODE)));

            codes.add(code);
        }
    }

    public void clear(DatabaseHelper helper) {

        SQLiteDatabase db = helper.getReadableDatabase();
        helper.onUpgrade(db, 1, 2);
    }
}
