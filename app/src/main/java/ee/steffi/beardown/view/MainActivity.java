package ee.steffi.beardown.view;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Vibrator;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import ee.steffi.beardown.R;
import ee.steffi.beardown.db.DatabaseHelper;
import ee.steffi.beardown.model.CodeList;
import ee.steffi.beardown.model.CustomButton;
import ee.steffi.beardown.model.PinPad;
import ee.steffi.beardown.model.ValueObject;

public class MainActivity extends AppCompatActivity {

    private static final String CODES = "CODE_LIST";
    private static final String VALUE_OBJECT = "VALUE_OBJ";

    private DatabaseHelper myDataBaseHelper;

    private PinPad pad;
    private CodeList list;
    private ValueObject v_objekt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pad = new PinPad((Switch)findViewById(R.id.switch_scramble), (Switch)findViewById(R.id.switch_save),
                (TextView)findViewById(R.id.info), (EditText)findViewById(R.id.pin_entry),
                initBackspace(), initButtons());

        Intent intent = getIntent();
        String action = intent.getAction();

        if(Intent.ACTION_SEND.equals(action)) {
            Bundle extras = intent.getExtras();
            list = (CodeList) extras.get(CODES);
            v_objekt = (ValueObject) extras.get(VALUE_OBJECT);
        }
        else {
            list = new CodeList();
            myDataBaseHelper = new DatabaseHelper(getApplicationContext());
            list.readPinCodes(myDataBaseHelper);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_new) {

            Intent intent = new Intent(this, SaveCodeActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(CODES, list);
            startActivity(intent);
        }
        if(id == R.id.action_show_codes) {

            Intent intent = new Intent(this, ShowCodesActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(CODES, list);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private CustomButton[] initButtons() {

        final CustomButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;

        button1 = new CustomButton(getApplicationContext(), 1, (ImageButton)findViewById(R.id.button1));
        button2 = new CustomButton(getApplicationContext(), 2, (ImageButton)findViewById(R.id.button2));
        button3 = new CustomButton(getApplicationContext(), 3, (ImageButton)findViewById(R.id.button3));
        button4 = new CustomButton(getApplicationContext(), 4, (ImageButton)findViewById(R.id.button4));
        button5 = new CustomButton(getApplicationContext(), 5, (ImageButton)findViewById(R.id.button5));
        button6 = new CustomButton(getApplicationContext(), 6, (ImageButton)findViewById(R.id.button6));
        button7 = new CustomButton(getApplicationContext(), 7, (ImageButton)findViewById(R.id.button7));
        button8 = new CustomButton(getApplicationContext(), 8, (ImageButton)findViewById(R.id.button8));
        button9 = new CustomButton(getApplicationContext(), 9, (ImageButton)findViewById(R.id.button9));
        button10 = new CustomButton(getApplicationContext(), 0, (ImageButton)findViewById(R.id.button10));

        button1.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button1.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button2.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button2.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button3.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button3.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button4.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button4.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button5.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button5.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button6.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button6.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button7.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button7.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button8.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button8.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button9.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button9.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        button10.getImageButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendValue(button10.getValue());
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
            }
        });

        CustomButton[] btns = new CustomButton[10];
        btns[0] = button1;
        btns[1] = button2;
        btns[2] = button3;
        btns[3] = button4;
        btns[4] = button5;
        btns[5] = button6;
        btns[6] = button7;
        btns[7] = button8;
        btns[8] = button9;
        btns[9] = button10;

        return btns;

    }

    private ImageButton initBackspace() {

        ImageButton btn_backspace = (ImageButton)findViewById(R.id.delete);

        btn_backspace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                tap.vibrate(60);
                CharSequence temp = pad.getCode();
                if (temp.length() > 0) {
                    temp = temp.subSequence(0, temp.length() - 1);
                    pad.getPinField().setText(temp);
                    pad.getPinField().setSelection(pad.getPinField().length());
                }
            }
        });

        return btn_backspace;
    }

    private void sendValue(int value) {

        int status = pad.type(value, v_objekt);

        if(status == 1) {
            pad.getInfo().setText(getResources().getString(R.string.info));

        }
        else if(status == 2) {
            pad.getInfo().setText(getResources().getString(R.string.error_incorrect_pin));
            Vibrator wrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            wrong.vibrate(200);

            System.out.println("INCORRECT");

        }
        else if(status == 3) {
            pad.getInfo().setText(getResources().getString(R.string.info_correct_pin) + "\n" + v_objekt.getTime() + "s");
            Vibrator correct = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            correct.vibrate(250);

            System.out.println("CORRECT");
            System.out.println(v_objekt.toString());
            v_objekt.reset();
        }
        else if(status == 0) {
            System.out.println("Does not count");
        }
        else if(status == 4) {
            pad.getInfo().setText(getResources().getString(R.string.error_pin_not_selected));
            Vibrator wrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            wrong.vibrate(200);
        }
    }
}