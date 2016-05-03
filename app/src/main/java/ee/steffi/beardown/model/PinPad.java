package ee.steffi.beardown.model;

import android.content.Context;
import android.os.Vibrator;
import android.renderscript.Sampler;
import android.text.Editable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import ee.steffi.beardown.R;

/**
 * Created by rain on 4/12/16.
 */
public class PinPad {

    private static final int[] VALUES = {1,2,3,4,5,6,7,8,9,0};

    public static final int PIN_READY = 5;
    public static final int V_OBJ_NULL = 4;
    public static final int PIN_CORRECT = 3;
    public static final int PIN_INCORRECT = 2;
    public static final int PIN_SHORT = 1;
    public static final int PIN_EMPTY = 0;

    private CustomButton[] buttons;
    private ArrayList<Integer> button_values;

    private Switch toggle_scramble, toggle_save;
    private EditText pin;
    private TextView info;
    private ImageButton btn_backspace, btn_confirm;
    private long start_time, stop_time;
    private boolean savePIN;
    private ValueObject v_object;

    public PinPad(Switch random, Switch save, TextView info_field, EditText pin_entry, ImageButton backspace, CustomButton[] btns, ImageButton confirm, ValueObject v_obj) {
        super();
        this.toggle_scramble = random;
        this.toggle_save = save;
        this.info = info_field;
        this.pin = pin_entry;
        this.btn_backspace = backspace;
        this.btn_confirm = confirm;
        this.buttons = btns;
        this.button_values = new ArrayList<Integer>();
        this.stop_time = 0;
        this.start_time = 0;
        this.savePIN = false;
        this.v_object = v_obj;

        toggle_scramble.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    scrambleButtons();
                } else {
                    unScrambleButtons();
                }
            }
        });

        toggle_save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (toggle_scramble.isChecked()) {
                        scrambleButtons();
                    }
                    pin.setText("");
                    startTiming();
                    v_object.reset();
                } else {
                    stopTiming();
                }
            }
        });
    }

    private void scrambleButtons() {

        button_values.clear();

        for(int i = 0; i < buttons.length; i++) {
            button_values.add(buttons[i].getValue());
        }

        Collections.shuffle(button_values, new SecureRandom());

        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setValue(button_values.get(i));
            buttons[i].update();
        }
    }

    private void unScrambleButtons() {

        button_values.clear();
        for(int i = 0; i < buttons.length; i++) {
            button_values.add(VALUES[i]);
            buttons[i].setValue(button_values.get(i));
            buttons[i].update();
        }
    }

    private void startTiming() {

        this.start_time = System.currentTimeMillis();
    }

    private void stopTiming() {

        this.stop_time = System.currentTimeMillis();
    }

    private double getTime() {

        return (stop_time - start_time) / 1000.0;
    }

    public int pinStatus(ValueObject v_obj) {

        int status = -1;

        if(isLengthOK() && v_obj.isPinSet()) {
            if(toggle_save.isChecked()) {
                if(v_obj.isMatch(pin.getText().toString())) {
                    status = PIN_READY;
                    toggle_save.setChecked(false);
                    v_obj.setTime(getTime());
                    pin.setText("");
                }
                else {
                    status = PIN_INCORRECT;
                    v_obj.storeWrongAttempt(pin.getText().toString());
                    pin.setText("");
                }
            }
            else {
                start_time = 0;
                stop_time = 0;

                if(v_obj.isMatch(pin.getText().toString())) {
                    status = PIN_CORRECT;
                    v_obj.setTime(getTime());
                    pin.setText("");
                    return status;
                } else {
                    status = PIN_INCORRECT;
                    pin.setText("");
                    return status;
                }
            }
        }
        else if(!isLengthOK() && v_obj.isPinSet()) {
            if(toggle_save.isChecked()) {
                status = PIN_INCORRECT;
                v_obj.storeWrongAttempt(pin.getText().toString());
                pin.setText("");
            }
            else {
                status = PIN_SHORT;
            }
        }
        else {
            status = V_OBJ_NULL;
        }


        /*
        if(isLengthOK()) {
            if(toggle_save.isChecked()) {
                if(v_obj == null) {
                    status = V_OBJ_NULL;
                    pin.setText("");
                }
                else {
                    if(v_obj.isMatch(pin.getText().toString())) {
                        status = PIN_READY;
                        toggle_save.setChecked(false);
                        v_obj.setTime(getTime());
                        pin.setText("");
                    }
                    else {
                        status = PIN_INCORRECT;
                        v_obj.storeWrongAttempt(pin.getText().toString());
                        pin.setText("");
                    }
                }
            }
            else {
                start_time = 0;
                stop_time = 0;
                if(v_obj == null) {
                    status = V_OBJ_NULL;
                    pin.setText("");
                }
                else {
                    if(v_obj.isMatch(pin.getText().toString())) {
                        status = PIN_CORRECT;
                        v_obj.setTime(getTime());
                        pin.setText("");
                        return status;
                    }
                    else {
                        status = PIN_INCORRECT;
                        pin.setText("");
                        return status;
                    }
                }
            }
        }
        else {
            if(toggle_save.isChecked()) {
                status = PIN_INCORRECT;
                v_obj.storeWrongAttempt(pin.getText().toString());
                pin.setText("");
            }
            else {
                status = PIN_SHORT;
            }
        }
        */
        return status;
    }

    public int type(int value, ValueObject v_obj) {

        String v = String.valueOf(value);
        int status = -1;

        Editable duh = pin.getText();
        duh.append(v);
        pin.setText(duh);
        pin.setSelection(pin.length());

        if(pin.length() < 4) {
            status = PIN_SHORT;
        }

        return status;
    }

    public TextView getInfo() {
        return info;
    }

    public boolean isLengthOK() {
        if(pin.length() >= 4) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getCode() {

        return pin.getText().toString();
    }

    public EditText getPinField() {

        return pin;
    }

    public void disableSaving() {

        this.toggle_save.setClickable(false);
    }

    public void setSavePIN(boolean value) {

        this.savePIN = value;
    }
}
