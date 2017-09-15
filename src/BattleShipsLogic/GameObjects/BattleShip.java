package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.ShipDirection;
import BattleShipsLogic.Definitions.ShipType;

public class BattleShip extends SeaItem {
    /* -------------- Data members -------------- */
    private ShipDirection direction;
    private ShipType type;
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

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    /* -------------- Function members -------------- */

    public BattleShip(ShipDirection direction, ShipType type, int length, int score, int x, int y) {
        super(x,y);
        this.score = score;
        this.direction = direction;
        this.type = type;
        this.length = this.remainingPartsCount = length;
        setItemChar('B');
        if(type == ShipType.shipTypeA) {
            setItemChar('A');
        }
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

