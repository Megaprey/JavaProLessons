public class ExecuteGoalThread extends Thread {
    private ConsumerProducerBox taskHanlers;
    private boolean running = true;
    private boolean isShutdown = false;

    public ExecuteGoalThread(ConsumerProducerBox taskHanlers) {
        this.taskHanlers = taskHanlers;
    }

    @Override
    public void run() {
        while (running && (!isShutdown || !taskHanlers.isEmpty())) {
            System.out.println(Thread.currentThread().getName() + " is running.");
            taskHanlers.take().run();
        }
    }

    public void shutdown() {
        isShutdown = true;
        ConsumerProducerBox.setIsShotdown(true);
    }
}
