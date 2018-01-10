import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("GameLayout.fxml"));
            Parent settings = FXMLLoader.load(getClass().getResource("SettingsLayout.fxml"));
            primaryStage.setTitle("ReversiFx");
            Scene gameScene = new Scene(root, 1280, 720);
            Scene settingsScene = new Scene(settings, 300, 400);
            gameScene.getStylesheets().add("game.css");
            //primaryStage.setScene(gameScene);
            primaryStage.setScene(settingsScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
