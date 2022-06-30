package Game;

public class Player {
    private Color color;
    private boolean currentMove;

    public Player(){
        color = null;
    }

    public Player(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(boolean currentMove) {
        this.currentMove = currentMove;
    }
}
