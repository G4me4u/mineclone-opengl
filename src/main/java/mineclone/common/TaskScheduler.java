package mineclone.common;

import java.util.ArrayDeque;
import java.util.Queue;

public class TaskScheduler {

	private final Queue<Runnable> tasks;
	
	public TaskScheduler() {
		tasks = new ArrayDeque<>();
	}
	
	public void schedule(Runnable task) {
		synchronized (tasks) {
			tasks.add(task);
			tasks.notifyAll();
		}
	}
	
	public void executeAll() {
		while (true) {
			Runnable task;
			synchronized (tasks) {
				task = tasks.poll();
			}
			if (task == null)
				break;
			task.run();
		}
	}
	
	public void execute(long timeoutMillis) {
		long last = System.currentTimeMillis();
		while (timeoutMillis > 0L) {
			Runnable task = null;

			synchronized (tasks) {
				task = tasks.poll();
				
				if (task == null) {
					try {
						tasks.wait(timeoutMillis);
					} catch (InterruptedException ignore) {
						// We have new tasks
					}
				}
			}
			
			if (task != null)
				task.run();
			
			long now = System.currentTimeMillis();
			// Handle system time changes for dt
			long dt = Math.max(now - last, 0L);
			timeoutMillis -= dt;
			last = now;
		}
	}
}
