package cacheAlgorithm;

import umontreal.ssj.randvarmulti.DirichletGen;
import umontreal.ssj.rng.*;
import umontreal.ssj.rng.MRG32k3aL;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;



class UtilityFunctionPoint{
	private double hit;
	private double m;

	public UtilityFunctionPoint(double m, double hit){
		this.hit=hit;
		this.m=m;
	}

	public double getHit(){
		return hit;
	}
	public double getM(){
		return m;
	}
}

class UtilityFunction{
	private ArrayList<UtilityFunctionPoint> function;
	public UtilityFunction(){
		function= new ArrayList<UtilityFunctionPoint>();
	}
	ArrayList<UtilityFunctionPoint> getFunction(){
		return function;
	}

	void addPoint(UtilityFunctionPoint p){
		function.add(p);
	}

	void addPoint(double m, double hit){
		function.add(new UtilityFunctionPoint(m,hit));
	}

	public int size(){
		return function.size();
	}

	public UtilityFunctionPoint get(int index){
		return function.get(index);
	}
}

//objeto solucion
//contiene el conjunto de X
//y el resultado de evaluar F(m)
class Solution{
	private double[] xs;
	private double result;

	public Solution(double[] xs, double result){
		this.xs = xs;
		this.result = result;
	} 
	public double[] getXs(){
		return xs;
	}
	public double getResult(){
		return result;
	}
}


public class AlgoritmoImp{




	/*
	Calcula los m dado los x, efectua: mi = xi*M + mi_
	*/
	private double[]  Denormalize(double[] x , double[] ms_ , double M){
		int tam = x.length;
		double total=0.0;
		double sobrante;
		for (int i=0;i<ms_.length ;i++ ) {
			total+=ms_[i];
		}
		sobrante=M-total;
		double [] ms = new double[tam];
		for (int i =0; i< tam; i++){
			ms[i] = x[i]*sobrante + ms_[i];
		}
		return ms;
	}

	/*
	Calcula F(x) = Sum(wi*Ui(mi))
	donde:
	wi es un peso
	Ui es la funcion de utilidad = -fi*EAT(mi)
	EAT(mi) = hi(mi)*cdi + (1-h(mi))*bdi
	*/
	private double calculateFunction(ArrayList<UnivariateFunction> functions, double [] ms, double []weights){
		double result=0;
		
		int numFunctions = functions.size();
		for (int i=0; i< numFunctions; i++){
			double cd, bd, f;
			cd = 1.2;
			bd = 3.4;
			f = 30;
			UnivariateFunction h = functions.get(i);
			double h_i = h.value(ms[i]);
			double EAT = h_i*cd + (1.0- h_i)*bd;
			double Ui = -1*f*EAT;
			result += weights[i]*Ui; // Wi*Ui(mi)
		}

		return result;

	}

	private int getMinSolution(Solution[] solutions){
		int index=0, n = solutions.length;
		for (int i=0; i < n ;i++ ) {
			if(solutions[i].getResult() < solutions[index].getResult()){
				index=i;
			}
		}
		return index;
	}

	private int getMaxSolution(Solution[] solutions){
		int index=0, n = solutions.length;
		for (int i=0;i < n ;i++ ) {
			if(solutions[i].getResult()>solutions[index].getResult()){
				index=i;
			}
		}
		return index;
	}


	/*
	Retorna los hit rates
	*/
	private ArrayList<UnivariateFunction> createHitRateCurves(ArrayList<UtilityFunction> functions){
		int numFunctions = functions.size();

		ArrayList<UnivariateFunction> hitRateCurves = new ArrayList<UnivariateFunction>(numFunctions);
		for (UtilityFunction f : functions){
			SplineInterpolator interpolator = new SplineInterpolator();
			int numPoints = f.size();
			double x[] = new double[numPoints]; 
			double y[] = new double[numPoints];
			for(int i =0; i< numPoints; i++){
				UtilityFunctionPoint point = f.get(i);
				x[i] = point.getM();
				y[i] = point.getHit();
			}
			hitRateCurves.add(interpolator.interpolate(x,y));
		}
		return hitRateCurves; 
	}


