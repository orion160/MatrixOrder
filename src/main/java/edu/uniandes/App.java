package edu.uniandes;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App implements Runnable {
  private final ExecutorService executor;

  public App(final ExecutorService e) {
    this.executor = e;
  }

  public static void main(String[] args) {
    final var app = new App(Executors.newVirtualThreadPerTaskExecutor());
    app.run();
  }

  @Override
  public void run() {
    final var rows = 2;
    final var columns = 10;
    final var r = new Random();
    final var matrix = new Matrix<>(rows, columns, () -> r.nextInt(1, 100));

    // Print the matrix
    System.out.println("Matrix:");
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        System.out.print(matrix.get(i, j) + " ");
      }
      System.out.println();
    }

    Integer result = null;
    try (executor) {
      result = Matrix.parallelReduce(matrix, (a, b) -> Math.max(a, b), Integer.MIN_VALUE, executor);
    } catch (Exception e) {
      System.out.println(e);
    }

    System.out.println("The maximum value in the matrix is: " + result);
  }
}
