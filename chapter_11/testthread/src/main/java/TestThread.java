import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TestThread {
    private volatile int i = 0;
    private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {
        TestThread testThread = new TestThread();

        // 实现Runnable接口
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                for (int j = 0; j < 10; j++) {
                    try {
                        // testThread.queue.put(j);
                        boolean offer = testThread.queue.offer(j);
                        System.out.println(offer);
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    System.out.println(testThread.queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 实现Callable接口
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Future<Object> future = singleThreadExecutor.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                return null;
            }
        });
        try {
            // 等待线程结束并返回结果
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printStr() {
        synchronized (TestThread.class) {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("currentThread = " + Thread.currentThread() + ", i = " + i++);
            }
            System.out.println("end");
        }
    }
}
