package com.ReversiFx;

import com.ReversiFx.controllersViews.GameController;
import com.ReversiFx.controllersViews.SettingsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.ReversiFx.model.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // set up settings stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("controllersViews/SettingsLayout.fxml"));
            Parent settings = loader.load();
            Scene settingsScene = new Scene(settings, 300, 500);
            Stage settingsPopup = new Stage();
            settingsPopup.setTitle("Settings");
            settingsPopup.setScene(settingsScene);
            settingsPopup.initModality(Modality.WINDOW_MODAL);
            settingsPopup.initOwner(primaryStage);
            settingsPopup.setResizable(false);
            SettingsController setController = (SettingsController) loader.getController();

            // set up model
            Model model = new Model(setController.getBoardSize());
            // set up game stage
            loader = new FXMLLoader(getClass().getResource("controllersViews/GameLayout.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("ReversiFx");
            Scene gameScene = new Scene(root, 1280, 720);
            gameScene.getStylesheets().add("com/ReversiFx/controllersViews/game.css");
            primaryStage.setScene(gameScene);
            GameController gameController = (GameController) loader.getController();

            gameController.initSettings(setController, settingsPopup);
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
