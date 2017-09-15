package FormUI;

import BattleShipsLogic.GameObjects.GameManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.*;

public class Controller {

    // ----------------------- Declaration of variables ----------------------- //

    static final int NANO_SECONDS_IN_SECOND = 1000000000;

    protected GameManager theGame;

    @FXML private Button loadGame;
    @FXML private Button startGame;
    @FXML private Button attack;
    @FXML private Button gameStatistics;
    @FXML private Button quit;

    // ----------------------- Controller methods ----------------------- //

    @FXML
    public void loadButtonClick() {
        System.out.println("stam");
    }
}
