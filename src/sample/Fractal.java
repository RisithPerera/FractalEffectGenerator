package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

enum FractalType{
    MANDELBROT, JULIA
}

public class Fractal extends Application {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int NUM_OF_ITERATIONS = 100;
    private static final int NUM_OF_THREADS = 4;

    private static double SCALE_WIDTH = 1;
    private static double SCALE_HEIGHT = 1;
    private static int EQU_POWER = 2;
    private static FractalType fractalType = FractalType.MANDELBROT;
    private BufferedImage bufferedImage;
    private Complex cJulia = new Complex(-0.4,0.6);
    private ArrayList<Color> colors = new ArrayList<>();
    private Label timeLbl;
    private Slider magSlider, equSlider;

    public static long startTime;

    @Override
    public void start(Stage stage) {
        Label equationLbl = new Label("Equation Power");
        equationLbl.setStyle("-fx-font-size: 14; -fx-text-fill: white;");
        equSlider = new Slider(2,5,0);
        equSlider.setShowTickLabels(true);
        equSlider.setShowTickMarks(true);
        equSlider.setMajorTickUnit(1);
        equSlider.setMinorTickCount(0);
        equSlider.setBlockIncrement(1);
        equSlider.setSnapToTicks(true);
        VBox equVBox = new VBox(equationLbl,equSlider);
        equVBox.setSpacing(10);

        Label magnifyLbl = new Label("Magnification");
        magnifyLbl.setStyle("-fx-font-size: 14; -fx-text-fill: white;");
        magSlider = new Slider(0.5,2,1);
        magSlider.setShowTickLabels(true);
        magSlider.setShowTickMarks(true);
        magSlider.setMajorTickUnit(0.5);
        magSlider.setMinorTickCount(0);
        magSlider.setBlockIncrement(2);
        magSlider.setSnapToTicks(true);
        VBox magVBox = new VBox(magnifyLbl,magSlider);
        magVBox.setSpacing(10);

        timeLbl = new Label();
        timeLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: white;");

        VBox controlPanel = new VBox();
        controlPanel.setPrefWidth(200);
        controlPanel.getChildren().addAll(equVBox,magVBox,timeLbl);
        controlPanel.setSpacing(30);
        controlPanel.setPadding(new Insets(10,10,10,10));
        controlPanel.setStyle("-fx-background-color: #2B2B2B");

        Pane resultPanel = new Pane();
        resultPanel.setPrefHeight(SCREEN_HEIGHT);
        resultPanel.setPrefWidth(SCREEN_WIDTH);
        resultPanel.setStyle("-fx-background-color: #000000");

        HBox root = new HBox();
        root.getChildren().addAll(controlPanel,resultPanel);

        Scene scene = new Scene(root);
        stage.setTitle("Fractal Effect Generator");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        initializeColors();

        resultPanel.getChildren().add(new ImageView(generateFractal()));

        equSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isNowChanging) {
                if (! isNowChanging) {
                    EQU_POWER = (int) equSlider.getValue();
                    resultPanel.getChildren().add(new ImageView(generateFractal()));
                }
            }
        });

        magSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isNowChanging) {
                if (! isNowChanging) {
                    SCALE_WIDTH = magSlider.getValue();
                    SCALE_HEIGHT = magSlider.getValue();
                    resultPanel.getChildren().add(new ImageView(generateFractal()));
                }
            }
        });

    }

    private Image generateFractal() {
        Fractal.startTime = System.currentTimeMillis();
        bufferedImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        //Fractal Loop
        for (int iThread = 0; iThread <= NUM_OF_THREADS; iThread++) {
            new Thread(() -> {
                for (int yCoordinate = (iThread*200)+1; yCoordinate <= (iThread+1)*200; yCoordinate++) {
                    for (int xCoordinate = 0; xCoordinate <= SCREEN_WIDTH; xCoordinate++) {
                        double xMapValue = SCALE_WIDTH * (((double) 2 * xCoordinate / SCREEN_WIDTH) - 1);
                        double yMapValue = SCALE_HEIGHT * (((double) 2 * yCoordinate / SCREEN_HEIGHT) - 1);
                        int iteration = 0;
                        switch (fractalType) {
                            case MANDELBROT:
                                iteration = isMandelbrot(new Complex(xMapValue, yMapValue));
                                break;
                            case JULIA:
                                iteration = isJulia(new Complex(xMapValue, yMapValue));
                                break;
                        }

                        if (iteration == NUM_OF_ITERATIONS) {
                            g.setColor(Color.BLACK);
                        } else {
                            int nColors = 16;
                            for (int i = 0; i < nColors; i++) {
                                if (iteration > i * NUM_OF_ITERATIONS / nColors && iteration <= (i + 1) * NUM_OF_ITERATIONS / nColors) {
                                    g.setColor(colors.get(i));
                                }
                            }
                        }
                        g.drawRect(xCoordinate, yCoordinate, 1, 1);
                    }
                }
            }).start();
        }

        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        g.dispose();
        timeLbl.setText("Time Elapsed : " + (System.currentTimeMillis() - Fractal.startTime)+"mS");
        return image;
    }

    public int isMandelbrot(Complex c) {
        int count = 0;
        Complex zN = new Complex(0, 0);
        while (count++ < NUM_OF_ITERATIONS) {
            zN = zN.getComplexPower(EQU_POWER).addComplex(c);
            if(zN.getSquareAbsolute() > 4){
                return count;
            }
        }
        return NUM_OF_ITERATIONS;
    }

    public int isJulia(Complex zN) {
        int count = 0;
        while (zN.getSquareAbsolute() < 4 && count++ < NUM_OF_ITERATIONS) {
            zN = zN.getComplexPower(EQU_POWER).addComplex(cJulia);
        }
        return count;
    }

    private void initializeColors() {
        colors.add(new Color( 66 , 15 , 30));
        colors.add(new Color( 25 ,  7 , 26));
        colors.add(new Color(  9 ,  1 , 47));
        colors.add(new Color(  4 ,  4 , 73));
        colors.add(new Color(  0 ,  7 ,100));
        colors.add(new Color( 12 , 44 ,138));
        colors.add(new Color( 24 , 82 ,177));
        colors.add(new Color( 57 ,125 ,209));
        colors.add(new Color(134 ,181 ,229));
        colors.add(new Color(211 ,236 ,248));
        colors.add(new Color(241 ,233 ,191));
        colors.add(new Color(248 ,201 , 95));
        colors.add(new Color(255 ,170 ,  0));
        colors.add(new Color(204 ,128 ,  0));
        colors.add(new Color(153 , 87 ,  0));
        colors.add(new Color(106 , 52 ,  3));
    }

    public static void main(String[] args) {
        if(args.length>0) {
            switch (args[0]) {
                case "Mandelbrot":
                    fractalType = FractalType.MANDELBROT;
                    break;
                case "Julia":
                    fractalType = FractalType.JULIA;
                    break;
                default:
                    System.out.println("Wrong Input Arguments");
                    return;
            }
        }
        launch(args);
    }
}