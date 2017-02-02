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


public class LatencyTest{


	public static void main(String[] args) throws Exception {

        for (int i =0; i < 10;i++){
            ClienteThroughput client = new ClienteThroughput("localhost", 42420);
            DataCacheRequest.Builder builder = client.createBuilder(10*(1+i));

            try {

                long startTime = System.currentTimeMillis();
                List<Float> mValues= client.probabilisticAdaptiveSearch(builder.build());
                if(mValues.size()>0){
                    System.out.println("Respuesta recibida");
                    long elapsedTimeMillis = System.currentTimeMillis() - startTime;
                    System.out.println("******Latencia: " + (10*(1+i)) + " " + elapsedTimeMillis);


                }
            } catch(Exception e) {
                System.out.println(e);
            }


        }

        
        

    }


}
