package ee.steffi.beardown.model;

import java.util.ArrayList;

/**
 * Created by rain on 4/12/16.
 */
public class ImportantData {

    private String code;
    private int attempts;
    private ArrayList<String> wrong_attempts;
    private String time;
    private boolean wasScrambled;

    public ImportantData() {
        super();
        this.wrong_attempts = new ArrayList<String>();
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isWasScrambled() {
        return wasScrambled;
    }

    public void setWasScrambled(boolean wasScrambled) {
        this.wasScrambled = wasScrambled;
    }

    public void addWrongAttempt(String pin) {

        wrong_attempts.add(pin);
    }
}
