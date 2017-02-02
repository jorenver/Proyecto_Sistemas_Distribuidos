package cacheService;

import umontreal.ssj.randvarmulti.DirichletGen;
import umontreal.ssj.rng.*;
import umontreal.ssj.rng.MRG32k3aL;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class AlgoritmoImp{


	/*
	Calcula los m dado los x, efectua: mi = xi*M + mi_
	*/
	private double[]  Denormalize(double[] x , double[] ms_ , double M){
		int tam = x.length;
		double [] ms = new double[tam];

		for (int i =0; i< tam; i++){
			ms[i] = x[i]*M + ms_[i];
		}
		return ms;
	}

	//retorna el resto de memoria (M menos suma de minimos)
	private double calculateRestMemory(double[] ms_, double M){
		double total=0.0;
		double sobrante;
		for (int i=0;i<ms_.length ;i++ ) {
			total+=ms_[i];
		}
		if(total > M){
			return -1.0;
		}
		sobrante=M-total;
		return sobrante;
	}

	private double limit(UnivariateFunction h, double value){
		PolynomialSplineFunction h1 = (PolynomialSplineFunction)h;
		double[] knots = h1.getKnots();
		double min = knots[0];
		double max = knots[knots.length -1];

		if(value< min){
			return min;
		}
		if(value>max){
			return max;
		}

		return value;
	}

	/*
	Calcula F(x) = Sum(wi*Ui(mi))
	donde:
	wi es un peso
	Ui es la funcion de utilidad = -fi*EAT(mi)
	EAT(mi) = hi(mi)*cdi + (1-h(mi))*bdi
	*/
	private double calculateFunction(List<UnivariateFunction> functions, double [] ms, double []weights, int[] f ,
	float cd , float bd){
		double result=0;


		int numFunctions = functions.size();
		for (int i=0; i< numFunctions; i++){
			UnivariateFunction h = functions.get(i);
			//se validan los valores de x
			double ms_i = limit(h, ms[i]);
			double h_i = h.value(ms_i);
			double EAT = h_i*cd + (1.0- h_i)*bd;
			double Ui = -1*f[i]*EAT;
			result += weights[i]*Ui; // Wi*Ui(mi)
		}

		return result;

	}

	private int getIndexMinSolution(Solution[] solutions){
		int index=0, n = solutions.length;
		for (int i=0; i < n ;i++ ) {
			if(solutions[i].getResult() < solutions[index].getResult()){
				index=i;
			}
		}
		return index;
	}

	private int getIndexMaxSolution(Solution[] solutions){
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
	private ArrayList<UnivariateFunction> createHitRateCurves(List<UtilityFunction> functions){
		int numFunctions = functions.size();

		ArrayList<UnivariateFunction> hitRateCurves = new ArrayList<UnivariateFunction>(numFunctions);
		for (UtilityFunction f : functions){
			SplineInterpolator interpolator = new SplineInterpolator();
			int numPoints = f.getPointList().size();
			double x[] = new double[numPoints]; 
			double y[] = new double[numPoints];
			for(int i =0; i< numPoints; i++){
				UtilityFunctionPoint point = f.getPoint(i);
				x[i] = point.getM();
				y[i] = point.getHit();
				//hit rates debe estar entre cero y uno
				if( !(y[i]>=0  && y[i]<=1) ){
					return null;
				}

			}
			

			//interpola la curva y lo agrega al arrayList
			try{
				hitRateCurves.add(interpolator.interpolate(x,y));
			}catch(Exception e){
				return null;
			}
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


	//validar suma de minimos
	//validar contenido y <=1
	//controlar exception de valores de x


	public double [] probabilisticAdactiveSearch(DataCacheRequest request,int k,int j ,int limit){
		
		//SE OBTIENEN LOS DATOS DEL OBJETO REQUEST
		List<UtilityFunction> functions=request.getUList(); //se obtiene los puntos de los hit rates 
		double M=request.getM(); //cantidad de memoria M
		List<Float> m_list=request.getMMinList(); //se obtienen las particiones de memoria minima
		List<Float> weights_list=request.getWList();//se obtienen los pesos
		List<Integer> frecuencies_list =  request.getFList();//se obtienen las frecuencias
		float cd = request.getCdi();//se obtiene el cdi
		float bd = request.getBdi(); //se obtiene el bdi

		
		
		//numero de caches
		int n = functions.size();
		//minimos de memoria
		double[] m_= new double[n];
		//pesos
		double[] weights= new double[n];
		int[] frecuencias = new int[n];
		//inicializo los pesos, los minimos y las frecuencias
		for (int i=0;i<m_list.size() ;i++) {
			m_[i]=m_list.get(i);
			weights[i]=weights_list.get(i);
			frecuencias[i] = frecuencies_list.get(i);
		}
		//se calcula el resto de memoria, es lo que se va a repartir
		double restM = calculateRestMemory(m_,M);
		//validacion
		if(restM<0){
			return null;
		}
		//se crean las curvas de hit rate
		List<UnivariateFunction> hitRateCurves = createHitRateCurves(functions);
		if(hitRateCurves==null){
			return null;
		}



		//Arreglo de soluciones
		Solution[] solutions= new Solution[k];
		double[] alpha= new double[n];
		//inicializo los alpha con 1/n
		for (int i=0;i<alpha.length ;i++) {
			alpha[i]=1.0/n;
		}
		DirichletGen dirichlet = new DirichletGen(new MRG32k3aL(),alpha);
		//Genero la primeras K soluciones
		

		for (int i=0;i<solutions.length ;i++) {
			double []xs = new double[n];
			dirichlet.nextPoint(xs);
			double []ms = Denormalize(xs, m_ , M);
			double result = calculateFunction(hitRateCurves,ms,weights,frecuencias, cd, bd);
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
				double result =calculateFunction(hitRateCurves,ms,weights,frecuencias, cd, bd); //calculamos el valor de la funcion con la solucion generada
				
				int index=getIndexMinSolution(solutions); // encontramos menos buena
				 //si la solucion generada es mejor que la peor guardada la remplazamos
				if(result>solutions[index].getResult()){
					solutions[index] = new Solution(xs, result);
				}
			}
			count++;
		}
		int max=getIndexMaxSolution(solutions);
		return Denormalize(solutions[max].getXs(),m_,M);

	}

}