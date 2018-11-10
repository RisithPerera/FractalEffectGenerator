package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Mandelbrot {

    Timeline timer;

    private static  int SCREEN_SIZE = 800;
    private static final double FRACTAL_SCALE = 0.7;
    private static int count;

    private ArrayList<Complex> complexes = new ArrayList<>();
    private int iterator = 0;

    public Mandelbrot(Stage stage) {
        StackPane pane = new StackPane();
        Canvas canvas = new Canvas(SCREEN_SIZE,SCREEN_SIZE);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        pane.getChildren().add(canvas);
        pane.setStyle("-fx-background-color: black");
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

        for(int i=0;i<=SCREEN_SIZE;i++){
            for(int j=0;j<=SCREEN_SIZE;j++){
                double x = ((double) j/SCREEN_SIZE)-FRACTAL_SCALE/2;
                double y = ((double) i/SCREEN_SIZE)-FRACTAL_SCALE/2;
                complexes.add(new Complex(x,y));
            }
        }

        timer = new Timeline(new KeyFrame(Duration.millis(0.1), (ActionEvent event) -> {
            if(iterator++ < SCREEN_SIZE*SCREEN_SIZE){
                Complex c = complexes.get(iterator);
                double x = (c.getReal()+FRACTAL_SCALE/2)*SCREEN_SIZE;
                double y = (c.getImag()+FRACTAL_SCALE/2)*SCREEN_SIZE;
                if(isMandelbrot(c)){
                    gc.setFill(Color.BLUE);
                }else{
                    double code = Math.abs(count/1000.0);
                    gc.setFill(new Color(code,code,code,1));
                }
                gc.fillOval(x, y,2,2);
            }else{
                System.out.println("stoped");
                timer.stop();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public boolean isMandelbrot(Complex c){
        count = 1000;
        Complex zN = new Complex(0,0);
        while(count-- >0){
            zN = Complex.addComplex(Complex.squareComplex(zN),c);
            if(zN.getAbolute() > 4){
                return false;
            }
        }
        return true;
    }
}
