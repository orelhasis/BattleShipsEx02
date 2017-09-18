package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.ShipCategories;
import BattleShipsLogic.Definitions.ShipDirection;

public class BattleShip extends SeaItem {
    /* -------------- Data members -------------- */
    private ShipDirection direction;
    private String type;
    private final int length;
    private int remainingPartsCount;
    private int score;
    private ShipCategories shipCategory;

    /* -------------- Getters and setters -------------- */
    public ShipDirection getDirection() {
        return direction;
    }

    public ShipCategories  getShipCategory() {
        return shipCategory;
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

    public BattleShip(ShipDirection direction, String type, int length, int score, int x, int y, ShipCategories cat) {
        super(x,y);
        this.shipCategory = cat;
        this.score = score;
        this.direction = direction;
        this.type = type;
        this.length = this.remainingPartsCount = length;
        if(shipCategory == ShipCategories.L_SHAPE){
            this.remainingPartsCount =(this.remainingPartsCount*2) - 1;
        }
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

