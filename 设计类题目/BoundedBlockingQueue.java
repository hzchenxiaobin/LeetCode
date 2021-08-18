import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//有限阻塞队列
public class BoundedBlockingQueue {
    private int capacity = 0;

    private final Deque<Integer> deque = new LinkedList<>();

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition notEmpty = lock.newCondition();

    private final Condition notFull = lock.newCondition();

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public void enqueue(int element) throws InterruptedException {
        lock.lock();
        try {
            while (deque.size() >= capacity) {
                notFull.await();
            }
            deque.offerFirst(element);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public int dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (deque.isEmpty()) {
                notEmpty.await();
            }
            int result = deque.pollLast();
            notFull.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return deque.size();
    }
}
