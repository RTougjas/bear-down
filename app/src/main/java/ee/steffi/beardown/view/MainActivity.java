package ee.steffi.beardown.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;

import ee.steffi.beardown.R;
import ee.steffi.beardown.db.DatabaseHelper;
import ee.steffi.beardown.model.ClientTask;
import ee.steffi.beardown.model.CodeList;
import ee.steffi.beardown.model.CustomButton;
import ee.steffi.beardown.model.PinPad;
import ee.steffi.beardown.model.ServerList;
import ee.steffi.beardown.model.ValueObject;

public class MainActivity extends AppCompatActivity {

    private static final String CODES = "CODE_LIST";
    private static final String VALUE_OBJECT = "VALUE_OBJ";
    private static final String SERVERS = "SERVERS";

    private DatabaseHelper myDataBaseHelper;

    private PinPad pad;
    private CodeList list;
    private ServerList servers;
    private ValueObject v_objekt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(savedInstanceState != null) {

            list = (CodeList) savedInstanceState.getSerializable(CODES);
            v_objekt = (ValueObject) savedInstanceState.getSerializable(VALUE_OBJECT);
            servers = (ServerList) savedInstanceState.getSerializable(SERVERS);

        }

        Intent intent = getIntent();
        String action = intent.getAction();
        servers = new ServerList();

        if(Intent.ACTION_SEND.equals(action)) {
            Bundle extras = intent.getExtras();
            if(extras.containsKey(CODES)) {
                list = (CodeList) extras.get(CODES);
            }
            if(extras.containsKey(VALUE_OBJECT)) {
                v_objekt = (ValueObject) extras.get(VALUE_OBJECT);
                try {

                    v_objekt.setURL(servers.getActive());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            if(extras.containsKey(SERVERS)) {
                servers = (ServerList) extras.get(SERVERS);
            }
        }
        else {
            list = new CodeList();
            servers = new ServerList();
            v_objekt = new ValueObject();

            myDataBaseHelper = new DatabaseHelper(getApplicationContext());

            list.readPinCodes(myDataBaseHelper);
            servers.readServerList(myDataBaseHelper);
        }

        pad = new PinPad((Switch)findViewById(R.id.switch_scramble), (Switch)findViewById(R.id.switch_save),
                (TextView)findViewById(R.id.info), (EditText)findViewById(R.id.pin_entry),
                initBackspace(), initButtons(), initializeConfirmButton(), v_objekt);

        if(v_objekt.getPadStatus().equals(ValueObject.PAD_RANDOM)) {
            pad.scrambleButtons();
        }

        /*
        if(v_objekt.getStatus() == ValueObject.STATUS_SUCCESS) {
            String message = getResources().getString(R.string.info_data_sent_success);
            showMessage(message);
        }
        else if(v_objekt.getStatus() == ValueObject.STATUS_FAIL) {
            String message = getResources().getString(R.string.error_data_sent_fail);
            showMessage(message);
        }
        */
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable(CODES, list);
        savedInstanceState.putSerializable(VALUE_OBJECT, v_objekt);
        savedInstanceState.putSerializable(SERVERS, servers);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        list = (CodeList) savedInstanceState.getSerializable(CODES);
        v_objekt = (ValueObject) savedInstanceState.getSerializable(VALUE_OBJECT);
        servers = (ServerList) savedInstanceState.getSerializable(SERVERS);

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
            intent.putExtra(SERVERS, servers);
            intent.putExtra(VALUE_OBJECT, v_objekt);
            startActivity(intent);
        }
        if(id == R.id.action_show_codes) {

            Intent intent = new Intent(this, ShowCodesActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(CODES, list);
            intent.putExtra(SERVERS, servers);
            intent.putExtra(VALUE_OBJECT, v_objekt);
            startActivity(intent);
        }
        if(id == R.id.action_set_server) {

            Intent intent = new Intent(this, SaveServerActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(SERVERS, servers);
            intent.putExtra(CODES, list);
            intent.putExtra(VALUE_OBJECT, v_objekt);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private ImageButton initializeConfirmButton() {

        final ImageButton buttonConfirm;

        buttonConfirm = (ImageButton) findViewById(R.id.buttonConfirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int status = pad.pinStatus(v_objekt);

                if(status == PinPad.PIN_SHORT) {
                    pad.getInfo().setText(getResources().getString(R.string.info));

                }
                else if(status == PinPad.PIN_INCORRECT) {
                    pad.getInfo().setText(getResources().getString(R.string.error_incorrect_pin));
                    Vibrator wrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    wrong.vibrate(200);

                    System.out.println("INCORRECT");

                }
                else if(status == PinPad.PIN_READY) {
                    pad.getInfo().setText(getResources().getString(R.string.info_correct_pin) + "\n" + v_objekt.getTime() + "s");

                    try {
                        v_objekt.setURL(servers.getActive());
                        openDialog(v_objekt.toString());

                        System.out.println("CORRECT");

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                else if(status == PinPad.V_OBJ_NULL) {
                    pad.getInfo().setText(getResources().getString(R.string.error_pin_not_selected));
                    Vibrator wrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    wrong.vibrate(200);
                }
                else if(status == PinPad.PIN_CORRECT) {
                    pad.getInfo().setText(getResources().getString(R.string.info_correct_pin) + "\n" + v_objekt.getTime() + "s");
                    Vibrator correct = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    correct.vibrate(250);

                    System.out.println("CORRECT");
                    v_objekt.reset();
                }
                else if(status == PinPad.PIN_EMPTY) {
                    System.out.println("Does not count");
                }
            }
        });

        return buttonConfirm;
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


    private void openDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final CharSequence[] items = {"SF"};

        alertDialogBuilder.setTitle(getResources().getString(R.string.title_dialog_window));
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.btn_dialog_positive), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    new ClientTask().execute(v_objekt);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.btn_dialog_negative), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                v_objekt.reset();
                intent.putExtra(CODES, list);
                intent.putExtra(VALUE_OBJECT, v_objekt);
                intent.putExtra(SERVERS, servers);
                startActivity(intent);
            }
        });

        alertDialogBuilder.setNeutralButton(getResources().getString(R.string.btn_dialog_neutral), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    v_objekt.setType(ValueObject.SHOULDER_SURFER);
                    new ClientTask().execute(v_objekt);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sendValue(int value) {


        int status = pad.type(value, v_objekt);

        if(status == PinPad.PIN_SHORT) {
            pad.getInfo().setText(getResources().getString(R.string.info));

            Vibrator tap = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            tap.vibrate(60);
        }
    }
}
