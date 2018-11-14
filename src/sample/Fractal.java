package sample;

/*
   JavaFx Framework was used This software used for building the user interface
   Added two slider controls and Time elapsed label
   Equation Power slider control change the power of the fractal equation
   Magnify Slider control change the magnification of the fractal
   And each control change Time Elapsed label shows how much time was taken by the CPU to do
   following changes
   Command Line arguments also work well................

 */

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


public class Fractal extends Application {

    //This are constant. These values don't change
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;
    private static final int NUM_OF_THREADS = 10;

    //Each one of these is used to keep parameter data
    private static String[] args;
    private static double realLower = -1;
    private static double realUpper = 1;
    private static double complexLower = -1;
    private static double complexUpper = 1;
    private static double scaleWidth;
    private static double scaleHeight;
    private static long startTime;
    private static int numOfIterations = 100;
    private static int equPower = 2;
    private static int equType = 0;

    //Some GUI Elements
    private BufferedImage bufferedImage;
    private Pane resultPanel;
    private Graphics2D graphics;
    private Complex cJulia = new Complex(-0.4,0.6);
    private ArrayList<Color> colors = new ArrayList<>();
    private Label timeLbl;
    private Slider magSlider, equSlider;

    @Override
    public void start(Stage stage) {
        //Check command line arguments and state an error
        try {
            if (args.length == 0) {
                System.out.println("Error: No Input Parameters");
                System.exit(0);
            } else if (args[0].equals("Mandelbrot")) {
                equType = 0;
                switch (args.length - 1) {
                    case 5:
                        numOfIterations = Integer.parseInt(args[5]);
                    case 4:
                        realLower = Double.parseDouble(args[1]);
                        realUpper = Double.parseDouble(args[2]);
                        complexLower = Double.parseDouble(args[3]);
                        complexUpper = Double.parseDouble(args[4]);
                        break;
                    default:
                        System.out.println("Error: Number of parameters");
                        System.exit(0);
                }

            } else if (args[0].equals("Julia")) {
                equType = 1;
                switch (args.length - 1) {
                    case 3:
                        numOfIterations = Integer.parseInt(args[3]);
                    case 2:
                        cJulia.setReal(Double.parseDouble(args[1]));
                        cJulia.setImag(Double.parseDouble(args[2]));
                        break;
                    default:
                        System.out.println("Error: Number of parameters");
                        System.exit(0);
                }
            } else {
                System.out.println("Error: Set Name");
                System.exit(0);
            }
        }catch (NumberFormatException e){
            System.out.println("Error: Input Parameters");
            System.exit(0);
        }

        scaleWidth = (realUpper-realLower)/2;
        scaleHeight = (complexUpper-complexLower)/2;
        if(scaleWidth>0 && scaleHeight>0) {
            initializeColors();
            createUserInterface(stage);
        }else {
            System.out.println("Error: The region of interest");
        }
    }

    /*
        This function includes the user interface design and
        event listeners for slider controls
     */
    private void createUserInterface(Stage stage) {
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

        //This result panel contains the fractals
        resultPanel = new Pane();
        resultPanel.setPrefHeight(SCREEN_HEIGHT);
        resultPanel.setPrefWidth(SCREEN_WIDTH);
        resultPanel.setStyle("-fx-background-color: #000000");

        HBox root = new HBox();
        root.getChildren().addAll(controlPanel,resultPanel);

        stage.setTitle("Fractal Effect Generator");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();

        resultPanel.getChildren().add(new ImageView(generateFractal()));

        //Event Listener for equation power slider
        equSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isNowChanging) {
                if (! isNowChanging) {
                    equPower = (int) equSlider.getValue();
                    resultPanel.getChildren().add(new ImageView(generateFractal()));
                }
            }
        });

        //Event Listener for Magnification Slider
        magSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isNowChanging) {
                if (! isNowChanging) {
                    scaleHeight = magSlider.getValue();
                    scaleWidth = magSlider.getValue();
                    resultPanel.getChildren().add(new ImageView(generateFractal()));
                }
            }
        });

    }

    private Image generateFractal() {
        /*
            Concept behind this is ,An image was created by calculating each pixel and
            select a color according to number of iterations
            Finally the image is viwed as normal jpeg, png photo in the JavaFX Panel using
            ImageView class
         */
        //Store CPU Time into startTime Variable
        Fractal.startTime = System.currentTimeMillis();

        //Create a buffered Image. It can keep pixel data as an image
        bufferedImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = bufferedImage.createGraphics();

        //Calculate how much height should allocate to a thread;
        int slotHeight = SCREEN_HEIGHT/NUM_OF_THREADS;
        Thread thread;
        //Iterate over number of threads
        for(int iThread=0;iThread<NUM_OF_THREADS;iThread++){
            //Run PaintSlot Runnable class by giving from which height point to which height point should calculate
            thread = new Thread(new PaintSlots(iThread*slotHeight,(iThread+1)*slotHeight));
            thread.start();

            /*
                Main thread have to wait until all the other thread finish
                Therefore we join these threads with main thread
             */
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Something Happened");
            }
        }

        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        graphics.dispose();
        timeLbl.setText("Time Elapsed : " + (System.currentTimeMillis() - Fractal.startTime)+"mS");
        return image;
    }

    //Used some pre defined random color gradient to map the fractal
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

    /*
        Created a Inner class called PaintSlots which is implemented Runnable interface
        It paint paint buffered image shared variable
     */
    private class PaintSlots implements Runnable{
        private int lowerBound;
        private int upperBound;

        public PaintSlots(int lowerBound, int upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public void run() {
            for (int yCoordinate = lowerBound; yCoordinate <= upperBound; yCoordinate++) {
                for (int xCoordinate = 0; xCoordinate <= SCREEN_WIDTH; xCoordinate++) {
                    //Mapped the pixel coordinate to complex region
                    double xMapValue = scaleWidth * (((double) 2 * xCoordinate / SCREEN_WIDTH) - 1);
                    double yMapValue = scaleHeight * (((double) 2 * yCoordinate / SCREEN_HEIGHT) - 1);
                    int iteration = 0;

                    //Checked which set should be used
                    switch (equType){
                        case 0:
                            iteration = isMandelbrot(new Complex(xMapValue, yMapValue)); break;
                        case 1:
                            iteration = isJulia(new Complex(xMapValue, yMapValue)); break;
                    }

                    //Add color according to according to number of iterations
                    if (iteration == numOfIterations) {
                        graphics.setColor(Color.BLACK);
                    } else {
                        int nColors = 16;
                        for (int i = 0; i < nColors; i++) {
                            if (iteration > i * numOfIterations / nColors && iteration <= (i + 1) * numOfIterations / nColors) {
                                graphics.setColor(colors.get(i));
                            }
                        }
                    }
                    graphics.drawRect(xCoordinate, yCoordinate, 1, 1);
                }
            }
        }
    }

    //This function checks whether given complex number is included in Mandelbrot or not.
    public int isMandelbrot(Complex c) {
        int count = 0;
        Complex zN = new Complex(0, 0);
        while (count++ < numOfIterations) {
            zN = zN.getComplexPower(equPower).addComplex(c);
            if(zN.getSquareAbsolute() > 4){
                return count;
            }
        }
        return numOfIterations;
    }

    //This function checks whether given complex number is included in Julia or not.
    public int isJulia(Complex zN) {
        int count = 0;
        while (zN.getSquareAbsolute() < 4 && count++ < numOfIterations) {
            zN = zN.getComplexPower(equPower).addComplex(cJulia);
        }
        return count;
    }

    public static void main(String[] args) {
        Fractal.args = args;
        launch(args);
    }
}
