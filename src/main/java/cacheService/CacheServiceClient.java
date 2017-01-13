package cacheService;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void probabilisticAdactiveSearch(float m) {
        logger.info("Trying probabilisticAdactiveSearch: " + m);
        try {
            DataCacheRequest request = DataCacheRequest.newBuilder().setM(m).build();
            PartitionCacheResponse response = blockingStub.probabilisticAdactiveSearch(request);
            logger.info("Respondio el Servidor: ");
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "Request to grpc server failed", e);
        }
    }


    public static void main(String[] args) throws Exception {
        CacheServiceClient client = new CacheServiceClient("localhost", 42420);

        try {
            client.probabilisticAdactiveSearch((float)50.5);
        } finally {
            client.shutdown();
        }
    }
}
