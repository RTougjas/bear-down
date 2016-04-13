package ee.steffi.beardown.view;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;

import ee.steffi.beardown.R;
import ee.steffi.beardown.db.DatabaseHelper;
import ee.steffi.beardown.model.CodeList;
import ee.steffi.beardown.model.PinCode;
import ee.steffi.beardown.model.ValueObject;

public class ShowCodesActivity extends AppCompatActivity {

    private static final String CODES = "CODE_LIST";
    private static final String VALUE_OBJECT = "VALUE_OBJ";

    private ArrayAdapter adapter;
    private ListView list_view;
    private CodeList codes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_codes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String action = intent.getAction();

        if(Intent.ACTION_SEND.equals(action)) {
            Bundle extras = intent.getExtras();
            codes = (CodeList) extras.get(CODES);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, codes.codesToString());
        list_view = (ListView)findViewById(R.id.listView);
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);
                PinCode c = new PinCode(item);
                ValueObject v_obj = new ValueObject(c);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(CODES, codes);
                intent.putExtra(VALUE_OBJECT, v_obj);
                startActivity(intent);

                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_pin_selected), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);
                PinCode c = new PinCode(item);
                c.delete(new DatabaseHelper(getApplicationContext()), codes);

                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.info_pin_deleted), Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(getApplicationContext(), ShowCodesActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(CODES, codes);
                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_codes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_back) {
            this.finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
