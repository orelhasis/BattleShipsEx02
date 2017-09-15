package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.PlayerName;

public class Player {

    /* -------------- Data members -------------- */
    private PlayerName name;
    private int boardSize;
    private SeaItem[][] board;
    private final int numOfShips;
    private int remainingShips;
    private int score;
    private PlayerStatistics statistics = new PlayerStatistics();
    private int numberOfMines;

    /* -------------- Getters and setters -------------- */

    public PlayerName getName() {
        return name;
    }

    public void setName(PlayerName name) {
        this.name = name;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }

    public void AddScore(int score) {
        this.score += score;
    }

    public SeaItem[][] getBoard() {
        return board;
    }

    public void setBoard(SeaItem[][] board) {
        this.board = board;
    }

    public PlayerStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(PlayerStatistics statistics) {
        this.statistics = statistics;
    }

    /* -------------- Function members -------------- */
    public Player(PlayerName name, int boardSize, int numOfShips, int numberOfMines) {
        this.numOfShips = this.remainingShips = numOfShips;
        this.name = name;
        this.boardSize = boardSize;
        this.numberOfMines = numberOfMines;
        setEmptyGrid();
    }

    private void setEmptyGrid() {
        this.board = new SeaItem[boardSize][boardSize];
        int i, j;
        for (i = 0; i < boardSize; i++) {
            for (j = 0; j < boardSize; j++) {
                board[i][j] = new WaterItem(i + 1, j + 1);
            }
        }
    }

    public char[][] getPlayerPrimaryGrid() {

        char[][] primary = getEmptyBoardForPrint(boardSize);

        int i,j;
        for (i = 0; i < boardSize; i++) {
            for (j = 0; j < boardSize; j++) {
                if(board[i][j]!=null)
                {
                    primary[i+1][j+1] = board[i][j].getItemChar();
                }
                else
                {
                    primary[i+1][j+1] = 'X';
                }
            }
        }

        return primary;
    }

    public char[][] getPlayerTrackingGrid() {
        char[][] tracking = getEmptyBoardForPrint(boardSize);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tracking[i+1][j+1] = ' ';
                if(!(board[i][j] instanceof BattleShip) && !(board[i][j] instanceof Mine)){
                    tracking[i+1][j+1] = board[i][j].getItemChar();
                }
            }
        }

        return tracking;
    }

    private char[][] getEmptyBoardForPrint(int boardSize) {

        char[][] newBoard = new char[boardSize + 1][boardSize + 1];
        char currentNumber = '1', currentLetter = 'A';

        newBoard[0][0] = ' ';

        for (int i = 1; i < boardSize + 1; i++) {
            newBoard[0][i] = currentLetter;
            currentLetter+=1;
        }

        for (int i = 1; i < boardSize + 1; i++) {
            newBoard[i][0] = currentNumber;
            currentNumber+=1;
        }

        return newBoard;
    }

    public void ShipDrowned() {
        this.remainingShips--;
    }

    public boolean IsPlayerDestroyed(){
        return this.remainingShips == 0;
    }
}
