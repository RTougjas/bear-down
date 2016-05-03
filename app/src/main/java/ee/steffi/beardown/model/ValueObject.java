package ee.steffi.beardown.model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rain on 4/13/16.
 */
public class ValueObject implements Serializable {

    public static final String SHOULDER_SURFER = "SF";
    public static final String TEST_ENTRY = "TE";

    private PinCode code;
    private ArrayList<String> wrong_attempts;
    private String time;
    private URL url;
    private int attempt_count;
    private boolean isPinSet = false;
    private String type;

    public ValueObject() {
        super();
        this.wrong_attempts = new ArrayList<String>();
        this.attempt_count = wrong_attempts.size();
        this.type = TEST_ENTRY;
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
                "Aeg: " + time + "s" + "\n" +
                "Katseid: " + attempt_count + "\n" +
                "Server: " + url;

    }

    public void reset() {
        this.time = null;
        this.attempt_count = 0;
        this.wrong_attempts.clear();
    }

    public void setURL(String url) throws MalformedURLException {

        this.url = new URL(url);
    }

    public URL getURL() {

        return this.url;
    }

    public ArrayList<String> getWrongAttempts() {

        return this.wrong_attempts;
    }

    public String getCorrectCode() {

        return code.getPinCode();
    }

    public String getType() {

        return this.type;
    }

    public void setType(String s) {

        this.type = s;
    }

    public void setCode(PinCode c) {

        this.code = c;
        this.isPinSet = true;
    }

    public boolean isPinSet() {

        return this.isPinSet;
    }
}
