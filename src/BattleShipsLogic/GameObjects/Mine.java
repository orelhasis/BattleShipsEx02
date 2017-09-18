package BattleShipsLogic.GameObjects;

public class Mine extends SeaItem {

    /* -------------- Data members -------------- */


    /* -------------- Function members -------------- */
    public Mine(int x, int y) {
        super(x,y);
        setItemChar('M');
    }

    public Mine(Point position) {
        super(position);
        setItemChar('M');
    }

    @Override
    public void GotHit(){
        setItemChar('E');
        super.GotHit();
    }
}
