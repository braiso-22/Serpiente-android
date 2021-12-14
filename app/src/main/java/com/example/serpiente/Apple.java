package com.example.serpiente;

import android.graphics.Color;

public class Apple {
    Position position ;
    int color;

    public Apple(Position position) {
        this.position = position;
        this.color = Color.rgb(255,(int)((Math.random()*205)+51),0);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
