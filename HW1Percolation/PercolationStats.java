/**
 * Created by Artem on 4/22/2017.
 */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // private Percolation[] perc;
    //private double[] frac;
    private double mean;
    private double stddev;
    private double lo;
    private double hi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        double[] frac = new double[trials];
        Percolation[] perc = new Percolation[trials];
        for (int i = 0; i < trials; i++) {
            perc[i] = new Percolation(n);
        }

        for (int i = 0; i < trials; i++) {
            while (!perc[i].percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                perc[i].open(row, col);
            }
            frac[i] = perc[i].numberOfOpenSites()/((double) n*n);
        }

        this.mean = StdStats.mean(frac);
        this.stddev = StdStats.stddev(frac);
        this.lo = mean - (1.96*stddev)/Math.sqrt(frac.length);
        this.hi = mean + (1.96*stddev)/Math.sqrt(frac.length);
    }    // perform trials independent experiments on an n-by-n grid

    public double mean() {
        //return StdStats.mean(frac);
        return mean;
    }                          // sample mean of percolation threshold

    public double stddev() {
        return stddev;
    }                     // sample standard deviation of percolation threshold

    public double confidenceLo()    {
        return lo;

    }             // low  endpoint of 95% confidence interval

    public double confidenceHi()  {
        return hi;

    }                // high endpoint of 95% confidence interval

    public static void main(String[] args) {
/*        if (Integer.parseInt(args[0]) <= 0 || Integer.parseInt(args[1]) <= 0) {
            throw new IllegalArgumentException();
        }*/

        int n = Integer.parseInt(args[0]);      // input file
        int t = Integer.parseInt(args[1]);

       /* int n = 20;
        int t = 10; */

        PercolationStats stats = new PercolationStats(n, t);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() +"]");

    }        // test client (described below)

}
