import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class ThreadPool implements Executor {
    private int capacity;
    private boolean isShutdown = false;
    LinkedList<ExecuteGoalThread> threads;
    ConsumerProducerBox taskCollector = new ConsumerProducerBox();

    public ThreadPool(int capacity) {
        threads = new LinkedList<>();
        for (int i = 0; i < capacity; i++) {
            ExecuteGoalThread thread = new ExecuteGoalThread(taskCollector);
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public void execute(Runnable command) {
        Object monitor = ConsumerProducerBox.getMonitor();
        synchronized (monitor){
            if (isShutdown) {
                monitor.notifyAll();
                throw new IllegalStateException("ThreadPool is closed");
            }
        }
        taskCollector.put(command);
    }

    public void shutdown() {
        isShutdown = true;
        threads.stream().forEach(ExecuteGoalThread::shutdown);
//        ConsumerProducerBox.getMonitor().notifyAll();
    }

    public void awaitTermination() {
        this.shutdown();
        threads.stream().forEach(executeGoalThread -> {
            try {
                executeGoalThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
