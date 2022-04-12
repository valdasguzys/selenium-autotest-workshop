package lt.insoft.webdriver.runner.model;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadId {
	private static final AtomicInteger nextId = new AtomicInteger(0);

	private static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial(nextId::incrementAndGet);

	public static int get() {
		return threadId.get();
	}
}