package sample;

/*
    This class keeps the real and imaginary part of a complex number
    And some bunch of functions related to complex number arithmetic
 */
public class Complex {
    private double real;
    private double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public void setImag(double imag) {
        this.imag = imag;
    }

    //--------------- Complex Function --------------------//

    // Return the square absolute value of the complex number
    public double getSquareAbsolute(){
        return Math.pow(real,2)+Math.pow(imag,2);
    }

    // Return any power of a given complex number
    public Complex getComplexPower(int power){
        double newReal = this.real;
        double newImag = this.imag;

        while (power-- > 1){
            double newRealTemp = this.real*newReal - this.imag*newImag;
            newImag = this.real*newImag + this.imag*newReal;
            newReal = newRealTemp;
        }
        return new Complex(newReal,newImag);
    }

    // Add two complex number and return new Object
    public Complex addComplex(Complex number){
        return new Complex(this.real+number.real,this.imag+number.imag);
    }
}
