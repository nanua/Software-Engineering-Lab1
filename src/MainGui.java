import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class MainGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    public static Graph graph;



    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(getClass().getResource("Home.fxml"));
        FXMLLoader.load(getClass().getResource("OpenFile.fxml"));
        FXMLLoader.load(getClass().getResource("ShowImage.fxml"));
        FXMLLoader.load(getClass().getResource("ShowBridgeWords.fxml"));
        FXMLLoader.load(getClass().getResource("ShowShortestPaths.fxml"));
        primaryStage.setTitle("软设实验一");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("main.ico")));
        Home.homeController.contentPane.setCenter
                (OpenFile.openFileController.openFilePane);
        Scene scene = new Scene(root, 900, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * 结束时清理
     * @throws Exception
     */
    @Override
    public void stop() throws Exception{
        try {
            Arrays.asList(new File(".").listFiles()).forEach(file -> {
                if (file.toString().endsWith(".png") || file.toString().endsWith(".dot")) {
                    file.delete();
                }
            });
        }catch (Throwable e) {

        }
        super.stop();
    }
}