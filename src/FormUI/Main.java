package FormUI;

import com.sun.javaws.jnl.JavaFXAppDesc;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
        primaryStage.setTitle("Battle-Ships");
        Scene scene = new Scene(root, 765, 585);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/Resources/icon.jpg"));
        primaryStage.show();
    }
}
