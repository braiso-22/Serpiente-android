package com.example.serpiente;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;

import afu.org.checkerframework.checker.oigj.qual.O;
import kotlinx.metadata.internal.metadata.jvm.deserialization.BitEncoding;

public class Snake {
    Position position;
    int orientacion;
    int color;
    Bitmap skin;
    ArrayList<BodyPart> cuerpo;


    public Snake(Position position, Bitmap skin) {
        this.position = position;
        this.color = Color.rgb((int) ((Math.random() * 100) + 1),
                (int) ((Math.random() * 105) + 151),
                (int) ((Math.random() * 205) + 51));
        orientacion = Orientacion.ARRIBA;
        this.skin = skin;
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

    public void giraIzquierda() {
        //orientacion=(orientacion+1)%4;

        if (orientacion != Orientacion.IZQUIERDA) {
            orientacion++;
        } else {
            orientacion = Orientacion.ARRIBA;
        }
    }

    public void giraDerecha() {

        //orientacion=(orientacion-1)%4;


        if (orientacion != Orientacion.ARRIBA) {
            orientacion--;
        } else {
            orientacion = Orientacion.IZQUIERDA;
        }
    }

    public void addBodyPart(Object o) {
        cuerpo.add(new BodyPart(o));
    }

    public Bitmap getSkin() {
        return skin;
    }

    public void setSkin(Bitmap skin) {
        this.skin = skin;
    }

    public void giraSkin(int grados) {

        Matrix matrix = new Matrix();
        matrix.preRotate(grados);
        Bitmap bm = Bitmap.createBitmap(getSkin(), 0, 0,
                getSkin().getWidth(),
                getSkin().getHeight(), matrix, true);
        setSkin(bm);

    }
    public void updateBody(){
        if (!cuerpo.isEmpty()) {
            BodyPart bp;
            for (int i = cuerpo.size() - 1; i >= 0; i--) {
                bp = cuerpo.get(i);
                if (i == 0) {
                    bp.position.setPos(position);
                } else {
                    bp.position.setPos(cuerpo.get(i - 1).position);
                }
            }
        }
    }
}
