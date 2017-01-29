package cacheService;
//objeto solucion
//contiene el conjunto de X
//y el resultado de evaluar F(m)
public class Solution{
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