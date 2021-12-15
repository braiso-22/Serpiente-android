package com.example.serpiente;


import android.graphics.Color;

import java.util.ArrayList;

import afu.org.checkerframework.checker.oigj.qual.O;

public class Snake {
    Position position;
    int orientacion;
    int color;
    ArrayList<BodyPart> cuerpo;


    public Snake(Position position) {
        this.position = position;
        this.color = Color.rgb((int) ((Math.random() * 100)+1),
                (int) ((Math.random() * 105) + 151),
                (int) ((Math.random() * 205) + 51));
        orientacion = Orientacion.ARRIBA;
        cuerpo = new ArrayList<>();
    }

    public Position getPosition() {
        return position;
    }

    public int getColor() {
        return color;
    }

    public int getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(int orientacion) {
        this.orientacion = orientacion;
    }

    public void giraDerecha() {
        if (orientacion != Orientacion.IZQUIERDA) {
            orientacion++;
        } else {
            orientacion = Orientacion.ARRIBA;
        }
    }

    public void giraIzquierda() {
        if (orientacion != Orientacion.ARRIBA) {
            orientacion--;
        } else {
            orientacion = Orientacion.IZQUIERDA;
        }
    }

    public void addBodyPart(Object o) {
        cuerpo.add(new BodyPart(o));
    }
}
