package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.ShipDirection;

public class BattleShip extends SeaItem {
    /* -------------- Data members -------------- */
    private ShipDirection direction;
    private String type;
    private final int length;
    private int remainingPartsCount;
    private int score;

    /* -------------- Getters and setters -------------- */
    public ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(ShipDirection direction) {
        this.direction = direction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    /* -------------- Function members -------------- */

    public BattleShip(ShipDirection direction, String type, int length, int score, int x, int y) {
        super(x,y);
        this.score = score;
        this.direction = direction;
        this.type = type;
        this.length = this.remainingPartsCount = length;
        setItemChar('S');
    }

    @Override
    public void GotHit() {
        this.remainingPartsCount--;
    }

    @Override
    public boolean IsDestroyed() {
         return remainingPartsCount == 0;
    }

    @Override
    public int GetScore(){
        return score;
    }
}

