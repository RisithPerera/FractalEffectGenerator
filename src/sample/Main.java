package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static long startTime;

    @Override
    public void start(Stage primaryStage) {
        new Mandelbrot(primaryStage);
    }

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        launch(args);
    }
}