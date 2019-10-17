import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadApp {
    private static int NUM_OF_REQUEST;
    private static long TIME_TAKEN;

    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        boolean isComplete = false;

        if(isValidArgs(args)){
            int threadno = Integer.parseInt(args[1]);
            final int numOfTimes = Integer.parseInt(args[2]);
            final URL url = new URL(args[0]);

            ExecutorService threadPool = Executors.newFixedThreadPool(threadno);
            for (int i = 1; i <= threadno; i++) {
                 final int threadCount = i;
                 threadPool.submit(new Runnable() {
                 public void run() {
                    for (int i = 1; i <=numOfTimes; i++) {
                        final int requestCount=i;
                        try {
                            fetchUrl(url,threadCount,requestCount);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
            threadPool.awaitTermination(3000, TimeUnit.MILLISECONDS);
            isComplete=true;
        }
        else{
            invalidArguments();
        }
        if(isComplete) {
            printSummary();
            System.exit(0);
        }
    }


    public static boolean isValidArgs(String[] args){
        return args.length==3;
    }

    public static void invalidArguments(){
        System.out.println("No Arguments providec");
        System.exit(0);
    }
    public static void fetchUrl(URL url,int threadNum,int reqNum) throws IOException{
        NUM_OF_REQUEST++;
        long start = System.currentTimeMillis();
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        long timeTaken = System.currentTimeMillis() - start;
        TIME_TAKEN=TIME_TAKEN+timeTaken/60;

        int code = connection.getResponseCode();
        System.out.println("request"+reqNum+"/Thread"+threadNum+",HTTP"+" "+code+","+timeTaken+" milliseconds");
    }
    public static void printSummary(){
        System.out.println("Summary: number of requests:"+NUM_OF_REQUEST+" total time spent:"+TIME_TAKEN+" seconds requests per second:"+NUM_OF_REQUEST/(TIME_TAKEN));
    }
}


