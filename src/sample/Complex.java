package sample;

public class Complex {

    private int xPixelCordinate;
    private int yPixelCordinate;
    private double scale;
    private double real;
    private double imag;

    public Complex(int xPixelCordinate, int yPixelCordinate) {
        this.xPixelCordinate = xPixelCordinate;
        this.yPixelCordinate = yPixelCordinate;
        this.scale = scale;
    }

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex(int xPixelCordinate, int yPixelCordinate, double scale) {
        this.xPixelCordinate = xPixelCordinate;
        this.yPixelCordinate = yPixelCordinate;
        this.scale = scale;
    }

    public int getxPixelCordinate() {
        return xPixelCordinate;
    }

    public void setxPixelCordinate(int xPixelCordinate) {
        this.xPixelCordinate = xPixelCordinate;
    }

    public int getyPixelCordinate() {
        return yPixelCordinate;
    }

    public void setyPixelCordinate(int yPixelCordinate) {
        this.yPixelCordinate = yPixelCordinate;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImag() {
        return imag;
    }

    public void setImag(double imag) {
        this.imag = imag;
    }


    //-----------------------------------------------------//

    public double getAbolute(){

        return Math.pow(real,2)+Math.pow(imag,2);
    }

    //---------- Static functions ------------------------------------------//
    public static Complex addComplex(Complex z1, Complex z2){
        return new Complex(z1.getReal()+z2.getReal(),z1.getImag()+z2.getImag());
    }

    public static Complex squareComplex(Complex z1){
        double newReal = Math.pow(z1.getReal(),2)-Math.pow(z1.getImag(),2);
        double newImag = 2*z1.getReal()*z1.getImag();
        return new Complex(newReal,newImag);
    }
}
