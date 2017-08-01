
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by eugenec on 6/12/2017.
 */
public class Board {
    private int size;
    private int[] tiles;
    private boolean isGoal;
    private int hamming;
    private int manhattan;
    private int zero;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        size = blocks[0].length;
        tiles = new int[size * size];
        if (blocks[size - 1][size - 1] == 0) {
            isGoal = true;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i*size + j] = blocks[i][j];

                if (tiles[i*size + j] == 0) {
                    zero = i*size + j;
                } else if (i*size + j != tiles[i*size + j] - 1){
                    isGoal = false;
                    hamming++;
                    int goal_i = row(blocks[i][j] - 1);
                    int goal_j = col(blocks[i][j] - 1);
                    manhattan += (Math.abs(goal_i - i) + Math.abs(goal_j - j));
                }

            }
        }
    }

    private Board(int[] blocks) {
        size = (int) Math.sqrt(blocks.length);
        tiles = blocks;
        if (tiles[size * size - 1] == 0) {
            isGoal = true;
        }

        for (int i = 0; i < size*size; i++) {
                if (tiles[i] == 0) {
                    zero = i;
                } else if (i != tiles[i] - 1){
                    isGoal = false;
                    hamming++;
                    int goal_i = row(tiles[i] - 1);
                    int goal_j = col(tiles[i] - 1);
                    manhattan += Math.abs(goal_i - row(i)) + Math.abs(goal_j - col(i));
                }
        }
    }

    private int row(int i) {
        return i / size;
    }

    private int col(int i) {
        return i % size;
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return isGoal;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[] twin = Arrays.copyOf(tiles, tiles.length);
/*        Random rand = new Random();
        int i = rand.nextInt(size);
        int j = rand.nextInt(size);
        while (twin[i] == 0 || twin[j] == 0) {
            i = rand.nextInt(size);
            j = rand.nextInt(size);
        }

        swap(twin, i , j);*/

        for (int i = 0; i < twin.length; i += size){
            if (twin[i] != 0 && twin[i+1] != 0) {
                swap(twin, i, i+1);
                break;
            }

        }
        return new Board(twin);
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y instanceof Board && (this.tiles.length  == ((Board) y).tiles.length)) {
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i] != ((Board) y).tiles[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // all neighboring boards
    // maybe fix location of 0 in constructor
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new LinkedList<>();
        int row = row(zero);
        int col = col(zero);
        int index;
        int[] neighbor;
        if (row > 0) {
            //swap with element above
            index = size * (row - 1) + col;
            neighbor = Arrays.copyOf(tiles, tiles.length);
            swap(neighbor, index, zero);
            neighbors.add(new Board(neighbor));
        }

        if (row < size - 1) {
            // swap with element below
            index = size * (row + 1) + col;
            neighbor = Arrays.copyOf(tiles, tiles.length);
            swap(neighbor, index, zero);
            neighbors.add(new Board(neighbor));
        }

        if (col > 0) {
            // swap with element to the left
            index = size * row + col - 1;
            neighbor = Arrays.copyOf(tiles, tiles.length);
            swap(neighbor, index, zero);
            neighbors.add(new Board(neighbor));
        }

        if (col < size -1) {
            // swap with element to the right
            index = size * row + col + 1;
            neighbor = Arrays.copyOf(tiles, tiles.length);
            swap(neighbor, index, zero);
            neighbors.add(new Board(neighbor));
        }

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", tiles[i*size + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[] goal = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        int[][] test = {{1, 6, 7}, {2, 5, 0}, {3, 4, 8}};
        int[][] other = {{1, 2, 3}, {4, 0, 8}, {7, 5, 6}};
        int[][] manhattan = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board_goal = new Board(goal);
        Board board_test = new Board(test);
        Board board_other = new Board(other);
        //Board board_manhattan = new Board(manhattan);
        /*System.out.println("Testing isGoal() method");
        System.out.println("Expect: TRUE");
        System.out.println(board_goal.isGoal());
        System.out.println("Expect: FALSE");
        System.out.println(board_test.isGoal());
        System.out.println();*/

        /*System.out.println("Testing equals() method");
        System.out.println("Expect: TRUE");
        System.out.println(board_goal.equals(board_goal));
        System.out.println("Expect: FALSE");
        System.out.println(board_goal.equals(board_other));
        System.out.println("Expect: FALSE");
        System.out.println(board_goal.equals(board_test));
        System.out.println();

        System.out.println("Testing toString() method");
        System.out.println(board_goal.toString());
        System.out.println(board_test.toString());
        System.out.println(board_other.toString());
        System.out.println();*/

        System.out.println("Testing twin method");
        System.out.println(board_goal.twin());
        System.out.println(board_test.twin());
        System.out.println(board_other.twin());
        System.out.println();

        /*System.out.println("Testing neighbors method");
        System.out.println("Expect 2 neighbors");
        System.out.println(board_goal.neighbors().toString());
        System.out.println("Expect 4 neighbors");
        System.out.println(board_other.neighbors().toString());
        System.out.println("Expect 3 neighbors");
        System.out.println(board_test.neighbors().toString());
        System.out.println();

        System.out.println("Testing hamming() method");
        System.out.println("Expect 0");
        System.out.println(board_goal.hamming());
        System.out.println("Expect 3");
        System.out.println(board_other.hamming());
        System.out.println("Expect 6");
        System.out.println(board_test.hamming());
        System.out.println();

        System.out.println("Testing manhattan() method");
        System.out.println("Expect 0");
        System.out.println(board_goal.manhattan());
        System.out.println("Expect 4");
        System.out.println(board_other.manhattan());
        System.out.println("Expect 15");
        System.out.println(board_test.manhattan());
        System.out.println("Expect 10");
        System.out.println(board_manhattan.manhattan());
        System.out.println();

        int[] test1 = {1, 0, 3, 4, 2, 5, 7, 8, 6};
        Board board_test1 = new Board(test1);
        System.out.println("Expect 2");
        System.out.println(board_test1.manhattan());

        int[] test2 = {4, 1, 3, 0, 2, 5, 7, 8, 6};
        Board board_test2 = new Board(test2);
        System.out.println("Expect 5");
        System.out.println(board_test2.manhattan());*/
    }

}
