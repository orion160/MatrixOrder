package edu.uniandes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixGenerator {

    private static final Random r = new Random();

    public static <T> List<List<Integer>> generate(int n, int m) {
        var matrix = new ArrayList<List<Integer>>(n);
        for (int i = 0; i < n; i++) {
            var row = new ArrayList<Integer>(m);
            for (int j = 0; j < m; j++) {
                var tmp = r.nextInt();
                row.add(tmp);
            }
            matrix.add(row);
        }
        return matrix;
    }
}
