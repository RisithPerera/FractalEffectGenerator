package sample;

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

    //----------------------- Complex Function ------------------------------//

    public double getSquareAbsolute(){
        return Math.pow(real,2)+Math.pow(imag,2);
    }

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

    public Complex addComplex(Complex number){
        return new Complex(this.real+number.real,this.imag+number.imag);
    }
}
