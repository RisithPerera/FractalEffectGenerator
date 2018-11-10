package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Mandelbrot {

    private static final int SCREEN_HEIGHT = 800;
    private static final int SCREEN_WIDTH = 800;
    private static final double FRACTAL_SCALE = 1;
    private static int count;
    private BufferedImage bufferedImage;

    private ArrayList<Complex> complexes = new ArrayList<>();
    private int iterator = 0;

    public Mandelbrot(Stage stage) {

        StackPane root = new StackPane();

        Scene scene = new Scene(root);
        stage.setTitle("Fractal Effect Generator");
        stage.setScene(scene);
        stage.show();

        root.setStyle("-fx-background-color: black");
        root.getChildren().add(new ImageView(createImage()));

        /*

        //Map the pixel point to complex plain
        for (int i = 0; i <= SCREEN_HEIGHT; i++) {
            for (int j = 0; j <= SCREEN_WIDTH; j++) {
                double x = ((double) j / SCREEN_WIDTH) - FRACTAL_SCALE / 2;
                double y = ((double) i / SCREEN_HEIGHT) - FRACTAL_SCALE / 2;
                complexes.add(new Complex(x, y));
            }
        }


        if (iterator++ < SCREEN_HEIGHT * SCREEN_WIDTH) {
            Complex c = complexes.get(iterator);
            double x = (c.getReal() + FRACTAL_SCALE / 2) * SCREEN_WIDTH;
            double y = (c.getImag() + FRACTAL_SCALE / 2) * SCREEN_HEIGHT;
            if (isMandelbrot(c)) {
                g2d.setColor(Color.BLUE);
                //gc.setFill(Color.BLUE);
            } else {
                int code = (int) Math.abs(count / 1000.0);
                g2d.setColor(new Color(code, code, code, 1));
                // gc.setFill();
            }
            g2d.fillOval(x, x, 1.0, 1.0);
            gc.fillOval(x, y, 2, 2);
        } else {
            System.out.println("stoped");
        }
        */

        File file = new File("myimage.png");

        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Time : " + (System.currentTimeMillis() - Main.startTime));
    }

    private Image createImage() {
        bufferedImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bufferedImage.createGraphics();


        long startTime = System.currentTimeMillis();
        for (int i = 0; i <= SCREEN_HEIGHT; i++) {
            for (int j = 0; j <= SCREEN_WIDTH; j++) {
                if(i<255 && j<255) g.setColor(new Color(i,j,i));
                g.fillOval(i+10,j+10,2,2);
            }
        }

        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        g.dispose();
        return image;
    }

    public boolean isMandelbrot(Complex c) {
        count = 1000;
        Complex zN = new Complex(0, 0);
        while (count-- > 0) {
            zN = Complex.addComplex(Complex.squareComplex(zN), c);
            if (zN.getAbolute() > 4) {
                return false;
            }
        }
        return true;
    }
}
