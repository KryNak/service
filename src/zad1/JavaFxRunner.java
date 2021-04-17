package zad1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JavaFxRunner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TPO App");
        primaryStage.getIcons().add(new Image("zad1/resources/baseline_smart_toy_black_48dp.png"));

        Pane root = FXMLLoader.load(getClass().getResource("view/main-stage.fxml"));
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add("zad1/resources/css/main-stage-stylesheet.css");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

}
