package BattleShipsLogic.GameObjects;

public class PlayerStatistics {

    /* -------------- Data members -------------- */

    private int numberOfTurns;
    private int averageTimeForTurn; // In seconds.
    private int numberOfHits;
    private int numberOfMissings;

    /* -------------- Getters and setters -------------- */

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public int getAverageTimeForTurn() {
        return averageTimeForTurn;
    }

    public void setAverageTimeForTurn(int averageTimeForTurn) {
        this.averageTimeForTurn = averageTimeForTurn;
    }

    public int getNumberOfHits() {
        return numberOfHits;
    }

    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    public void AddHit() {
        this.numberOfHits ++;
    }

    public int getNumberOfMissing() {
        return numberOfMissings;
    }

    public void setNumberOfMissings(int numberOfMissings) {
        this.numberOfMissings = numberOfMissings;
    }

    public void AddMiss() {
        this.numberOfMissings++;
    }

}
