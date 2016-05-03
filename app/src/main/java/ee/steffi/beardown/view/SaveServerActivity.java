package ee.steffi.beardown.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ee.steffi.beardown.R;
import ee.steffi.beardown.db.Contracts;
import ee.steffi.beardown.db.DatabaseHelper;
import ee.steffi.beardown.model.CodeList;
import ee.steffi.beardown.model.ServerList;
import ee.steffi.beardown.model.ValueObject;

public class SaveServerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String SERVERS = "SERVERS";
    private static final String CODES = "CODE_LIST";
    private static final String VALUE_OBJECT = "VALUE_OBJ";

    private Spinner select_servers;
    private ArrayAdapter adapter;
    private EditText server_URL;
    private Cursor cursor;

    private CodeList codes;
    private ServerList servers;
    private ValueObject v_object;
    private DatabaseHelper myDataBaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_server);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String action = intent.getAction();

        if(Intent.ACTION_SEND.equals(action)) {
            Bundle extras = intent.getExtras();
            servers = (ServerList) extras.get(SERVERS);
            codes = (CodeList) extras.get(CODES);
            v_object = (ValueObject) extras.get(VALUE_OBJECT);
        }

        server_URL = (EditText)findViewById(R.id.new_server_address);
        select_servers = (Spinner)findViewById(R.id.spinner_servers);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, servers.getServers());

        myDataBaseHelper = new DatabaseHelper(getApplicationContext());
        db = myDataBaseHelper.getReadableDatabase();

        String[] projection = {
                Contracts.ServerEntry.COL_URL
        };

        String sortOrder = Contracts.ServerEntry.COL_URL + " ASC";

        cursor = db.query(
                Contracts.ServerEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        for(cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
            servers.saveServer(cursor.getString(cursor.getColumnIndexOrThrow(Contracts.ServerEntry.COL_URL)), myDataBaseHelper);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, servers.getServers());
        select_servers.setOnItemSelectedListener(this);
        select_servers.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_servers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_ready) {

            String url = server_URL.getText().toString();
            if(url.length() > 0) {

                long newRowID = servers.saveServer(url, myDataBaseHelper);

                ContentValues values = new ContentValues();
                values.put(Contracts.ServerEntry.COL_URL, url);


                if(newRowID != -1) {
                    Intent intent = new Intent(this, SaveServerActivity.class);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(SERVERS, servers);
                    intent.putExtra(CODES, codes);
                    intent.putExtra(VALUE_OBJECT, v_object);
                    startActivity(intent);

                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_server_saved), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        else if(id == R.id.action_done) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(SERVERS, servers);
            intent.putExtra(CODES, codes);
            intent.putExtra(VALUE_OBJECT, v_object);
            startActivity(intent);

            Toast toast = Toast.makeText(getApplicationContext(), servers.getActive(), Toast.LENGTH_SHORT);
            toast.show();
        }

        else if(id == R.id.action_delete_servers) {

            servers.deleteServers(myDataBaseHelper);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(SERVERS, servers);
            intent.putExtra(CODES, codes);
            intent.putExtra(VALUE_OBJECT, v_object);
            startActivity(intent);

            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_servers_deleted), Toast.LENGTH_SHORT);
            toast.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        servers.setActive((String) parent.getItemAtPosition(position));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
