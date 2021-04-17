package com.alfoirazabal.studyquizmaker.helpers.icons;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.alfoirazabal.studyquizmaker.R;

public class IconColors {

    public class ColorForIcon {
        public int color;
        public String colorName;

        public ColorForIcon(int color, String colorName) {
            this.color = color;
            this.colorName = colorName;
        }

        @NonNull
        @Override
        public String toString() {
            return this.colorName;
        }
    }

    private final Context context;

    public IconColors(Context context) {
        this.context = context;
    }

    public ColorForIcon[] getColors() {
        return new ColorForIcon[] {
                new ColorForIcon(
                        Color.rgb(125, 125, 125),
                        this.context.getString(R.string.color_gray)
                ),
                new ColorForIcon(
                        Color.rgb(252, 3, 3),
                        this.context.getString(R.string.color_red)
                ),
                new ColorForIcon(
                        Color.rgb(252, 119, 3),
                        this.context.getString(R.string.color_orange)
                ),
                new ColorForIcon(
                        Color.rgb(252, 202, 3),
                        this.context.getString(R.string.color_yellow)
                ),
                new ColorForIcon(
                        Color.rgb(0, 227, 26),
                        this.context.getString(R.string.color_green)
                ),
                new ColorForIcon(
                        Color.rgb(0, 255, 116),
                        this.context.getString(R.string.color_green_water)
                ),
                new ColorForIcon(
                        Color.rgb(0, 187, 255),
                        this.context.getString(R.string.color_light_blue)
                ),
                new ColorForIcon(
                        Color.rgb(0, 90, 244),
                        this.context.getString(R.string.color_blue)
                ),
                new ColorForIcon(
                        Color.rgb(128, 0, 255),
                        this.context.getString(R.string.color_violet)
                ),
                new ColorForIcon(
                        Color.rgb(255, 0, 251),
                        this.context.getString(R.string.color_pink)
                ),
                new ColorForIcon(
                        Color.rgb(255, 0, 144),
                        this.context.getString(R.string.color_fucsia)
                )
        };
    }

}
