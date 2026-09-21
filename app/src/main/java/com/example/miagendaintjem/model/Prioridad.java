package com.example.miagendaintjem.model;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.example.miagendaintjem.R;

public enum Prioridad {
    BAJA(R.color.priority_low, R.string.priority_low_label),
    MEDIA(R.color.priority_medium, R.string.priority_medium_label),
    ALTA(R.color.priority_high, R.string.priority_high_label);

    @ColorRes
    private final int colorRes;
    @StringRes
    private final int labelRes;

    Prioridad(@ColorRes int colorRes, @StringRes int labelRes) {
        this.colorRes = colorRes;
        this.labelRes = labelRes;
    }

    @ColorRes
    public int getColorRes() {
        return colorRes;
    }

    @StringRes
    public int getLabelRes() {
        return labelRes;
    }
}
