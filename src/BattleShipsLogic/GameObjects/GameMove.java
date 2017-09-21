package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.PlayerName;

public class GameMove {

    /* -------------- Data members -------------- */

    private char[][] primaryBoard;
    private char[][] trackingBoard;
    private PlayerName currentPlayerName;
    private int player1Score;
    private int player2Score;
    private int player1Mines;
    private int player2Mines;

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

    public PlayerName getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(PlayerName currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public int getPlayer1Mines() {
        return player1Mines;
    }

    public void setPlayer1Mines(int player1Mines) {
        this.player1Mines = player1Mines;
    }

    public int getPlayer2Mines() {
        return player2Mines;
    }

    public void setPlayer2Mines(int player2Mines) {
        this.player2Mines = player2Mines;
    }


    /* -------------- Function members -------------- */

    public GameMove(char[][] primaryBoard, char[][] trackingBoard, PlayerName currentPlayerName, int player1Score, int player2Score, int player1Mines, int player2Mines, int boardSize) {
        this.primaryBoard = new char[boardSize+1][boardSize+1];
        this.trackingBoard = new char[boardSize+1][boardSize+1];
        copyBoard(primaryBoard, this.primaryBoard);
        copyBoard(trackingBoard, this.trackingBoard);
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.currentPlayerName = currentPlayerName;
        this.player1Mines = player1Mines;
        this.player2Mines = player2Mines;
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
