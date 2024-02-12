package edu.uniandes;

public class Main {
  public static void main(String[] args) {
    int n = 3;
    int m = 3;
    var matrix = MatrixGenerator.generate(100, 100);

    // System.out.println(matrix);

    var service = new MatrixService();
    var maximum = service.max(matrix);

    System.out.println("The maximum value in the matrix is: " + maximum);

    service.close();
  }
}
