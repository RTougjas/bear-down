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
    public static final String PAD_TRADITIONAL = "REG";
    public static final String PAD_RANDOM = "RAN";

    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_FAIL = 0;

    private PinCode code;
    private ArrayList<String> wrong_attempts;
    private String time;
    private URL url;
    private int attempt_count;
    private boolean isPinSet = false;
    private String type;
    private int status;
    private String pad_status;

    public ValueObject() {
        super();
        this.wrong_attempts = new ArrayList<String>();
        this.attempt_count = wrong_attempts.size();
        this.type = TEST_ENTRY;
        this.status = STATUS_PENDING;
        this.pad_status = PAD_TRADITIONAL;
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

    public void reset(int l_status) {
        this.time = null;
        this.attempt_count = 0;
        this.wrong_attempts.clear();
        this.type = TEST_ENTRY;
        this.status = l_status;
    }

    public void reset() {
        this.time = null;
        this.attempt_count = 0;
        this.wrong_attempts.clear();
        this.type = TEST_ENTRY;
        this.status = STATUS_PENDING;
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

    public String getPadStatus() {

        return this.pad_status;
    }

    public void setPadStatus(String p_status) {

        this.pad_status = p_status;
    }

    public void setCode(PinCode c) {

        this.code = c;
        this.isPinSet = true;
    }

    public boolean isPinSet() {

        return this.isPinSet;
    }

    public int getStatus() {

        return this.status;
    }


}
