package sample;

public class Complex {
    private double real;
    private double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    //----------------------- Complex Function ------------------------------//

    public double getSquareAbsolute(){
        return Math.pow(real,2)+Math.pow(imag,2);
    }

    public Complex getSquareComplex(){
        double newReal = Math.pow(this.real,2)-Math.pow(this.imag,2);
        double newImag = 2*this.real*this.imag;
        return new Complex(newReal,newImag);
    }

    public Complex getThirdComplex(){
        double newReal = Math.pow(this.real,3)-3*this.real*Math.pow(this.imag,2);
        double newImag = 3*Math.pow(this.real,2)*this.imag-Math.pow(this.imag,3);
        return new Complex(newReal,newImag);
    }

    public Complex addComplex(Complex number){
        return new Complex(this.real+number.real,this.imag+number.imag);
    }
}
