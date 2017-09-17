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
        checkGameType(gameSettings);
        loadGame(gameSettings);
        currentPlayer = players[0];
        winnerPlayer = null;
        return !isErrorLoading;
    }

    private void checkGameType(BattleShipGame gameSettings) {
        errorString = "";
        isErrorLoading = false;
        type = GameType.ADVANCE;
        if (GameType.BASIC.name().equalsIgnoreCase(gameSettings.getGameType())) {
            type = GameType.BASIC;
        }
        else if (!GameType.ADVANCE.name().equalsIgnoreCase(gameSettings.getGameType())) {
            type = null;
            errorString = "Game Type " + gameSettings.getGameType() +" is illegal, Please use BASIC or ADVANCE" + System.getProperty("line.separator");
            isErrorLoading = true;
        }
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
        // Initiate board.
        players[playerIndex] = new Player(name, boardSize, playerBoard.getShip().size());
        initiatePlayerBattleShips(players[playerIndex], playerBoard, shipTypesList);
    }

    private LinkedHashMap<String, Integer> createShipTypeMap(List<ShipTypes.ShipType> shipTypes){
        LinkedHashMap<String,Integer> shipTypeMap = new LinkedHashMap<>();
        for (ShipTypes.ShipType type :shipTypes) {
            if(this.type == GameType.BASIC && ShipCategories.L_SHAPE.name().equalsIgnoreCase(type.getCategory())){
                isErrorLoading = true;
                errorString += "Basic Game cannot have an L-Shape Ships!" + System.getProperty("line.separator");
            }
            else if(ShipCategories.L_SHAPE.name().equalsIgnoreCase(type.getCategory())
                    && ShipCategories.REGULAR.name().equalsIgnoreCase(type.getCategory())){
                isErrorLoading = true;
                errorString += "Bad ship category " + (type.getCategory())  + System.getProperty("line.separator");
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
        ShipCategories category = getShipCategoryByType(ships.get(index),shipTypes);
        int length = getShipLength(ships.get(index), shipTypes); // Get ship length by type.
        int score = getShipScore(ships.get(index), shipTypes); // Get ship length by type.
        int positionY = ships.get(index).getPosition().getY(); // Get y position.
        int positionX = ships.get(index).getPosition().getX(); // Get x position.
        // Create new battle ship.
        return new BattleShip(direction, shipType,length , score, positionX, positionY, category);
    }
    
    private int getShipLength(Board.Ship ship, List<ShipTypes.ShipType> shipTypes) {
        for (ShipTypes.ShipType type:shipTypes) {
            if(type.getId().equalsIgnoreCase(ship.getShipTypeId())){
                return type.getLength();
            }
        }

        return -1;
    }

    private int getShipScore(Board.Ship ship, List<ShipTypes.ShipType> shipTypes) {
        for (ShipTypes.ShipType type:shipTypes) {
            if(ship.getShipTypeId() == type.getId()){
                return type.getScore();
            }
        }

        return -1;
    }

    private ShipCategories getShipCategoryByType(Board.Ship ship, List<ShipTypes.ShipType> shipTypes) {
        for (ShipTypes.ShipType type:shipTypes) {
            if(ship.getShipTypeId().equalsIgnoreCase(type.getId())){
                if (ShipCategories.REGULAR.name().equalsIgnoreCase(type.getCategory())){
                    return ShipCategories.REGULAR;
                }
                else return ShipCategories.L_SHAPE;
            }
        }
        return ShipCategories.L_SHAPE;
    }

    private void setBattleShipToUserBoard(Player player, BattleShip playerShip) {
        if(playerShip.getShipCategory() == ShipCategories.L_SHAPE){
            setLShapeShip(player, playerShip);
        }
        else setRegularShip(player, playerShip);
    }

    private void setLShapeShip(Player player, BattleShip playerShip){
        ShipDirection shipDir= playerShip.getDirection();
        Point rowPoint,colPoint, pivotPos = playerShip.getPosition();
        rowPoint = colPoint = pivotPos;
        boolean continueAddingShip = true;
        switch (shipDir){
            case DOWN_RIGHT:
                colPoint = new Point(pivotPos.getX(),pivotPos.getY()- playerShip.getLength() + 1);
                break;
            case RIGHT_DOWN:
                rowPoint = new Point(pivotPos.getX() - playerShip.getLength() + 1,pivotPos.getY());
                colPoint = new Point(pivotPos.getX(),pivotPos.getY()+1);
                break;
            case RIGHT_UP:
                rowPoint = new Point(pivotPos.getX() - playerShip.getLength() + 1,pivotPos.getY());
                colPoint = new Point(pivotPos.getX(),pivotPos.getY()- playerShip.getLength() + 1);
                break;
            case UP_RIGHT:
                colPoint = new Point(pivotPos.getX(),pivotPos.getY() + 1);
                break;
            default:
                isErrorLoading = true;
                continueAddingShip = false;
                errorString += "L-Shape ship direction cannot be ROW  or COLUMN!" + System.getProperty("line.separator");
                break;
        }
        if(continueAddingShip){
            setRowShip(player,rowPoint,playerShip,playerShip.getLength());
            setColumnShip(player,colPoint,playerShip,playerShip.getLength()-1);
        }
    }

    private void setRegularShip(Player player, BattleShip playerShip){
        if (playerShip.getDirection() == ShipDirection.ROW) {
            setRowShip(player,playerShip.getPosition(),playerShip,playerShip.getLength());
        } else {
            setColumnShip(player,playerShip.getPosition(),playerShip,playerShip.getLength());
        }
    }

    private void setColumnShip(Player player, Point startingPoint, BattleShip playerShip,int length) {
        int y = startingPoint.getX();
        int x = startingPoint.getY();
        SeaItem[][] board = player.getBoard();
        for (int i = 0; i < length; i++) {
            // Set item to point to the battle ship. - x AND y ARE REPLACED IN ARRAY
            if (isShipOutOfBounds(x, y)) {
                isErrorLoading = true;
                errorString += "A Ship was placed outside of bounds (" + x + "," + y + "1) for " + player.getName() + System.getProperty("line.separator");
            } else {
                setShipInBoard(x - 1, y - 1, playerShip, board, player);
            }
            x++;
        }
    }

    private void setRowShip(Player player, Point startingPoint, BattleShip playerShip,int length) {
        int y = startingPoint.getX();
        int x = startingPoint.getY();
        SeaItem[][] board = player.getBoard();
        for (int i = 0; i < length; i++) {
            // Set item to point to the battle ship. - x AND y ARE REPLACED IN ARRAY
            if (isShipOutOfBounds(x, y)) {
                isErrorLoading = true;
                errorString += "A Ship was placed outside of bounds (" + x + "," + y + ") for " + player.getName() + System.getProperty("line.separator");
            } else {
                setShipInBoard(x - 1, y - 1, playerShip, board, player);
            }
            y++;
        }
    }

    private boolean isShipOutOfBounds(int x, int y){
        return x > boardSize || y > boardSize || x <=0 || y <=0;
    }

    private void setShipInBoard(int x,int y,BattleShip playerShip, SeaItem[][] board, Player player){
        if(x==1 && y==0){
            x=1;
        }
        boolean isPositionOk = board[x][y] instanceof WaterItem || board[x][y] == playerShip;
        if(x > 0){
            isPositionOk &= board[x-1][y] instanceof WaterItem || board[x-1][y] == playerShip;
            if(y>0) {
                isPositionOk &= board[x - 1][y - 1] instanceof WaterItem || board[x - 1][y - 1] == playerShip;
            }
            if(y<boardSize-1){
                isPositionOk &= board[x-1][y+1] instanceof WaterItem || board[x-1][y+1] == playerShip;
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
            // In case of battle ship hit - increase score.
            currentPlayer.AddScore(1);
            currentPlayer.getStatistics().AddHit();
            if(attackedItem.IsDestroyed()){
                result = MoveResults.Drowned;
                attackedPlayer.ShipDrowned();
            }
            if(attackedPlayer.IsPlayerDestroyed()) {
                winnerPlayer = currentPlayer;
                status = GameStatus.OVER;
            }
        }
        return result;
    }
}
