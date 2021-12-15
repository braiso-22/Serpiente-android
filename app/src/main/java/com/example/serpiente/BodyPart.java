package com.example.serpiente;

import android.graphics.Color;

public class BodyPart {
    Position position;
    int color;

    public BodyPart(Position position, int color) {
        this.position = position;
        this.color = color;
    }

    public BodyPart(Object bp) {
        clone(bp);
    }



    public void setPosition(Position position) {
        Position p = new Position(position);
        this.position = p;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public int getColor() {
        return color;
    }

    public void clone(Object ob) {
        try {
            BodyPart bp = (BodyPart) ob;
            setColor(((BodyPart) ob).color);
            setPosition(((BodyPart) ob).position);
        } catch (Exception e) {

        }
        try {
            setColor(((Snake) ob).getColor());
            setPosition(((Snake) ob).getPosition());
        } catch (Exception e) {

        }
    }


}
