package BattleShipsLogic.GameObjects;

public abstract class SeaItem {

    /* -------------- Data members -------------- */

    private Point position;

    private char itemChar;

    protected boolean gotHit;

    /* -------------- Getters and setters -------------- */
    SeaItem(int x, int y) {
        this.position = new Point(x,y);
    }

    SeaItem(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public char getItemChar() {
        return itemChar;
    }

    public void setItemChar(char itemChar) {
        this.itemChar = itemChar;
    }

    public void GotHit(){
        gotHit = true;
    }

    public boolean IsDestroyed(){
        return gotHit;
    }

    public int GetScore(){
        return 0;
    }
}
