package cacheService;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


public class ThroughputTest{
	
	static int respuestas = 0;


	public static void main(String[] args) throws Exception {
             
        int pedidos = Integer.parseInt(args[0]);
        
        ExecutorService executor = Executors.newFixedThreadPool(pedidos);
        
        long startTime = System.currentTimeMillis();
        while(pedidos>0){

            executor.execute(new Thread(new Runnable(){
                public void run(){
                    ClienteThroughput client = new ClienteThroughput("localhost", 42420);
                    DataCacheRequest.Builder builder = client.createBuilder(5);
                    try {

                        List<Float> mValues= client.probabilisticAdaptiveSearch(builder.build());
                        if(mValues.size()>0){
                            System.out.println("Respuesta recibida");
                      		aumentarContador();

                        }
                    } catch(Exception e) {
                        System.out.println(e);
                    }

                }
                synchronized public void aumentarContador(){
                	ThroughputTest.respuestas ++;
                }

            }));

            pedidos --;            

        }
        executor.shutdown();
        while (!executor.isTerminated());
        long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        double tiempoExec = (double)elapsedTimeMillis/1000; //segundos
        
        double throughput = (double)ThroughputTest.respuestas/tiempoExec;

        System.out.println("Tiempo: " + tiempoExec);
        System.out.println("Numero Respuestas: " + ThroughputTest.respuestas);
        System.out.println("El throughput es " + throughput);

    }


}
