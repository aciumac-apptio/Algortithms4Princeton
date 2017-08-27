/**
 * Created by Artem on 3/29/2017.
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.*;

public class Percolation {
    private WeightedQuickUnionUF grid;
    private byte[] connectedTopBottom;
    private boolean[] openSites;
    private int size;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n)      {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new WeightedQuickUnionUF(n*n + 2);
        connectedTopBottom = new byte[n*n + 2];
        openSites = new boolean[n*n + 2];
        Arrays.fill(connectedTopBottom, (byte) 0);
        size = n;
    }
    private void connectNeighbors(int row, int col) {
        // if invalid indices return
        if (row < 1 || row > size ||col < 1 || col > size ) {
            return;
        }

        if (isFull(row, col)) {
            return;
        }

        if (!isOpen(row, col)) {
            return;
        } else {
            grid.union(0, size*(row - 1) + col);
            connectedTopBottom[size*(row - 1) + col] = (byte) 1;

            connectNeighbors(row-1, col);
            connectNeighbors(row + 1, col);

            // both conditions for middle column
            if (col < size && col > 1 || col == 1) {
                connectNeighbors(row, col + 1);
            }

            if (col < size && col > 1 || col == size) {
                connectNeighbors(row, col - 1);
            }
        }
    }

    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            if (row == size){ //&& !percolates()) {
                grid.union(size*(row - 1) + col, size*size + 1);
            }

            // Top row
            if (row == 1) {
                // Fully open
                grid.union(0, size*(row - 1) + col);
                openSites[size*(row - 1) + col] = true;
                connectedTopBottom[size*(row - 1) + col] = (byte) 1;
                // recursively connect neighbors
                connectNeighbors(row + 1, col);
                connectNeighbors(row, col - 1);
                connectNeighbors(row, col + 1);

            } else {
                openSites[size*(row - 1) + col] = true;
                /// Connect open neighbors.
                // If a neighbor is fully open, recursively
                // any row more than 1
                if (isOpen(row - 1, col)) {
                    grid.union(size*(row - 2) + col, size*(row - 1) + col);
                    if (isFull(row - 1, col)) {
                        // change
                        grid.union(0,size*(row - 1) + col );
                        connectedTopBottom[size*(row - 1) + col] = (byte) 1;
                        connectNeighbors(row-1, col);
                        connectNeighbors(row + 1, col);
                        connectNeighbors(row, col - 1);
                        connectNeighbors(row, col + 1);
                    }
                }

                // Middle rows all have bottom branches
                if (row < size) {
                    if (isOpen(row+1, col)) {
                        // changed row+1 to row -> next line
                        grid.union(size*(row) + col, size*(row - 1) + col);
                        if (isFull(row + 1, col)) {
                            grid.union(0, size*(row - 1) + col );
                            connectedTopBottom[size*(row - 1) + col] = (byte) 1;
                            connectNeighbors(row-1, col);
                            connectNeighbors(row + 1, col);
                            connectNeighbors(row, col - 1);
                            connectNeighbors(row, col + 1);
                        }
                    }
                }

                // Middle columns have left and right neighbor
                if (col < size && col > 1 || col == 1) {
                    // to the right
                    if (isOpen(row, col+1)) {
                        //Exchange p & q
                        grid.union(size*(row - 1) + col+1,size*(row - 1) + col);
                        if (isFull(row, col + 1)) {
                            grid.union(0, size*(row - 1) + col);
                            connectedTopBottom[size*(row - 1) + col] = (byte) 1;
                            connectNeighbors(row-1, col);
                            connectNeighbors(row + 1, col);
                            connectNeighbors(row, col - 1);
                            connectNeighbors(row, col + 1);
                        }
                    }
                }

                // Difference between if/else if
                if (col < size && col > 1 || col == size) {
                    // to the left
                    if (isOpen(row, col-1)) {
                        grid.union(size*(row - 1) + col-1, size*(row - 1) + col);
                        if (isFull(row, col-1)) {
                            grid.union(size*(row - 1) + col,0 );
                            connectedTopBottom[size*(row - 1) + col] = (byte) 1;
                            connectNeighbors(row-1, col);
                            connectNeighbors(row + 1, col);
                            connectNeighbors(row, col - 1);
                            connectNeighbors(row, col + 1);
                        }
                    }
                }
            }
        }
    }   // open site (row, col) if it is not open already

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IndexOutOfBoundsException();
        }
        return openSites[size*(row-1) + col];
    }  // is site (row, col) open?

    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IndexOutOfBoundsException();
        }

        return connectedTopBottom[size*(row-1) + col] == (byte) 1;
    }  // is site (row, col) full?

    public int numberOfOpenSites()  {
        int count = 0;
        for (int i = 0; i < openSites.length; i++) {
            if (openSites[i]) {
                count++;
            }
        }

        return count;
    }     // number of open sites

    public boolean percolates() {
        return grid.connected(0, size*size + 1);
    }        // does the system percolate?

}
