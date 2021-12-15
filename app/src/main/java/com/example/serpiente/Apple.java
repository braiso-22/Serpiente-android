package com.example.serpiente;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;

public class Apple {
    Position position;

    Bitmap skin;

    public Apple(Position position, Bitmap skin) {
        this.position = position;
        this.skin = skin;
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Bitmap getSkin() {
        return skin;
    }
}
