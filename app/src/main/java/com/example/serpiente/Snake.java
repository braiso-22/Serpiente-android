package com.example.serpiente;


import android.graphics.Color;

import afu.org.checkerframework.checker.oigj.qual.O;

public class Snake {
    Position position ;
    int orientacion;
    int color;


    public Snake(Position position) {
        this.position = position;
        this.color = Color.rgb(255,0,0);
        orientacion=Orientacion.ARRIBA;
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

    public void giraDerecha(){
        if(orientacion!=Orientacion.IZQUIERDA){
            orientacion++;
        }else{
            orientacion=Orientacion.ARRIBA;
        }
    }
    public void giraIzquierda(){
        if(orientacion!=Orientacion.ARRIBA){
            orientacion--;
        }else{
            orientacion=Orientacion.IZQUIERDA;
        }
    }
}
