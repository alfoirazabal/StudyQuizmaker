package com.alfoirazabal.studyquizmaker.gui.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.alfoirazabal.studyquizmaker.helpers.icons.IconColors;

import java.util.Random;

public class IconColorPicker {

    private final Context context;
    private IconColors.ColorForIcon[] colorForIcons;
    private Spinner spinnerColorPicker;

    public IconColorPicker(Context context) {
        this.context = context;
    }

    public void setIconColorPicker(
            Spinner spinnerColorPicker,
            ImageView imgIcon
    ) {
        this.spinnerColorPicker = spinnerColorPicker;
        IconColors iconColors = new IconColors(this.context);
        this.colorForIcons = iconColors.getColors();
        ArrayAdapter<IconColors.ColorForIcon> adapterSpinner = new ArrayAdapter<>(
                this.context, android.R.layout.simple_spinner_item, this.colorForIcons
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerColorPicker.setAdapter(adapterSpinner);
        this.spinnerColorPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IconColors.ColorForIcon selectedColorForIcon = colorForIcons[position];
                imgIcon.setColorFilter(selectedColorForIcon.color, PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setCurrentColorInSpinner(int color) {
        int foundPosition = -1;
        for (int i = 0 ; foundPosition == -1 && i < this.colorForIcons.length ; i++) {
            if (this.colorForIcons[i].color == color) {
                foundPosition = i;
            }
        }
        spinnerColorPicker.setSelection(foundPosition);
    }

    public IconColors.ColorForIcon getSelectedColor() {
        return this.colorForIcons[this.spinnerColorPicker.getSelectedItemPosition()];
    }

}
