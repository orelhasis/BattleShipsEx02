package FormUI;

import BattleShipsLogic.Definitions.MoveResults;
import BattleShipsLogic.GameObjects.Player;
import BattleShipsLogic.GameObjects.Point;
import ConsoleUI.BattleShipUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
    @FXML private Button attack;
    @FXML private Button gameStatistics;
    @FXML private Button quit;
    @FXML private AnchorPane opponentGridArea;
    @FXML private AnchorPane playerGridArea;
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
        startGame.setVisible(true);
        attack.setVisible(true);
        gameStatistics.setVisible(true);
    }

    private void hideGameButtons() {
        //startGame.setVisible(false);
        attack.setVisible(false);
        gameStatistics.setVisible(false);
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
        startAlert("Failed to load","Could not load game file",UIloadingError + theGame.getLoadingError());
    }

    @Override
    @FXML
    public void StartGame() {
        createGrids();
        showBoards(theGame.getCurrentPlayer());
    }

    private void createGrids() {
        int boardSize = 10;//theGame.getBoarSize();
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
                addPane(playerGrid,c,r, true);
                addPane(opponentGrid ,c,r, false);
            }
        }
        opponentGridArea.getChildren().add(opponentGrid);
        playerGridArea.getChildren().add(playerGrid);
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
        //TODO: add a check if in history mode
        pane.setOnMouseClicked(e -> {
            Point minePoint = new Point(colIndex,rowIndex);
            if(setMineInPosition(minePoint)){
                showAMineWasSetMessage();
                swapPlayers();
                showBoards(theGame.getCurrentPlayer());
            }else{
                showNoMoreMineMessage();
            }
        });
    }

    private void setOnclickAttackBoard(Pane pane, int colIndex, int rowIndex) {
        //TODO: add a check if in history mode
        pane.setOnMouseClicked(e -> {
            long startMoveTime = System.nanoTime();
            int moveTime = (int) ((System.nanoTime() - startMoveTime)/NANO_SECONDS_IN_SECOND); // Calculate time for a move in seconds.
            Point attackedPoint = new Point(colIndex,rowIndex);
            showMoveResults(theGame.makeMove(attackedPoint,moveTime));
            showBoards(theGame.getCurrentPlayer());
        });
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
    protected void printStatistics() {

    }

    @Override
    protected void showBoards(char[][] board, char[][] trackingBoard, String attackPlayerName, String attackedPlayerName) {
        GridPane opponentGrid = (GridPane) opponentGridArea.getChildren().get(0);
        GridPane playerGrid = (GridPane) playerGridArea.getChildren().get(0);
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
}