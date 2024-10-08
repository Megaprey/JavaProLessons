import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolApp {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(4);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
//            if (finalI == 4) {
//                waitMin(0.1);
//                threadPool.shutdown();
//            }
            System.out.println(Thread.currentThread().getName() + " : " + finalI); // main : 4
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " : " + finalI);
                waitMin(0.1);
            });
        }
//        System.out.println("shotdown");
//        threadPool.shutdown();
        threadPool.awaitTermination();
        waitMin(0.1);
        System.out.println("Stream is closed I liked!!!");

    }


    public static void waitMin(double min) {
        try {
            Thread.sleep((long) (min * 60000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
