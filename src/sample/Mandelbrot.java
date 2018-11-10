package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Box;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Mandelbrot {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;

    private static final double SCALE_WIDTH = 2;
    private static final double SCALE_HEIGHT = 2;

    private static final int NUM_OF_ITERATIONS = 1000;
    private BufferedImage bufferedImage;

    private ArrayList<Pixel> pixels = new ArrayList<>();

    public Mandelbrot(Stage stage) {
        Button playBtn = new Button("Play");
        playBtn.setPrefHeight(30);
        playBtn.setPrefWidth(50);
        Button pauseBtn = new Button("Pause");
        pauseBtn.setPrefHeight(30);
        pauseBtn.setPrefWidth(50);
        Button stopBtn = new Button("Stop");
        stopBtn.setPrefHeight(30);
        stopBtn.setPrefWidth(50);

        HBox btnHBox = new HBox(pauseBtn,playBtn,stopBtn);
        btnHBox.setSpacing(10);
        btnHBox.setLayoutX(14);
        btnHBox.setLayoutY(14);

        Pane controlPanel = new Pane();
        controlPanel.setPrefWidth(200);
        controlPanel.getChildren().add(btnHBox);

        Pane resultPanel = new Pane();
        resultPanel.setPrefHeight(SCREEN_HEIGHT);
        resultPanel.setPrefWidth(SCREEN_WIDTH);

        HBox root = new HBox();
        root.getChildren().addAll(controlPanel,resultPanel);

        Scene scene = new Scene(root);
        stage.setTitle("Fractal Effect Generator");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        //Create pixel array
        for (int i = 0; i <= SCREEN_HEIGHT; i++) {
            for (int j = 0; j <= SCREEN_WIDTH; j++) {
                pixels.add(new Pixel(j, i));
            }
        }

        //Map the pixel point to complex plain
        for (Pixel pixel:pixels) {
            pixel.setReal(SCALE_WIDTH*(((double) 2*pixel.getCoordinateX() / SCREEN_WIDTH) - 1));
            pixel.setImag(SCALE_HEIGHT*(((double) 2*pixel.getCoordinateY() / SCREEN_HEIGHT) - 1));
        }

        resultPanel.setStyle("-fx-background-color: black");
        //resultPanel.getChildren().add(new ImageView(createImage()));

        //--------------------------------- Create Image File ------------------------------------------//
        /*
        File file = new File("myimage.png");

        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        System.out.println("Time : " + (System.currentTimeMillis() - Main.startTime));
    }

    private Image createImage() {
        bufferedImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bufferedImage.createGraphics();

        //Check Pixels
        for (Pixel pixel : pixels){
            if(isMandelbrot(pixel)){
                g.setColor(Color.black);
            }else{
                //int color = pixel.getIteration()*255/NUM_OF_ITERATIONS;
                int i = pixel.getIteration();
                if(i>100 && i<200){
                    g.setColor(Color.BLUE);
                }else{
                    g.setColor(Color.DARK_GRAY);
                }
            }
            g.drawRect(pixel.getCoordinateX(),pixel.getCoordinateY(),1,1);
        }

        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        g.dispose();
        return image;
    }

    public boolean isMandelbrot(Pixel pixel) {
        int count = NUM_OF_ITERATIONS;
        Complex zN = new Complex(0, 0);
        Complex c = new Complex(pixel.getReal(),pixel.getImag());
        while (count-- > 0) {
            zN = zN.getThirdComplex().addComplex(c);
            if (zN.getSquareAbsolute() > 4) {
                pixel.setIteration(count);
                return false;
            }
        }
        pixel.setIteration(count);
        return true;
    }
}
