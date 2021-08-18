import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流算法
 */
public class TokenLimiter {
    private ArrayBlockingQueue<String> queue;

    private int limit;

    private TimeUnit timeUnit;

    private int period;

    public TokenLimiter(int limit, TimeUnit timeUnit, int period) {
        this.limit = limit;
        this.timeUnit = timeUnit;
        this.period = period;
        queue = new ArrayBlockingQueue<>(limit);
        init();

    }

    public boolean tryAcquire() {
        return queue.poll() != null;
    }

    private void init() {
        for(int i=0;i<limit;i++) {
            queue.offer("T");
        }
    }

    private void addStoken() {
        queue.offer("T");
    }

    private void start() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::addStoken, 10, period, timeUnit);
    }
}
