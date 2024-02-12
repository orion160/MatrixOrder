package edu.uniandes;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

public class Reducer<T> implements Runnable {
  private final List<T> buffer;

  private final BinaryOperator<T> op;

  private final T initialValue;

  private final Consumer<T> result;

  private final CountDownLatch latch;

  public Reducer(
      final List<T> buffer,
      final BinaryOperator<T> op,
      final T initialValue,
      final Consumer<T> setResult,
      final CountDownLatch latch) {
    this.buffer = buffer;
    this.op = op;
    this.initialValue = initialValue;
    this.result = setResult;
    this.latch = latch;
  }

  @Override
  public void run() {
    var target = initialValue;
    for (int i = 0; i < buffer.size(); i++) {
      target = op.apply(buffer.get(i), target);
    }

    // System.out.println("Target: " + target);
    result.accept(target);

    latch.countDown();
  }
}
