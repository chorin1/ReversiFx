import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GameLayout.fxml"));
        primaryStage.setTitle("ReversiFx");
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add("game.css");

/*
        final ImageView imageView = new ImageView("img/newAssetsForReversi.png");
        imageView.setViewport(new Rectangle2D(0, 0, 59, 59));
        final Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(1000),
                9, 3,
                0, 0,
                59, 59
        );
        animation.setCycleCount(2);
        animation.play();
        primaryStage.setScene(new Scene(new Group(imageView)));

*/
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
