package com.example.serpiente;

import java.util.Objects;

public class Position {
    private int PosX, PosY;

    public Position(int posX, int posY) {
        PosX = posX;
        PosY = posY;
    }
    public Position(Position pos){
        setPos(pos);
    }

    public int getPosX() {
        return PosX;
    }

    public void setPosX(int posX) {
        PosX = posX;
    }

    public int getPosY() {
        return PosY;
    }

    public void setPosY(int posY) {
        PosY = posY;
    }

    public void setPos(int x, int y) {
        setPosX(x);
        setPosY(y);
    }
    public void setPos(Position pos){
        PosX=pos.getPosX();
        PosY=pos.getPosY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return PosX == position.PosX && PosY == position.PosY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PosX, PosY);
    }
}
