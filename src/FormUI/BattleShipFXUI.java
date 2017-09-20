package FormUI;

import BattleShipsLogic.Definitions.GameStatus;
import BattleShipsLogic.Definitions.PlayerName;
import BattleShipsLogic.GameObjects.GameManager;
import BattleShipsLogic.GameObjects.Player;
import BattleShipsLogic.GameObjects.Point;
import ConsoleUI.BattleShipUI;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Observable;

public class BattleShipFXUI extends BattleShipUI {

    // ----------------------- Declaration of variables ----------------------- //

    static final int NANO_SECONDS_IN_SECOND = 1000000000;
    static final int CELL_IMAGE_SIZE = 40;
    static final String WATER_URL = "\\Resources\\water.png";
    static final String HIT_WATER_URL = "\\Resources\\hitwater.png";
    static final String BATTLESHIP_URL = "\\Resources\\battleship.png";
    static final String HIT_BATTLESHIP_URL = "\\Resources\\hitbattleship.png";
    static final String MINE_URL = "\\Resources\\mine.png";
    static final String HIT_MINE_URL = "\\Resources\\hitmine.png";

    static final int START_GAME = 2;
    static final int GET_GAME_STATUS = 3;

    @FXML private Button loadGame;
    @FXML private Button startGame;
    @FXML private Button gameStatistics;
    @FXML private Button quit;
    @FXML private ScrollPane opponentGridArea;
    @FXML private ScrollPane playerGridArea;
    @FXML private Button prevMove;
    @FXML private Button nextMove;
    @FXML private Label moveNumber;
    @FXML private Label player1Score;
    @FXML private Label player2Score;
    @FXML private Label player1Mines;
    @FXML private Label player2Mines;

    // ----------------------- BattleShipFXUI methods ----------------------- //

    @FXML
    public void loadButtonClick() {
        if(!super.loadGame()){
            showGameLoadFailedMessage();
            hideGameButtons();
        }
        else{
            showGameLoadedMessage();
            showGamesButtons();
        }
    }

    @FXML
    protected void initialize(){
        hideGameButtons();
    }
    private void showGamesButtons() {
        startGame.setDisable(false);
        gameStatistics.setDisable(false);
        quit.setDisable(false);
    }

    private void hideGameButtons() {
        startGame.setDisable(true);
        gameStatistics.setDisable(true);
        quit.setDisable(true);
    }


    @FXML
    public void previousMoveButtonClick() {
        int val = Integer.parseInt(moveNumber.getText());
        if(val>1)
            moveNumber.setText(Integer.toString(val-1));
        showHistory();
    }

    @FXML
    public void nextMoveButtonClick() {
        int val = Integer.parseInt(moveNumber.getText());
        if(val<theGame.getGameHistory().size())
            moveNumber.setText(Integer.toString(val+1));
        showHistory();
    }

    private void showHistory() {
        int val = Integer.parseInt(moveNumber.getText());
        int indexInHistory = val-1;

        PlayerName attackPlayerName = PlayerName.PLAYER_1;
        PlayerName attackedPlayerName = PlayerName.PLAYER_2;
        if(theGame.getGameHistory().get(indexInHistory).getCurrentPlayerName() == PlayerName.PLAYER_2){
            attackPlayerName = PlayerName.PLAYER_2;
            attackedPlayerName = PlayerName.PLAYER_1;
        }

        showBoards(theGame.getGameHistory().get(indexInHistory).getPrimaryBoard(), theGame.getGameHistory().get(indexInHistory).getTrackingBoard(), attackPlayerName.toString(), attackedPlayerName.toString());
        player1Score.setText(Integer.toString(theGame.getGameHistory().get(indexInHistory).getPlayer1Score()));
        player2Score.setText(Integer.toString(theGame.getGameHistory().get(indexInHistory).getPlayer2Score()));
        player1Mines.setText(Integer.toString(theGame.getGameHistory().get(indexInHistory).getPlayer1Mines()));
        player2Mines.setText(Integer.toString(theGame.getGameHistory().get(indexInHistory).getPlayer2Mines()));
    }

