import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class Interpolador{
	public static void main(String[] args){
		SplineInterpolator lint = new SplineInterpolator();
		double x[] = new double[3];
		double y[] = new double[3];
		x[0] = 0.0;
		x[1] = 3.0;
		x[2] = 4.7;
		y[0] = 1.2;
		y[1] = 4.2;
		y[2] = 7.28;

		PolynomialSplineFunction funcion = lint.interpolate(x,y);
		System.out.println("pilas ya esta " + funcion.value(2.0)  + " " +  funcion.value(5.5));

	}

}