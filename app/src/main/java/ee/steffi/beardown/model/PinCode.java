package ee.steffi.beardown.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import ee.steffi.beardown.db.Contracts;
import ee.steffi.beardown.db.DatabaseHelper;

/**
 * Created by rain on 4/12/16.
 */
public class PinCode implements Serializable {

    private String code;

    public PinCode(String pin) {
        super();
        this.code = pin;
    }

    public String getPinCode() {

        return code;
    }

    public long save(DatabaseHelper helper, CodeList codes) {

        if(this.code.length() == 4) {
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Contracts.PinEntry.COL_CODE, this.code);

            long newRowID;

            newRowID = db.insertWithOnConflict(
                    Contracts.PinEntry.TABLE_NAME,
                    Contracts.PinEntry.COL_CODE,
                    values,
                    SQLiteDatabase.CONFLICT_IGNORE
            );

            if(newRowID != -1) {
                codes.saveCode(code);
            }

            return newRowID;
        }
        else {
            return -1;
        }

    }

    public int delete(DatabaseHelper helper, CodeList codes) {

        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = Contracts.PinEntry.COL_CODE + "=?";
        String[] args = {this.code};
        int affectedRows = db.delete(Contracts.PinEntry.TABLE_NAME, selection, args);

        if(affectedRows != 0) {
            codes.deleteCode(code);
        }

        return affectedRows;

    }

}
