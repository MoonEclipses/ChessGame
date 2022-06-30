package Piecies;

import java.util.Objects;

public class Position implements Cloneable{
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public static Position pos(int x, int y){
        return new Position(x,y);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    public Position sum(Position term){
        return new Position(x + term.x,y+ term.y);
    }

    public static boolean isPosible(Position position){
        if(position.x>=0 && position.x<8 && position.y>=0 && position.y<8){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
