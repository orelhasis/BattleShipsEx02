package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.PlayerName;

public class GameMove {

    /* -------------- Data members -------------- */

    private char[][] primaryBoard;
    private char[][] trackingBoard;
    private PlayerName currentPlayer;
    private PlayerName attackedPlayer;
    private int currentPlayerScore;
    private int opponentPlayerScore;

    /* -------------- Getters and setters -------------- */

    public char[][] getPrimaryBoard() {
        return primaryBoard;
    }

    public void setPrimaryBoard(char[][] primaryBoard) {
        this.primaryBoard = primaryBoard;
    }

    public char[][] getTrackingBoard() {
        return trackingBoard;
    }

    public void setTrackingBoard(char[][] trackingBoard) {
        this.trackingBoard = trackingBoard;
    }

    public PlayerName getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerName currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerName getAttackedPlayer() {
        return attackedPlayer;
    }

    public void setAttackedPlayer(PlayerName attackedPlayer) {
        this.attackedPlayer = attackedPlayer;
    }

    public int getCurrentPlayerScore() {
        return currentPlayerScore;
    }

    public void setCurrentPlayerScore(int currentPlayerScore) {
        this.currentPlayerScore = currentPlayerScore;
    }

    public int getOpponentPlayerScore() {
        return opponentPlayerScore;
    }

    public void setOpponentPlayerScore(int opponentPlayerScore) {
        this.opponentPlayerScore = opponentPlayerScore;
    }

    /* -------------- Function members -------------- */

    public GameMove(char[][] primaryBoard, char[][] trackingBoard, PlayerName currentPlayerName, PlayerName attackedPlayer, int currentPlayerScore, int opponentPlayerScore, int boardSize) {
        this.primaryBoard = new char[boardSize+1][boardSize+1];
        this.trackingBoard = new char[boardSize+1][boardSize+1];
        copyBoard(primaryBoard, this.primaryBoard);
        copyBoard(trackingBoard, this.trackingBoard);
        this.currentPlayer = currentPlayerName;
        this.attackedPlayer = attackedPlayer;
        this.currentPlayerScore = currentPlayerScore;
        this.opponentPlayerScore = opponentPlayerScore;
    }

    private void  copyBoard(char[][] src, char[][] dest) {
        int i,j;
        for(i=0; i<src.length; i++){
            for(j=0;j<src[i].length; j++) {
                dest[i][j] = src[i][j];
            }
        }
    }


}
