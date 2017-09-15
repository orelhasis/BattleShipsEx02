package BattleShipsLogic.GameObjects;

public class WaterItem extends SeaItem {

    /* -------------- Data members -------------- */


    /* -------------- Function members -------------- */
    public WaterItem(int x, int y) {
        super(x,y);
        setItemChar(' ');
    }
    @Override
    public void GotHit(){
        setItemChar('X');
        super.GotHit();
    }
}
