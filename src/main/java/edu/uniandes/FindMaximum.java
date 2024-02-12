package edu.uniandes;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FindMaximum implements Runnable {
  private final List<Integer> row;

  private final List<Integer> partialMax;

  private final CountDownLatch latch;

  public FindMaximum(List<Integer> row, List<Integer> partialMax, CountDownLatch latch) {
    this.row = row;
    this.partialMax = partialMax;
    this.latch = latch;
  }

  @Override
  public void run() {
    var max = Integer.MIN_VALUE;
    for (int i = 0; i < row.size(); i++) {
      if (row.get(i) > max) {
        max = row.get(i);
      }
    }

    // System.out.println("Partial max: " + max);
    synchronized (partialMax) {
      partialMax.add(max);
    }

    latch.countDown();
  }
}
