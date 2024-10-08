import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecuteShutdownThread  extends Thread {
    private ConsumerProducerBox taskHanlers;
    private LinkedList<ExecuteGoalThread> threads;

    public ExecuteShutdownThread(ConsumerProducerBox taskHanlers, LinkedList<ExecuteGoalThread> threads) {
        this.taskHanlers = taskHanlers;
        this.threads = threads;
    }

    @Override
    public void run() {
        System.out.println("shutdown is HELLO");
        AtomicBoolean allThreadsAreDone = new AtomicBoolean(false);
        Object monitor = ConsumerProducerBox.getMonitor();
        while (!taskHanlers.isQueueEmpty()) {
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
