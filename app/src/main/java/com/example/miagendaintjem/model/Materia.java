package com.example.miagendaintjem.model;

import androidx.annotation.StringRes;

import com.example.miagendaintjem.R;

/**
 * Subjects offered by the agenda. The value identifies the subject and the
 * label resource renders it, so a task keeps its identity when the device
 * language changes.
 *
 * <p>The declaration order must match {@code R.array.subjects}, and
 * {@code R.array.subject_filters} adds "all subjects" in front of it.
 */
public enum Materia {
    MOVILES(R.string.subject_mobile),
    BASE_DATOS(R.string.subject_database),
    SOFTWARE(R.string.subject_software),
    REDES(R.string.subject_networks);

    @StringRes
    private final int labelRes;

    Materia(@StringRes int labelRes) {
        this.labelRes = labelRes;
    }

    @StringRes
    public int getLabelRes() {
        return labelRes;
    }
}
