import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecuteShutdownThread  extends Thread {
    private ThreadPool threadPool;
    private LinkedList<ExecuteGoalThread> threads;

    public ExecuteShutdownThread(ThreadPool threadPool, LinkedList<ExecuteGoalThread> threads) {
        this.threadPool = threadPool;
        this.threads = threads;
    }

    @Override
    public void run() {
        System.out.println("shutdown is HELLO");
        AtomicBoolean allThreadsAreDone = new AtomicBoolean(false);
        Object monitor = threadPool.getMonitor();
        while (!threadPool.isQueueEmpty()) {
        }
        while (!allThreadsAreDone.get()) {
            allThreadsAreDone.set(true);
            threads.forEach(executeGoalThread -> {
                if (executeGoalThread.isAlive()) {
                    allThreadsAreDone.set(false);
                }
            });
            synchronized (monitor) {
                monitor.notifyAll();
            }
        }
        System.out.println("shutdown is GOODBYE");
    }
}
