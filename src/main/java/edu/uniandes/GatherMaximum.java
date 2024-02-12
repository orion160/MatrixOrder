package edu.uniandes;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GatherMaximum implements Runnable {

  private final List<Integer> partialMax;

  private Maximum maximum;

  private final CountDownLatch latch;

  public GatherMaximum(List<Integer> partialMax, Maximum maximum, CountDownLatch latch) {
    this.partialMax = partialMax;
    this.maximum = maximum;
    this.latch = latch;
  }

  @Override
  public void run() {
    var max = Integer.MIN_VALUE;
    for (int i = 0; i < partialMax.size(); i++) {
      if (partialMax.get(i) > max) {
        max = partialMax.get(i);
      }
    }

    maximum.value = max;
    // System.out.println("Global max: " + maximum);
    latch.countDown();
  }
}
