import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPool implements Executor {
    private int capacity;
    private AtomicBoolean isShutdown = new AtomicBoolean(false);
    private LinkedList<ExecuteGoalThread> threads;
    private LinkedList<Runnable> queue = new LinkedList<>();
    private final Object monitor = new Object();

    public Object getMonitor() {
        return monitor;
    }

    public ThreadPool(int capacity) {
        threads = new LinkedList<>();
        for (int i = 0; i < capacity; i++) {
            ExecuteGoalThread thread = new ExecuteGoalThread(this);
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public void execute(Runnable command) {
        synchronized (monitor){
            if (isShutdown.get()) {
                monitor.notifyAll();
                throw new IllegalStateException("ThreadPool is closed");
            }
            queue.addLast(command);
            monitor.notify();
        }
    }

    public Runnable take() {
        synchronized (monitor) {
            if (queue.isEmpty()) {
                try {
                    System.out.println(Thread.currentThread().getName() + " : ушел в вейт");
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (queue.isEmpty() && isShutdown.get()) {
                return new Runnable() {
                    @Override
                    public void run() {}
                };
            }
            return queue.removeFirst();
        }
    }

    public boolean isQueueEmpty() {
        synchronized (monitor) {
            return queue.isEmpty();
        }
    }

    public void shutdown() {
        isShutdown.set(true);
        threads.stream().forEach(ExecuteGoalThread::shutdown);
        new ExecuteShutdownThread(this, threads).start();
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
