package edu.uniandes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MatrixService {

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public MatrixService() {

    }

    public int max(List<List<Integer>> matrix) {
        var numRows = matrix.size();
        var maximum = new Maximum();
        var partials = new ArrayList<Integer>();

        var maxLatch = new CountDownLatch(numRows);
        for (int i = 0; i < numRows; i++) {
            executor.execute(new FindMaximum(matrix.get(i), partials, maxLatch));
        }

        try {
            maxLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var gatherLatch = new CountDownLatch(1);
        executor.execute(new GatherMaximum(partials, maximum, gatherLatch));

        try {
            gatherLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return maximum.value;
    }

    public void close() {
        executor.shutdown();
    }
}