	/*
	Genera los nuevos alpha muestreando una distribucion gaussianda
	multivariada con media X
	*/
	private  double[] generateAlphas(Solution[] solutions){
		
		//de todas las soluciones se escoge una de forma aleatoria
		int numSolutions = solutions.length;
		int randomIndex = (int)(Math.random()*numSolutions);
		double[] xs = solutions[randomIndex].getXs();
		int tam = xs.length;

		//se genera una matriz identidad de nxn
		double[][] covariances = new double[tam][tam];
		for(int i = 0; i<tam; i++){
			for(int j = 0; j<tam; j++){
				if(i==j){
					covariances[i][j] = 1.0;
				}else{
					covariances[i][j] = 0.0;
				}
			}
		}
		//System.out.println(Arrays.toString(covariances));
		MultivariateNormalDistribution gaussianaMult = new MultivariateNormalDistribution(xs, covariances);
		double[] alphas = gaussianaMult.sample();
		for (int i=0;i<tam ;i++ ) {
			alphas[i]=Math.abs(alphas[i]);
		}
		return alphas;
	}



	public double [] probabilisticAdactiveSearch(int k, int j, ArrayList<UtilityFunction> functions, double[] weights, 
		int limit, double M, double[] m_){
		Solution[] solutions; 
		double[] alpha; 
		int n;
		DirichletGen dirichlet;
		ArrayList<UnivariateFunction> hitRateCurves = createHitRateCurves(functions);

		n=functions.size();
		solutions= new Solution[k];
		alpha= new double[n];
		//inicializo el alpha
		for (int i=0;i<alpha.length ;i++) {
			alpha[i]=1.0/n;
		}
		dirichlet = new DirichletGen(new MRG32k3aL(),alpha);
		//Genero la primeras K soluciones
		

		for (int i=0;i<solutions.length ;i++) {
			double []xs = new double[n];
			dirichlet.nextPoint(xs);
			double []ms = Denormalize(xs, m_ , M);
			double result = calculateFunction(hitRateCurves,ms,weights);
			solutions[i] = new Solution(xs, result);
		}
		int count=0;
		while(count < limit){
			// Generamos un alpha apartir de las soluciones anteriores
			alpha = generateAlphas(solutions);

			dirichlet = new DirichletGen(new MRG32k3aL(),alpha);
			for (int i=0;i<j ;i++) {
				// genero una solucion x con el alpha
				double [] xs = new double[n];
				dirichlet.nextPoint(xs);
				double []ms = Denormalize(xs, m_ , M);
				double result =calculateFunction(hitRateCurves,ms,weights); //calculamos el valor de la funcion con la solucion generada
				
				int index=getMinSolution(solutions); // encontramos menos buena
				 //si la solucion generada es mejor que la peor guardada la remplazamos
				if(result>solutions[index].getResult()){
					solutions[index] = new Solution(xs, result);
				}
			}
			count++;
		}
		int max=getMaxSolution(solutions);
		return Denormalize(solutions[max].getXs(),m_,M);

	}

	public static void main(String[] args) {
		double []weights = {1.0,1.0};
		double M = 30.0;
		double [] m_ = {10.0, 5.0};
		ArrayList<UtilityFunction> functions = new ArrayList<UtilityFunction>();
		UtilityFunction f1 = new UtilityFunction();
		f1.addPoint(0,0.0);
		f1.addPoint(10,0.25);
		f1.addPoint(20,0.35);
		f1.addPoint(30,0.49);
		f1.addPoint(40,0.60);
		functions.add(f1);
		UtilityFunction f2 = new UtilityFunction();
		f2.addPoint(0,0.0);
		f2.addPoint(10,0.5);
		f2.addPoint(20,0.7);
		f2.addPoint(30,0.97);
		f2.addPoint(40,0.99);
		functions.add(f2);
		AlgoritmoImp al = new AlgoritmoImp();
		double[] result=al.probabilisticAdactiveSearch(10,5,functions,weights,500, M, m_);
		for (int i=0;i< result.length; i++ ) {
			System.out.println(result[i]);
		}
	}

}