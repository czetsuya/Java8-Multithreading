package com.broodcamp.multithreading;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Edward P. Legaspi
 * @created 19 Aug 2017
 */
public class Main {

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		List<MyTask> tasks = IntStream.range(0, 4).mapToObj(i -> new MyTask(1)).collect(Collectors.toList());
		// 4077
		// runSequentially(tasks);
		// 1062
		// useParallelStream(tasks);
		// 2037
		useCompletableFuture(tasks);
		useCompletableFutureWithExecutor(tasks);
	}

	public static void runSequentially(List<MyTask> tasks) {
		long start = System.nanoTime();
		List<Integer> result = tasks.stream().map(MyTask::calculate).collect(Collectors.toList());
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		System.out.println(result);
	}

	public static void useParallelStream(List<MyTask> tasks) {
		long start = System.nanoTime();
		List<Integer> result = tasks.parallelStream().map(MyTask::calculate).collect(Collectors.toList());
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		System.out.println(result);
	}

	public static void useCompletableFuture(List<MyTask> tasks) {
		long start = System.nanoTime();
		List<CompletableFuture<Integer>> futures = tasks.stream().map(t -> CompletableFuture.supplyAsync(() -> {
			try {
				return t.calculateWithError();
			} catch (Exception e) {
				return null;
			}
		})).collect(Collectors.toList());

		List<Integer> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		System.out.println(result);
	}

	public static void useCompletableFutureWithExecutor(List<MyTask> tasks) {
		System.out.println("Thead cout=" + Runtime.getRuntime().availableProcessors());
		long start = System.nanoTime();
		ExecutorService executor = Executors.newFixedThreadPool(Math.min(tasks.size(), Runtime.getRuntime().availableProcessors()));
		List<CompletableFuture<Integer>> futures = tasks.stream().map(t -> CompletableFuture.supplyAsync(() -> {
			try {
				return t.calculateWithError();
			} catch (Exception e) {
				return null;
			}
		}, executor)).collect(Collectors.toList());

		List<Integer> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
		System.out.println(result);
		executor.shutdown();
	}

}
