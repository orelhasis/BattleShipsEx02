package BattleShipsLogic.GameObjects;

public class ShipRemains extends SeaItem {

    ShipRemains(int x, int y) {
        super(x, y);
        super.gotHit = true;
        this.setItemChar('O');
    }
}
