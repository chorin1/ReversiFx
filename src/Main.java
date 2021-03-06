import assets.Assets;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.*;
import controllersViews.*;

/**
 * ReversiFX, a Reversi PVP game done by Javafx
 * @author      Ben C
 * @version     0.6
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // set up settings stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("controllersViews/SettingsLayout.fxml"));
            Parent settings = loader.load();
            Scene settingsScene = new Scene(settings, 300, 500);
            Stage settingsPopup = new Stage();
            settingsPopup.setTitle("Settings");
            settingsPopup.getIcons().add(Assets.getInstance().getAppImage("icon"));
            settingsPopup.setScene(settingsScene);
            settingsPopup.initModality(Modality.WINDOW_MODAL);
            settingsPopup.initOwner(primaryStage);
            settingsPopup.setResizable(false);
            SettingsController settingsController = loader.getController();

            // set up model (from user preferences)
            Model model = new Model(settingsController.getBoardSize());
            // set up game stage
            loader = new FXMLLoader(getClass().getResource("controllersViews/GameLayout.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("ReversiFx");
            Scene gameScene = new Scene(root, 1280, 720);
            gameScene.getStylesheets().add("controllersViews/game.css");
            primaryStage.getIcons().add(Assets.getInstance().getAppImage("icon"));
            primaryStage.setScene(gameScene);
            GameController gameController = loader.getController();

            gameController.initSettings(settingsController, settingsPopup);
            gameController.setModel(model);
            gameController.initBoard();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
