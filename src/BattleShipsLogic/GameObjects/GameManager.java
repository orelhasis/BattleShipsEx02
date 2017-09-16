package BattleShipsLogic.GameObjects;

import BattleShipsLogic.Definitions.*;
import BattleShipsLogic.GameSettings.BattleShipGame;
import BattleShipsLogic.GameSettings.BattleShipGame.Boards.Board;
import BattleShipsLogic.GameSettings.BattleShipGame.ShipTypes;

import java.util.LinkedHashMap;
import java.util.List;

public class GameManager extends java.util.Observable{

    /* -------------- Data members -------------- */

    private Player[] players = new Player[2];
    private GameType type;
    private GameStatus status;
    private Player currentPlayer;
    private Player winnerPlayer;
    private int boardSize;
    private int startTimeInSeconds;
    private boolean isErrorLoading;
    private String errorString;

    /* -------------- Getters and setters -------------- */

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setWinnerPlayer(Player winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer=currentPlayer;
    }

    public int getBoarSize(){
        return this.boardSize;
    }

    public String getLoadingError() {
        return errorString;
    }

    public int getStartTime() {
        return startTimeInSeconds;
    }

    public void setStartTime(int startTimeInSeconds) {
        this.startTimeInSeconds = startTimeInSeconds;
    }

    /* -------------- Function members -------------- */

    public GameManager() {

            status = GameStatus.INIT;
            errorString = "";
    }

    public boolean LoadGame(BattleShipGame gameSettings){
        errorString = "";
        isErrorLoading = false;
        if (GameType.BASIC == GameType.valueOf(gameSettings.getGameType()) || GameType.ADVANCE == GameType.valueOf(gameSettings.getGameType())) {
            type = GameType.valueOf(gameSettings.getGameType());
        }
        else {
            isErrorLoading = true;
            errorString += "Game type is undefined";
        }

        loadGame(gameSettings);
        currentPlayer = players[0];
        winnerPlayer = null;

        return !isErrorLoading;
    }

    private void loadGame(BattleShipGame gameSettings) {
        this.boardSize =  gameSettings.getBoardSize();
        initializePlayer(gameSettings, PlayerName.PLAYER_1);
        initializePlayer(gameSettings, PlayerName.PLAYER_2);
    }

    private void initializePlayer(BattleShipGame gameSettings, PlayerName name) {

        // Get board size.
        int boardSize = gameSettings.getBoardSize();
        // Get Player board.
        int playerIndex = 1;
        if(name == PlayerName.PLAYER_1) {
            playerIndex = 0;
        }

        // Get Game ship types and player board.
        List<ShipTypes.ShipType> shipTypesList = gameSettings.getShipTypes().getShipType();
        Board playerBoard = gameSettings.getBoards().getBoard().get(playerIndex);

        // Initiate player.
        players[playerIndex] = new Player(name, boardSize, playerBoard.getShip().size(), gameSettings.getMine().getAmount());
        initiatePlayerBattleShips(players[playerIndex], playerBoard, shipTypesList);
    }

    private LinkedHashMap<String, Integer> createShipTypeMap(List<ShipTypes.ShipType> shipTypes){
        LinkedHashMap<String,Integer> shipTypeMap = new LinkedHashMap<>();
        for (ShipTypes.ShipType type :shipTypes) {
            if(this.type == GameType.BASIC && type.getCategory().equals(ShipCategories.L_SHAPE.name())){
                isErrorLoading = true;
                errorString += "Basic Game cannot have an L-Shape Ships!" + System.getProperty("line.separator");
            }
            else if(!type.getCategory().equals(ShipCategories.L_SHAPE.name()) && !type.getCategory().equals(ShipCategories.REGULAR.name())){
                isErrorLoading = true;
                errorString += "Bad ship category "+ type.getCategory() + System.getProperty("line.separator");
            }
            shipTypeMap.put(type.getId(),0);
        }
        return shipTypeMap;
    }
    // Get player spaceships.
    private void initiatePlayerBattleShips(Player player, Board playerBoard, List<ShipTypes.ShipType> shipTypes) {
        List<Board.Ship> ship = playerBoard.getShip();

        BattleShip playerShip;
        LinkedHashMap<String,Integer> shipCountMap = createShipTypeMap(shipTypes);
        String currentType;
        for(int i=0; i < ship.size(); i++) {
            currentType = ship.get(i).getShipTypeId();
            if(!shipCountMap.containsKey(currentType)){
                isErrorLoading = true;
                errorString += currentType + " Ship Type in " + player.getName() + " is not defined as a ship type" + System.getProperty("line.separator");
            }else {
                shipCountMap.put(currentType, shipCountMap.get(currentType) + 1);
                playerShip = createAShip(ship, i, shipTypes);
                // Set battle ship to user board.
                setBattleShipToUserBoard(player, playerShip);
            }
        }
        checkShipCount(shipTypes, shipCountMap, player);
    }

