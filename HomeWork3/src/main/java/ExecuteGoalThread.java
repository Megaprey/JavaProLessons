import java.util.List;

public class ExecuteGoalThread extends Thread {
    private boolean running = true;
    private boolean isShutdown = false;
    private ThreadPool threadPool;

    public ExecuteGoalThread(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        while (running && (!isShutdown || !threadPool.isQueueEmpty())) {
            System.out.println(Thread.currentThread().getName() + " is running.");
            threadPool.take().run();
        }
    }

    public void shutdown() {
        isShutdown = true;
    }
}
