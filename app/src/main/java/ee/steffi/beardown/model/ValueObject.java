package ee.steffi.beardown.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rain on 4/13/16.
 */
public class ValueObject implements Serializable {

    private PinCode code;
    private ArrayList<String> wrong_attempts;
    private String time;
    private int attempt_count;

    public ValueObject(PinCode c) {
        super();
        this.code = c;
        this.wrong_attempts = new ArrayList<String>();
        this.attempt_count = wrong_attempts.size();
    }

    public void storeWrongAttempt(String wrong_pin) {
        if(code.getPinCode().equals(wrong_pin) == false) {
            wrong_attempts.add(wrong_pin);
            attempt_count++;
        }
    }

    public boolean isMatch(String another) {

        if(this.code.getPinCode().equalsIgnoreCase(another)) {
            attempt_count++;
            return true;
        }

        return false;
    }

    public void setTime(double time) {

        this.time = String.valueOf(time);

    }

    public String getTime() {

        return this.time;
    }

    public String toString() {

        return "PIN: " + code.getPinCode() + "\n" +
                "Aeg: " + time + "\n" +
                "Katseid: " + attempt_count;

    }

    public void reset() {
        this.time = null;
        this.attempt_count = 0;
        this.wrong_attempts.clear();
    }
}