    private void checkShipCount(List<ShipTypes.ShipType> shipTypes, LinkedHashMap<String,Integer> actualShips, Player  player){
        for (ShipTypes.ShipType type :shipTypes) {
            if(type.getAmount() != actualShips.get(type.getId())){
                isErrorLoading = true;
                errorString += player.getName() + " has " + actualShips.get(type.getId()) +" ships of type " + type.getId() + " instead of "+  type.getAmount() + System.getProperty("line.separator");
            }
        }
    }

    private BattleShip createAShip(List<Board.Ship> ships, int index, List<ShipTypes.ShipType> shipTypes){
        ShipDirection direction = ShipDirection.valueOf(ships.get(index).getDirection()); // Get battle ship direction.
        String shipType = ships.get(index).getShipTypeId(); // Get battle ship type.
        int length = getShipLength(ships.get(index), shipTypes); // Get ship length by type.
        int score = getShipScore(ships.get(index), shipTypes); // Get ship length by type.
        int positionY = ships.get(index).getPosition().getY(); // Get y position.
        int positionX = ships.get(index).getPosition().getX(); // Get x position.
        // Create new battle ship.
        return new BattleShip(direction, shipType,length , score, positionX, positionY);
    }
    
    private int getShipLength(Board.Ship ship, List<ShipTypes.ShipType> shipTypes) {
        for (ShipTypes.ShipType type:shipTypes) {
            if(ship.getShipTypeId().equals(type.getId())){
                return type.getLength();
            }
        }

        return -1;
    }

    private int getShipScore(Board.Ship ship, List<ShipTypes.ShipType> shipTypes) {
        for (ShipTypes.ShipType type:shipTypes) {
            if(ship.getShipTypeId().equals(type.getId())){
                return type.getScore();
            }
        }

        return -1;
    }

    private void setBattleShipToUserBoard(Player player, BattleShip playerShip) {
        Point position = new Point(playerShip.getPosition().getX(), playerShip.getPosition().getY());
        SeaItem[][] board = player.getBoard();
        for(int i = 0 ; i< playerShip.getLength(); i++) {
            // Set item to point to the battle ship. - x AND y ARE REPLACED IN ARRAY
            if(isShipOutOfBounds(position.getX(),position.getY())){
                isErrorLoading = true;
                errorString+= "A Ship was placed outside of bounds (" + position.getX() +","+ position.getY() + ") for " + player.getName() + System.getProperty("line.separator");
            }else {
                setShipInBoard(position.getX() - 1,position.getY() - 1, playerShip, board, player);

                // Move to next item that should point the battle ship.
                if (playerShip.getDirection() == ShipDirection.ROW) {
                    position.setY(position.getY() + 1);
                } else {
                    position.setX(position.getX() + 1);
                }
            }
        }
    }

    private boolean isShipOutOfBounds(int x, int y){
        return x > boardSize || y > boardSize || x <=0 || y <=0;
    }

    private void setShipInBoard(int x,int y,BattleShip playerShip, SeaItem[][] board, Player player){
        boolean isPositionOk = board[x][y] instanceof WaterItem;
        if(x > 0){
            isPositionOk &= board[x-1][y] instanceof WaterItem || board[x-1][y] == playerShip;
            if(y>0) {
                isPositionOk &= board[x - 1][y - 1] instanceof WaterItem || board[x - 1][y - 1] == playerShip;
            }
            if(y<boardSize-1){
                isPositionOk &= board[x-1][y+1] instanceof WaterItem || board[x][y+1] == playerShip;
            }
        }
        if(y>0) {
            isPositionOk &= board[x][y - 1] instanceof WaterItem || board[x][y - 1] == playerShip;
        }
        if(x < boardSize-1){
            isPositionOk &= board[x+1][y] instanceof WaterItem || board[x+1][y] == playerShip;
            if(y<boardSize-1){
                isPositionOk &= board[x+1][y+1] instanceof WaterItem || board[x+1][y+1] == playerShip;
            }
            if(y>0){
                isPositionOk &= board[x+1][y-1] instanceof WaterItem || board[x+1][y-1] == playerShip;
            }
        }
        if(y<boardSize-1){
            isPositionOk &= board[x][y+1] instanceof WaterItem || board[x][y+1] == playerShip;
        }
        if (isPositionOk){
            board[x][y] = playerShip;
        }
        else{
            isErrorLoading = true;
            errorString += "Ships are overlapping in " + player.getName() + " board at (" + (y+1) + "," + (x+1) +")" + System.getProperty("line.separator");
        }
    }

