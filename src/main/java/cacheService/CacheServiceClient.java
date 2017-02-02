package cacheService;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;


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
    UtilityFunction readFunction( String file) throws FileNotFoundException, IOException{
        UtilityFunction.Builder ufBilder=UtilityFunction.newBuilder();
        UtilityFunctionPoint  point;
        FileReader f = new FileReader(file);
        BufferedReader b = new BufferedReader(f);
        String cadena;
        //leer todos los puntos
        ArrayList<PointHit> points= new ArrayList<PointHit>();
        while((cadena=b.readLine())!=null){
            String [] fields = cadena.split(",");
            float m=Float.parseFloat(fields[0]);
            float hit= (float)1.0-Float.parseFloat(fields[1]);
            points.add(new PointHit(m,hit));
            
        }
        //ordeno los puntos
        Collections.sort(points, new PointHitComparator());

        //crear el objeto serializable
        for (PointHit p : points) {
            point= newPoint(p.getM(),p.getHit());
            //System.out.println(p.getM());
            //System.out.println(p.getHit());
            ufBilder.addPoint(point);
        }
        UtilityFunction function=ufBilder.build();
        return function;
    }

    String readValue(BufferedReader b)throws FileNotFoundException, IOException{
        String cadena = b.readLine();
        String [] fields = cadena.split(":");
        return fields[1];
    }

    DataCacheRequest readData(String file) throws FileNotFoundException, IOException {
        DataCacheRequest.Builder builder=DataCacheRequest.newBuilder();
        FileReader f = new FileReader(file);
        BufferedReader b = new BufferedReader(f);
        
        //leer M
        float M =Float.parseFloat(readValue(b));
        builder.setM((float)M);
        System.out.println("M: "+M);

        // leer cdi
        float cdi =Float.parseFloat(readValue(b));
        builder.setCdi((float)cdi);
        System.out.println("cd: "+cdi);

        // leer bdi
        float bdi =Float.parseFloat(readValue(b));
        builder.setBdi((float)bdi);
        System.out.println("bd: "+bdi);

        // leer f
        String [] fs= readValue(b).split(",");
        for (int i=0;i<fs.length ;i++ ) {
            builder.addF(Integer.parseInt(fs[i]));
            System.out.println("frecuencia[ "+i+"]: "+fs[i]);
        }

        // leer mim
        String [] mmims= readValue(b).split(",");
        for (int i=0;i<fs.length ;i++ ) {
            builder.addMMin(Float.parseFloat(mmims[i]));
            System.out.println("m_[ "+i+"]: "+mmims[i]);
        }

        // leer w
        String [] ws= readValue(b).split(",");
        for (int i=0;i<fs.length ;i++ ) {
            builder.addW(Float.parseFloat(ws[i]));
            System.out.println("w[ "+i+"]: "+ws[i]);
        }

        // leer miss rate function
        String [] mfunctionsFiles= readValue(b).split(",");
        UtilityFunction function;
        for (int i=0;i<fs.length ;i++ ) {
            System.out.println(mfunctionsFiles[i]);
            function= readFunction(mfunctionsFiles[i]);
            builder.addU(function);
        }

        b.close();
        return builder.build();
    }


    public static void main(String[] args) throws Exception {
        CacheServiceClient client = new CacheServiceClient("localhost", 42420);
        /*
        DataCacheRequest.Builder builder=DataCacheRequest.newBuilder();
        
        builder.setM((float)30.0);
        builder.setCdi((float)1.2);
        builder.setBdi((float)3.4);
        //agregamos las frecuencias
        builder.addF(1);
        builder.addF(1);
        builder.addF(1);
        //agregamos los m minimos
        builder.addMMin((float)5.0);
        builder.addMMin((float)3.0);
        builder.addMMin((float)2.0);
        //agregamos los w
        builder.addW((float)1.0);
        builder.addW((float)1.0);
        builder.addW((float)1.0);
        //agragamos las U
        UtilityFunctionPoint  point;
        // f1
        UtilityFunction.Builder ufBilder=UtilityFunction.newBuilder();
        point= newPoint((float)0.0,(float)0.25);
        ufBilder.addPoint(point);
        point= newPoint((float)20.0,(float)0.35);
        ufBilder.addPoint(point);
        point= newPoint((float)30.0,(float)0.5);
        ufBilder.addPoint(point);
        point= newPoint((float)40.0,(float)0.70);
        ufBilder.addPoint(point);
        point= newPoint((float)50.0,(float)0.8);
        ufBilder.addPoint(point);
        builder.addU(ufBilder.build());

        //f2
        ufBilder=UtilityFunction.newBuilder();
        point= newPoint((float)0.0,(float)0.1);
        ufBilder.addPoint(point);
        point= newPoint((float)20.0,(float)0.2);
        ufBilder.addPoint(point);
        point= newPoint((float)30.0,(float)0.25);
        ufBilder.addPoint(point);
        point= newPoint((float)40.0,(float)0.40);
        ufBilder.addPoint(point);
        point= newPoint((float)50.0,(float)0.6);
        ufBilder.addPoint(point);
        builder.addU(ufBilder.build());

        //f3
        ufBilder=UtilityFunction.newBuilder();
        point= newPoint((float)0.0,(float)0.1);
        ufBilder.addPoint(point);
        point= newPoint((float)20.0,(float)0.2);
        ufBilder.addPoint(point);
        point= newPoint((float)30.0,(float)0.25);
        ufBilder.addPoint(point);
        point= newPoint((float)40.0,(float)0.4);
        ufBilder.addPoint(point);
        point= newPoint((float)50.0,(float)0.6);
        ufBilder.addPoint(point);
        builder.addU(ufBilder.build());
        DataCacheRequest request=builder.build();
        */
        try {
            DataCacheRequest request=client.readData("./prueba.txt");
            List<Float> mValues= client.probabilisticAdaptiveSearch(request);
            if(mValues==null){
                System.out.println("Error");
                return;
            }
            System.out.println("Reponse to Server: ");
            Iterator<Float> iterator = mValues.iterator();
            int i=1;
            while(iterator.hasNext()){
                System.out.println("m"+i+": "+iterator.next());
                i++;
            }
        } finally {
            client.shutdown();
        }
    }
}
