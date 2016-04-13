package ee.steffi.beardown.model;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import ee.steffi.beardown.R;

/**
 * Created by rain on 4/12/16.
 */
public class CustomButton extends Button {

    private int value;
    private ImageButton image;


    public CustomButton(Context context, int value, ImageButton image) {
        super(context);
        this.value = value;
        this.image = image;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ImageButton getImageButton() {

        return this.image;
    }

    public void update() {


        switch (value) {
            case 0:
                this.image.setImageResource(R.drawable.ic_10);
                break;
            case 1:
                this.image.setImageResource(R.drawable.ic_1);
                break;
            case 2:
                this.image.setImageResource(R.drawable.ic_2);
                break;
            case 3:
                this.image.setImageResource(R.drawable.ic_3);
                break;
            case 4:
                this.image.setImageResource(R.drawable.ic_4);
                break;
            case 5:
                this.image.setImageResource(R.drawable.ic_5);
                break;
            case 6:
                this.image.setImageResource(R.drawable.ic_6);
                break;
            case 7:
                this.image.setImageResource(R.drawable.ic_7);
                break;
            case 8:
                this.image.setImageResource(R.drawable.ic_8);
                break;
            case 9:
                this.image.setImageResource(R.drawable.ic_9);
                break;
            default:
                this.image.setImageResource(R.drawable.ic_10);
        }

    }
}
