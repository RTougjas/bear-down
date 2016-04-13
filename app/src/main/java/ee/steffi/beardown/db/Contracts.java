package ee.steffi.beardown.db;

import android.provider.BaseColumns;

/**
 * Created by rain on 4/13/16.
 */
public class Contracts {

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA = ",";

    public Contracts() {

    }

    public static abstract class ServerEntry implements BaseColumns {

        public static final String TABLE_NAME = "Server";
        public static final String COL_URL = "ServerURL";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_URL + TEXT_TYPE + " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_DELETE_DATA =
                "DELETE FROM " + TABLE_NAME;
    }

    public static abstract class PinEntry implements BaseColumns {

        public static final String TABLE_NAME = "PinCode";
        public static final String COL_CODE = "code";
        public static final String COL_ACTIVE = "active";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_CODE + TEXT_TYPE + COMMA +
                        COL_ACTIVE + " INTEGER" + COMMA +
                        "UNIQUE(" + COL_CODE + ")" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_DELETE_DATA =
                "DELETE FROM " + TABLE_NAME;
    }
}
