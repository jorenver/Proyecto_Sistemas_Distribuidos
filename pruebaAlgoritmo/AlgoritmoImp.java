import umontreal.ssj.randvarmulti.DirichletGen;
import umontreal.ssj.rng.*;
import umontreal.ssj.rng.MRG32k3aL;
import java.util.Random;
import java.util.ArrayList;

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

	int size(){
		return function.size();
	}

	UtilityFunctionPoint get(int index){
		return function.get(index);
	}
}

public class AlgoritmoImp{

	private double calculateFunction(ArrayList<UtilityFunction> functions, double [] ms){
		return 1.0;
	}

	private int getMinSolution(double[][] solutions,int n){
		int index=0;
		for (int i=0;i < solutions.length ;i++ ) {
			if(solutions[i][n]<solutions[index][n]){
				index=i;
			}
		}
		return index;
	}

	private int getMaxSolution(double[][] solutions, int n){
		int index=0;
		for (int i=0;i < solutions.length ;i++ ) {
			if(solutions[i][n]>solutions[index][n]){
				index=i;
			}
		}
		return index;
	}

	public double [] probabilisticAdactiveSearch(int k, int j,ArrayList<UtilityFunction> functions, int limit){
		double[][] solutions; 
		double[] alpha; 
		int n;
		DirichletGen dirichlet;
		n=functions.size();
		solutions= new double[k][n+1]; // la posicion n guarda el varlor de evaluar la funcion de utilidad
		alpha= new double[n];
		//inicializo el alpha
		for (int i=0;i<alpha.length ;i++) {
			alpha[i]=n;
		}
		dirichlet = new DirichletGen(new MRG32k3aL(),alpha);
		//Genero la primeras K soluciones
		for (int i=0;i<solutions.length ;i++) {
			dirichlet.nextPoint(solutions[i]);
			solutions[i][n]=calculateFunction(functions,solutions[i]);
		}
		int count=0;
		while(count < limit){
			// Generamos un alpha apartir de las soluciones anteriores
			//alpha=
			dirichlet = new DirichletGen(new MRG32k3aL(),alpha);
			for (int i=0;i<j ;i++) {
				// genero una solucion x con el alpha
				double [] x = new double[n+1];
				dirichlet.nextPoint(x);
				x[n]=calculateFunction(functions,x); //calculamos el valor de la funcion con la solucion generada
				int index=getMinSolution(solutions,n); // encontramos menos buena
				 //si la solucion generada es mejor que la peor guardada la remplazamos
				if(x[n]>solutions[index][n]){
					solutions[index]=x;
				}
			}
			count++;
		}
		int max=getMaxSolution(solutions,n);
		return solutions[max];

	}

	public static void main(String[] args) {
		ArrayList<UtilityFunction> functions = new ArrayList<UtilityFunction>();
		UtilityFunction f1 = new UtilityFunction();
		f1.addPoint(1,0.5);
		f1.addPoint(2,0.7);
		f1.addPoint(3,0.9);
		functions.add(f1);
		UtilityFunction f2 = new UtilityFunction();
		f1.addPoint(1,0.5);
		f1.addPoint(2,0.7);
		f1.addPoint(3,0.9);
		functions.add(f2);
		AlgoritmoImp al = new AlgoritmoImp();
		double[] result=al.probabilisticAdactiveSearch(3,5,functions,4);
		for (int i=0;i< result.length; i++ ) {
			System.out.println(result[i]);
		}
	}

}