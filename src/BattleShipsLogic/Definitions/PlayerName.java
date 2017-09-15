package BattleShipsLogic.Definitions;

public enum PlayerName {
    PLAYER_1, PLAYER_2;

    @Override
    public String toString() {
        switch (this) {
            case PLAYER_1:
                return "Player 1";
            case PLAYER_2:
                return "Player 2";
            default:
                throw new IllegalArgumentException();
        }
    }
}
