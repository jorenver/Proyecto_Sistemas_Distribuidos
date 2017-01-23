package cacheService;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Iterator;

public class CacheServiceClient {
    private static final Logger logger = Logger.getLogger(CacheServiceClient.class.getName());

    private final ManagedChannel channel;
    private CacheServiceGrpc.CacheServiceBlockingStub blockingStub;

    public CacheServiceClient(String hostname, int port) {
        channel = ManagedChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build();
        blockingStub = CacheServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void probabilisticAdactiveSearch(DataCacheRequest request) {
        logger.info("Trying probabilisticAdactiveSearch");
        try {
            PartitionCacheResponse response = blockingStub.probabilisticAdactiveSearch(request);
            System.out.println("Reponse to Server: ");

            List<Float> mValues=response.getMList();
            Iterator<Float> iterator = mValues.iterator();
            int i=1;
            while(iterator.hasNext()){
                System.out.println("m"+i+": "+iterator.next());
                i++;
            }

        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "Request to grpc server failed", e);
        }
    }


    public static void main(String[] args) throws Exception {
        CacheServiceClient client = new CacheServiceClient("localhost", 42420);

        try {
            client.probabilisticAdactiveSearch(DataCacheRequest.newBuilder().setM((float)50.5).build());
        } finally {
            client.shutdown();
        }
    }
}
