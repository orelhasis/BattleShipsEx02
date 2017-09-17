package BattleShipsLogic.GameObjects;

public class Mine extends SeaItem {

    /* -------------- Data members -------------- */


    /* -------------- Function members -------------- */
    public Mine(int x, int y) {
        super(x,y);
        setItemChar('#');
    }
    @Override
    public void GotHit(){
        setItemChar('X');
        super.GotHit();
    }
}
