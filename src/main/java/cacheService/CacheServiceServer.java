package cacheService;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.logging.Logger;

public class CacheServiceServer {

    private static final Logger logger = Logger.getLogger(CacheServiceServer.class.getName());

    private int port = 42420;
    private Server server;

    private void start() throws Exception {
        logger.info("Starting the grpc Cache Service server.....");

        server = ServerBuilder.forPort(port)
                .addService(new CacheServiceImpl())
                .build()
                .start();

        logger.info("Server started. Listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** JVM is shutting down. Turning off grpc server as well ***");
            CacheServiceServer.this.stop();
            System.err.println("*** shutdown complete ***");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }


    public static void main(String[] args) throws Exception {
        logger.info("Server startup. Args = " + Arrays.toString(args));
        final CacheServiceServer cacheServiceServer = new CacheServiceServer();

        cacheServiceServer.start();
        cacheServiceServer.blockUntilShutdown();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private class CacheServiceImpl extends CacheServiceGrpc.CacheServiceImplBase {

        @Override
        public void probabilisticAdaptiveSearch(DataCacheRequest request, StreamObserver<PartitionCacheResponse> responseObserver) {
            PartitionCacheResponse response = PartitionCacheResponse.newBuilder().addM(new Float(1.5)).addM(new Float(2.3)).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
