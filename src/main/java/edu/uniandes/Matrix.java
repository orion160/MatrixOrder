package edu.uniandes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public class Matrix<T> {
  private final int rows;
  private final int columns;
  private final int size;
  private final List<T> buffer;

  public Matrix(final int rows, final int columns, Supplier<T> initialValues) {
    this.rows = rows;
    this.columns = columns;
    this.size = rows * columns;
    this.buffer = new ArrayList<T>(rows * columns);
    for (int i = 0; i < size; i++) {
      buffer.add(initialValues.get());
    }
  }

  public int rows() {
    return rows;
  }

  public int columns() {
    return columns;
  }

  public int size() {
    return size;
  }

  public T get(final int i, final int j) {
    return getLineal(i * j + j);
  }

  public void set(final int i, final int j, final T value) {
    setLineal(i * j + j, value);
  }

  public T getLineal(final int lineal) {
    return buffer.get(lineal);
  }

  public void setLineal(final int lineal, final T value) {
    buffer.set(lineal, value);
  }

  public List<T> getRow(final int i) {
    return buffer.subList(i * columns, (i + 1) * columns);
  }

  public static <T> T parallelReduce(
      final Matrix<T> matrix,
      final BinaryOperator<T> op,
      final T initialValue,
      final ExecutorService executor) {
    final var result = new Ref<T>();

    final var partials = new ArrayList<T>();
    var maxLatch = new CountDownLatch(matrix.rows());
    for (int i = 0; i < matrix.rows(); i++) {
      executor.execute(
          new Reducer<>(
              matrix.getRow(i),
              op,
              initialValue,
              (r) -> {
                synchronized (partials) {
                  partials.add(r);
                }
              },
              maxLatch));
    }

    try {
      maxLatch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    synchronized (partials) {
      System.out.println(partials);
    }

    final var gatherLatch = new CountDownLatch(1);
    executor.execute(
        new Reducer<>(
            partials,
            op,
            initialValue,
            (r) -> {
              result.value = r;
            },
            gatherLatch));

    try {
      gatherLatch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return result.value;
  }
}
