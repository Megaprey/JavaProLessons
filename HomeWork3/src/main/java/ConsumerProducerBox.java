import java.util.LinkedList;

public class ConsumerProducerBox {
    public static Object getMonitor() {
        return monitor;
    }

    public static void setIsShotdown(boolean isShotdown) {
        ConsumerProducerBox.isShotdown = isShotdown;
    }

    private static boolean isShotdown;
    private final static Object monitor = new Object();
    private LinkedList<Runnable> queue = new LinkedList<>();

    public Runnable take() {
        Runnable task;
        synchronized (monitor) {
            if (queue.isEmpty()) {
                try {
                    System.out.println(Thread.currentThread().getName() + " : ушел в вейт");
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (queue.isEmpty() && isShotdown) {
                return new Runnable() {
                    @Override
                    public void run() {

                    }
                };
            }
            return queue.removeFirst();
        }
    }

    public boolean isEmpty() {
        synchronized (monitor) {
            return queue.isEmpty();
        }
    }

    public void put(Runnable r) {
        synchronized (monitor) {
            queue.addLast(r);
            monitor.notify();
        }
    }
}
