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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





public class ClienteThroughput{
    private static final Logger logger = Logger.getLogger(ClienteThroughput.class.getName());

    private final ManagedChannel channel;
    private CacheServiceGrpc.CacheServiceBlockingStub blockingStub;

    public ClienteThroughput(String hostname, int port) {
        channel = ManagedChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build();
        blockingStub = CacheServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public List<Float> probabilisticAdaptiveSearch(DataCacheRequest request) {
        logger.info("Trying probabilisticAdaptiveSearch");
        try {
            PartitionCacheResponse response = blockingStub.probabilisticAdaptiveSearch(request);
            Boolean status = response.getStatus();
            if(status)
                return response.getMList();
            else
                return null;

        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "Request to grpc server failed", e);
            return null;
        }
    }

    public static UtilityFunctionPoint newPoint(float m, float hit){
        return UtilityFunctionPoint.newBuilder().setM(m).setHit(hit).build();
    }


    public DataCacheRequest.Builder createBuilder(int numPartitions){
        DataCacheRequest.Builder builder=DataCacheRequest.newBuilder();
        builder.setM((float)1000.0);
        builder.setCdi((float)0.05);
        builder.setBdi((float)0.06);
        for(int i = 0; i<numPartitions; i++){
            //agregamos las frecuencias
            builder.addF(1);
            //agregamos los minimos
            float random = (float)(Math.random()*10.0);
            builder.addMMin(random);
            //agregamos los pesos 
            builder.addW((float)1.0);
            //agregamos los U
            UtilityFunction.Builder ufBilder=UtilityFunction.newBuilder();
            for(int j=0; j<= 1200; j+=100){
                UtilityFunctionPoint  point;
                random = (float)(Math.random()*1.0);
                point= newPoint((float)j,random);
                ufBilder.addPoint(point);
             }
             builder.addU(ufBilder.build());
        }
       
        return builder;
    }

}