    private void updateStatistics(int moveTime){
        int numberOfTurns = currentPlayer.getStatistics().getNumberOfTurns();
        int averageTimeOfTurn = currentPlayer.getStatistics().getAverageTimeForTurn();
        int newAverage = ((numberOfTurns*averageTimeOfTurn)+moveTime)/(numberOfTurns+1);
        currentPlayer.getStatistics().setAverageTimeForTurn(newAverage);
        currentPlayer.getStatistics().setNumberOfTurns(numberOfTurns+1);
    }

    public MoveResults makeMove(Point attackedPoint, int moveTime) {
        MoveResults result = MoveResults.Miss;
        Player attackedPlayer = players[0];
        if(currentPlayer == players[0]) {
            attackedPlayer = players[1];
        }
        // Update current player statistics.
        updateStatistics(moveTime);

        // Get attacked item in the attacked player grid.
        int x = attackedPoint.getX();
        int y = attackedPoint.getY();
        SeaItem attackedItem = attackedPlayer.getBoard()[x][y];
        if(attackedItem.IsDestroyed()){
            result = MoveResults.Used;
        }
        else if(attackedItem instanceof WaterItem)
        {
            result = MoveResults.Miss;
            attackedItem.GotHit();
            currentPlayer.getStatistics().AddMiss();
        }
        else if (attackedItem instanceof BattleShip)
        {
            result = MoveResults.Hit;
            attackedItem.GotHit();
            attackedPlayer.getBoard()[x][y] = new ShipRemains(x, y);

            currentPlayer.getStatistics().AddHit();
            if(attackedItem.IsDestroyed()){
                result = MoveResults.Drowned;
                attackedPlayer.ShipDrowned();
                currentPlayer.AddScore(attackedItem.GetScore());
            }
            if(attackedPlayer.IsPlayerDestroyed()) {
                winnerPlayer = currentPlayer;
                status = GameStatus.OVER;
            }
        }
        else if (attackedItem instanceof Mine)
        {
            attackedItem.GotHit();
            result = handleMineAttack(attackedPoint);
        }
        return result;
    }

    private MoveResults handleMineAttack(Point attackedPoint) {

        MoveResults result = MoveResults.Mine;
        Player attackedPlayer = players[0];
        if(currentPlayer == players[0]) {
            attackedPlayer = players[1];
        }

        SeaItem attackedItem = currentPlayer.getBoard()[attackedPoint.getX()][attackedPoint.getY()];

        // Case 1: The match coordinate in the current player board is a water.
        if(attackedItem instanceof WaterItem)
        {
            attackedItem.GotHit();
        }
        // Case 2: The match coordinate in the current player board is a battleship.
        else if (attackedItem instanceof BattleShip)
        {
            attackedItem.GotHit();
            currentPlayer.getBoard()[attackedPoint.getX()][attackedPoint.getY()] = new ShipRemains(attackedPoint.getX(), attackedPoint.getY());
            if(attackedItem.IsDestroyed()){
                currentPlayer.ShipDrowned();
                attackedPlayer.AddScore(attackedItem.GetScore());
            }
            if(currentPlayer.IsPlayerDestroyed()) {
                winnerPlayer = attackedPlayer;
                status = GameStatus.OVER;
            }
        }
        // Case 3: The match coordinate in the current player board is a mine.
        else if (attackedItem instanceof Mine)
        {
            attackedItem.GotHit();
        }

        // Add hit to the player only if his battle ship wasn't hit.
        if(!(attackedItem instanceof BattleShip))
        {
            currentPlayer.getStatistics().AddHit();
        }

        return result;
    }
}
