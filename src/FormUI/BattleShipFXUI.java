package FormUI;

import BattleShipsLogic.Definitions.MoveResults;
import BattleShipsLogic.GameObjects.GameManager;
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

    protected GameManager theGame;

    @FXML private Button loadGame;
    @FXML private Button startGame;
    @FXML private Button attack;
    @FXML private Button gameStatistics;
    @FXML private Button quit;
    @FXML private AnchorPane opponentGridArea;
    private Image[][] playerBoard;
    private Image[][] trackingBoard;

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
        startAlert("Failed to load","Could not load game file",UIloadingError);
    }

    @Override
    @FXML
    public void StartGame() {
        createGrids();
    }

    private void createGrids() {
        int boardSize = 10;//theGame.getBoarSize();
        GridPane grid = new GridPane();
        for (int r = 0; r < boardSize; r++) {
            addRowConstrain(grid);
            for (int c = 0; c < boardSize; c++) {
                if(r == c && r==0){
                    addColConstrain(grid);
                }
                addPane(grid,c,r);
            }
        }
        opponentGridArea.getChildren().add(grid);
        opponentGridArea.autosize();
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

    private void addPane(GridPane grid,int colIndex, int rowIndex) {
        Pane pane = new Pane();
        Image img = new Image("\\Resources\\water.png",40,40,false,true);
        ImageView iv =new ImageView(img);
        pane.getChildren().add(iv);
        pane.setOnMouseClicked(e -> {
            long startMoveTime = System.nanoTime();
            int moveTime = (int) ((System.nanoTime() - startMoveTime)/NANO_SECONDS_IN_SECOND); // Calculate time for a move in seconds.
            Point attackedPoint = new Point(colIndex,rowIndex);
            updateBoard(theGame.makeMove(attackedPoint,moveTime),attackedPoint);
        });
        grid.add(pane, colIndex, rowIndex);
    }

    private void updateBoard(MoveResults moveResults, Point attackedPoint) {
        for (Node d:((GridPane)opponentGridArea.getChildren().get(0)).getChildren()) {
            if(d.get)
        }

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
    protected void showBoards(char[][] board, char[][] trackingboard, String attackPlayerName, String attackedPlayerName) {

    }

    @Override
    protected void showUsedMessage() {

    }

    @Override
    protected void showDrownedMessage() {

    }

    @Override
    protected void showHitAMineMessage() {

    }

    @Override
    protected void showMissMessage() {

    }

    @Override
    protected void showHitMessage() {

    }

    @Override
    protected void showBoard(char[][] board) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
