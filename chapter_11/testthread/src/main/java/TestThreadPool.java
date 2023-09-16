import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {
    private static final int corePoolSize = 3;
    private static final int maximumPoolSize = 5;
    private static final int keepAliveTime = 10;
    private static final TimeUnit timeUnit = TimeUnit.MINUTES;
    private static final int blockingQueueCapacity = 10;
    private static final BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(blockingQueueCapacity);

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, blockingQueue);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(corePoolSize);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize);

    }
}
