package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;


public class Triangle {
    Timeline timer;

    private Point[] triangle;
    private static int points = 100000;
    private static Point midPoint;

    private int width = 600, height = 600;
    public Triangle(Stage primaryStage) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(new Group(canvas));
        primaryStage.setScene(scene);
        primaryStage.show();

        Random ran = new Random();
        triangle = new Point[3];
        triangle[1] = new Point(0d, 0d);
        triangle[0] = new Point(width / 2d, (double) height);
        triangle[2] = new Point((double) width, 0d);
        midPoint = Point.findMidTo(triangle[0], triangle[1], triangle[2]);

        timer = new Timeline(new KeyFrame(Duration.millis(.1), (ActionEvent event) -> {
            if (points-- > 0) {
                int r = ran.nextInt(3);
                midPoint = midPoint.findMidTo(triangle[r]);
                gc.fillOval(midPoint.getX(), midPoint.getY(), 1, 1);
            }else{
                System.out.println("Stopping!");
                timer.stop();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
}
