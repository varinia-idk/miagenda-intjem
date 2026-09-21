package com.example.miagendaintjem.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public final class Tarea implements Parcelable {
    private final int id;
    @NonNull
    private final String titulo;
    @NonNull
    private final Materia materia;
    @NonNull
    private final String fechaEntrega;
    @NonNull
    private final Prioridad prioridad;

    public Tarea(
            int id,
            @NonNull String titulo,
            @NonNull Materia materia,
            @NonNull String fechaEntrega,
            @NonNull Prioridad prioridad
    ) {
        this.id = id;
        this.titulo = titulo;
        this.materia = materia;
        this.fechaEntrega = fechaEntrega;
        this.prioridad = prioridad;
    }

    private Tarea(@NonNull Parcel source) {
        id = source.readInt();
        titulo = valueOrEmpty(source.readString());
        String subjectName = source.readString();
        materia = subjectName == null ? Materia.MOVILES : Materia.valueOf(subjectName);
        fechaEntrega = valueOrEmpty(source.readString());

        String priorityName = source.readString();
        prioridad = priorityName == null
                ? Prioridad.MEDIA
                : Prioridad.valueOf(priorityName);
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    @NonNull
    public Materia getMateria() {
        return materia;
    }

    @NonNull
    public String getFechaEntrega() {
        return fechaEntrega;
    }

    @NonNull
    public Prioridad getPrioridad() {
        return prioridad;
    }

    @NonNull
    public Tarea withTitulo(@NonNull String newTitle) {
        return new Tarea(id, newTitle, materia, fechaEntrega, prioridad);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel destination, int flags) {
        destination.writeInt(id);
        destination.writeString(titulo);
        destination.writeString(materia.name());
        destination.writeString(fechaEntrega);
        destination.writeString(prioridad.name());
    }

    public static final Creator<Tarea> CREATOR = new Creator<>() {
        @Override
        public Tarea createFromParcel(Parcel source) {
            return new Tarea(source);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };
}