    @Override
    protected String getFilePath() {
        File selectedFile;
        FileChooser fileChooser = new FileChooser();
        String filePath = null;
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        selectedFile =fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            filePath = selectedFile.getAbsolutePath();
        }
        return filePath;
    }

    private void startAlert(String Title, String header, String content) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(Title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.showAndWait();
    }

    @Override
    protected void showWelcomeMessage() {

    }

    @Override
    protected void showGameLoadedMessage() {
        startAlert("Game Loaded","Successfully loaded the game","You can now start");
    }

    @Override
    protected void showGameLoadFailedMessage() {
        startAlert("Failed to load","Could not load game file",UIloadingError);
    }

    @Override
    @FXML
    public void StartGame() {
        theGame.LoadGame(theGame.getGameSettings());
        createGrids();
        updateInfo();
        theGame.setStartTime((int)(System.nanoTime()/NANO_SECONDS_IN_SECOND));
        theGame.setCurrentTurnStartTimeInSeconds((int)(System.nanoTime()/NANO_SECONDS_IN_SECOND));
        theGame.setStatus(GameStatus.RUN);
        showBoards(theGame.getCurrentPlayer());
        startAlert("Game Start", "Let the battle begin!", "Its now Player 1 turn");
    }

    private void createGrids() {
        int boardSize = theGame.getBoarSize();
        GridPane playerGrid = new GridPane();
        GridPane opponentGrid = new GridPane();
        for (int r = 0; r < boardSize; r++) {
            addRowConstrain(playerGrid);
            addRowConstrain(opponentGrid );
            for (int c = 0; c < boardSize; c++) {
                if(r == c && r==0){
                    addColConstrain(playerGrid);
                    addColConstrain(opponentGrid );
                }
                playerGrid.setAlignment(Pos.CENTER);
                addPane(playerGrid,c,r, true);
                opponentGrid.setAlignment(Pos.CENTER);
                addPane(opponentGrid ,c,r, false);

            }
        }
        opponentGridArea.setContent(opponentGrid);
        playerGridArea.setContent(playerGrid);
        opponentGridArea.autosize();
        playerGridArea.autosize();
    }

    private void addColConstrain(GridPane grid) {
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().add(colConstraints);
    }

    private void addRowConstrain(GridPane grid) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.SOMETIMES);
        grid.getRowConstraints().add(rowConstraints);
    }

    private void addPane(GridPane grid,int colIndex, int rowIndex, boolean isPlayerBoard) {
        Pane pane = new Pane();
        ImageView iv =new ImageView();
        pane.getChildren().add(iv);
        if (isPlayerBoard){
            setOnClickSetMine(pane,colIndex,rowIndex);
        }
        else{
            setOnclickAttackBoard(pane,colIndex,rowIndex);
        }
        grid.add(pane, colIndex, rowIndex);
    }

    private void setOnClickSetMine(Pane pane, int colIndex, int rowIndex) {

        pane.setOnMouseClicked(e -> {
            Point minePoint = new Point(rowIndex, colIndex);
            if(setMineInPosition(minePoint)){
                updateTurnStatistics();
                updateInfo();
                theGame.saveMove();
                showAMineWasSetMessage();
                swapPlayers();
                showBoards(theGame.getCurrentPlayer());
            }else{
                showNoMoreMineMessage();
            }
        });
    }

    private void setOnclickAttackBoard(Pane pane, int colIndex, int rowIndex) {

        pane.setOnMouseClicked(e -> {
            int moveTime = updateTurnStatistics();
            Point attackedPoint = new Point(rowIndex, colIndex);
            showMoveResults(theGame.makeMove(attackedPoint,moveTime));
            theGame.saveMove();
            showBoards(theGame.getCurrentPlayer());
            if(theGame.getStatus() == GameStatus.OVER) {
                handleGameOver();
                hideGameButtons();
            }
        });
    }

    private int updateTurnStatistics() {
        int moveTime = (int) ((System.nanoTime()/NANO_SECONDS_IN_SECOND) - theGame.getCurrentTurnStartTimeInSeconds());
        theGame.setCurrentTurnStartTimeInSeconds((int)(System.nanoTime()/NANO_SECONDS_IN_SECOND));
        return moveTime;
    }

    private void handleGameOver() {
        prevMove.setDisable(false);
        nextMove.setDisable(false);
        startAlert("Game Over", theGame.getWinnerPlayer().getName().toString() + " you are the winner!", "Thank you and good-bye!");
    }

    private ImageView getCellImageView(Point point,GridPane  grid) {
        for (Node d:grid.getChildren()) {
            if(grid.getRowIndex(d) == point.getY() && grid.getColumnIndex(d) == point.getX()){
                return (ImageView)((Pane)d).getChildren().get(0);
            }
        }
        return null;
    }

    private void updateImage(ImageView iv, char repChar){
        Image img = null;
        switch (repChar){
            case 'S':
                img = getImage(BATTLESHIP_URL);
                break;
            case 'O':
                img = getImage(HIT_BATTLESHIP_URL);
                break;
            case 'M':
                img = getImage(MINE_URL);
                break;
            case 'E':
                img = getImage(HIT_MINE_URL);
                break;
            case 'X':
                img = getImage(HIT_WATER_URL);
                break;
            default:
                img = getImage(WATER_URL);
                break;
        }
        iv.setImage(img);
    }

    private Image getImage(String url) {
        return new Image(url,CELL_IMAGE_SIZE,CELL_IMAGE_SIZE,false,true);
    }


    @Override
    protected void printGameStartsMessage() {

    }

    @Override
    protected void publishWinnerResults() {

    }

    @Override
    @FXML
    protected void printStatistics() {
        String statistics,  nl = System.getProperty("line.separator");
        statistics = "Total number of turns: " + (theGame.getPlayers()[0].getStatistics().getNumberOfTurns() + theGame.getPlayers()[1].getStatistics().getNumberOfTurns()) + nl;
        statistics += "Time elapsed: " + calcTime((int) ((System.nanoTime()/NANO_SECONDS_IN_SECOND) - theGame.getStartTime())) + nl;
        statistics += "Number of hits: " + theGame.getPlayers()[0].getName() + " - " + theGame.getPlayers()[0].getStatistics().getNumberOfHits() + ", " + theGame.getPlayers()[1].getName() + " - " + theGame.getPlayers()[1].getStatistics().getNumberOfHits() + "." + nl;
        statistics += "Number of misses: " + theGame.getPlayers()[0].getName() + " - " + theGame.getPlayers()[0].getStatistics().getNumberOfMissing() + ", " + theGame.getPlayers()[1].getName() + " - " + theGame.getPlayers()[1].getStatistics().getNumberOfMissing() + "." + nl;
        statistics += "Average time for attack: " + theGame.getPlayers()[0].getName() + " - " + calcTime(theGame.getPlayers()[0].getStatistics().getAverageTimeForTurn()) + ", " + theGame.getPlayers()[1].getName() + " - " + calcTime(theGame.getPlayers()[1].getStatistics().getAverageTimeForTurn()) + "." + nl;
        startAlert("Statistics","Current Game Statistics", statistics);
    }

    @FXML
    protected void quitGame() {
        startAlert("Quit Game", theGame.getCurrentPlayer().getName() +" left the game", "Thank you and good-bye!");
        prevMove.setDisable(false);
        nextMove.setDisable(false);
        hideGameButtons();
    }

    @Override
    protected void showBoards(char[][] board, char[][] trackingBoard, String attackPlayerName, String attackedPlayerName) {
        GridPane opponentGrid = (GridPane) opponentGridArea.getContent();
        GridPane playerGrid = (GridPane) playerGridArea.getContent();
        for(int i=1;i<=theGame.getBoarSize();i++){
            for(int j=1;j<=theGame.getBoarSize();j++){
                updateImage(getCellImageView(new Point(i-1,j-1),opponentGrid),trackingBoard[j][i]);
                updateImage(getCellImageView(new Point(i-1,j-1),playerGrid),board[j][i]);
            }
        }
    }

    @Override
    protected void showUsedMessage() {
        startAlert("Try Again", "This position was previously attacked","Choose another position to attack");
    }

    protected void showAMineWasSetMessage() {
        startAlert("Mine muahaha!", "You have set a mine","Lets wait for them to hit it!, it is now " + theGame.getNextPlayer().getName() + " Turn");
    }

    protected void showNoMoreMineMessage() {
        startAlert("No Mine", "Could not add a mine","You are out of mines");
    }

    @Override
    protected void showDrownedMessage() {
        startAlert("Destroyed!!!", "You have destroyed an opponent ship!",theGame.getCurrentPlayer().getName().toString() + " have another turn");
        updateInfo();
    }

    @Override
    protected void showHitAMineMessage() {
        startAlert("oh Shoot!", "You hit a mine...","and attacked yourself in that position");
    }

    @Override
    protected void showMissMessage() {
        startAlert("Miss!", "You have Missed!","It is now " + theGame.getNextPlayer().getName().toString() + " turn");
    }

    @Override
    protected void showHitMessage() {
        startAlert("Hit!", "You have hit an opponent ship!",theGame.getCurrentPlayer().getName().toString() + " have another turn");
    }

    @Override
    protected void showBoard(char[][] board) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    private void updateInfo() {
        player1Score.setText(Integer.toString(theGame.getPlayers()[0].getScore()));
        player2Score.setText(Integer.toString(theGame.getPlayers()[1].getScore()));
        player1Mines.setText(Integer.toString(theGame.getPlayers()[0].getNumberOfMines()));
        player2Mines.setText(Integer.toString(theGame.getPlayers()[1].getNumberOfMines()));

    }
}