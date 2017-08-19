package com.broodcamp.multithreading;

import java.time.LocalTime;

/**
 * @author Edward P. Legaspi
 * @created 19 Aug 2017
 */
class MyTask {
	private final int duration;

	public MyTask(int duration) {
		this.duration = duration;
	}

	public int calculate() {
		System.out.println("ThreadName=" + Thread.currentThread().getName());
		try {
			Thread.sleep(duration * 1000);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		return duration;
	}

	public int calculateWithError() throws Exception {
		System.out.println("ThreadName=" + Thread.currentThread().getName());
		try {
			Thread.sleep(duration * 1000);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (LocalTime.now().getSecond() % 2 == 0) {
			throw new Exception("ERROR");
		}

		return duration;
	}
}
