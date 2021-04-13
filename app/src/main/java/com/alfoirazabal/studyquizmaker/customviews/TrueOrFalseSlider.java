package com.alfoirazabal.studyquizmaker.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.alfoirazabal.studyquizmaker.R;

public class TrueOrFalseSlider extends androidx.appcompat.widget.AppCompatSeekBar {

    public enum SLIDER_STATUS {
        FALSE,
        UNSET,
        TRUE
    };

    private SLIDER_STATUS sliderStatus;

    public TrueOrFalseSlider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setMax(2);
        this.setStatus(SLIDER_STATUS.UNSET);

        this.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch(progress) {
                    case 0:
                        setStatus(SLIDER_STATUS.FALSE);
                        break;
                    case 1:
                        setStatus(SLIDER_STATUS.UNSET);
                        break;
                    case 2:
                        setStatus(SLIDER_STATUS.TRUE);
                        break;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void setStatus(SLIDER_STATUS sliderStatus) {
        this.sliderStatus = sliderStatus;
        switch (sliderStatus) {
            case TRUE:
                this.setProgress(2);
                this.setThumb(getResources().getDrawable(R.drawable.ic_true));
                this.setBackgroundColor(Color.GREEN);
                break;
            case UNSET:
                this.setProgress(1);
                this.setThumb(getResources().getDrawable(R.drawable.ic_true_or_false_unset));
                this.setBackgroundColor(Color.rgb(230, 230, 230));
                break;
            case FALSE:
                this.setProgress(0);
                this.setThumb(getResources().getDrawable(R.drawable.ic_false));
                this.setBackgroundColor(Color.RED);
                break;
        }
    }

    public SLIDER_STATUS getSliderStatus() {
        return this.sliderStatus;
    }

}
