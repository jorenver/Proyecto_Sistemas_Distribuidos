package cacheService;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.2)",
    comments = "Source: CacheService.proto")
public class CacheServiceGrpc {

  private CacheServiceGrpc() {}

  public static final String SERVICE_NAME = "cacheService.CacheService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cacheService.DataCacheRequest,
      cacheService.PartitionCacheResponse> METHOD_PROBABILISTIC_ADACTIVE_SEARCH =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cacheService.CacheService", "probabilisticAdactiveSearch"),
          io.grpc.protobuf.ProtoUtils.marshaller(cacheService.DataCacheRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cacheService.PartitionCacheResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CacheServiceStub newStub(io.grpc.Channel channel) {
    return new CacheServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CacheServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CacheServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static CacheServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CacheServiceFutureStub(channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class CacheServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Probabilistic adaptive search
     * </pre>
     */
    public void probabilisticAdactiveSearch(cacheService.DataCacheRequest request,
        io.grpc.stub.StreamObserver<cacheService.PartitionCacheResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PROBABILISTIC_ADACTIVE_SEARCH, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_PROBABILISTIC_ADACTIVE_SEARCH,
            asyncUnaryCall(
              new MethodHandlers<
                cacheService.DataCacheRequest,
                cacheService.PartitionCacheResponse>(
                  this, METHODID_PROBABILISTIC_ADACTIVE_SEARCH)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class CacheServiceStub extends io.grpc.stub.AbstractStub<CacheServiceStub> {
    private CacheServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CacheServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CacheServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CacheServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Probabilistic adaptive search
     * </pre>
     */
    public void probabilisticAdactiveSearch(cacheService.DataCacheRequest request,
        io.grpc.stub.StreamObserver<cacheService.PartitionCacheResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PROBABILISTIC_ADACTIVE_SEARCH, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class CacheServiceBlockingStub extends io.grpc.stub.AbstractStub<CacheServiceBlockingStub> {
    private CacheServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CacheServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CacheServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CacheServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Probabilistic adaptive search
     * </pre>
     */
    public cacheService.PartitionCacheResponse probabilisticAdactiveSearch(cacheService.DataCacheRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PROBABILISTIC_ADACTIVE_SEARCH, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class CacheServiceFutureStub extends io.grpc.stub.AbstractStub<CacheServiceFutureStub> {
    private CacheServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CacheServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CacheServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CacheServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Probabilistic adaptive search
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cacheService.PartitionCacheResponse> probabilisticAdactiveSearch(
        cacheService.DataCacheRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PROBABILISTIC_ADACTIVE_SEARCH, getCallOptions()), request);
    }
  }

  private static final int METHODID_PROBABILISTIC_ADACTIVE_SEARCH = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CacheServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(CacheServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PROBABILISTIC_ADACTIVE_SEARCH:
          serviceImpl.probabilisticAdactiveSearch((cacheService.DataCacheRequest) request,
              (io.grpc.stub.StreamObserver<cacheService.PartitionCacheResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_PROBABILISTIC_ADACTIVE_SEARCH);
  }

}
