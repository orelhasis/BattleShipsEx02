package FormUI;

import BattleShipsLogic.GameObjects.GameManager;
import ConsoleUI.BattleShipUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.*;
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

    // ----------------------- BattleShipFXUI methods ----------------------- //

    @FXML
    public void loadButtonClick() {
        if(!super.loadGame()){
            startAlert("Failed to load","Could not load game file",UIloadingError);
        }
        else{
            startAlert("Game Loaded","Successfully loaded the game","You can now start");
        }
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

    }

    @Override
    protected void showGameLoadFailedMessage() {

    }

    @Override
    public void StartGame() {

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
