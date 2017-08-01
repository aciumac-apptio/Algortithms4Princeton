import java.util.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by eugenec on 6/12/2017.
 */
public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private Node solution;
    private int moves = 0;

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }

        //Two queues one for original, one for twin
        MinPQ<Node> pq1 = new MinPQ<>();
        MinPQ<Node> pq2 = new MinPQ<>();
        pq1.insert(new Node(initial, 0 , null));
        pq2.insert(new Node(initial.twin(), 0 , null));

        while (!pq1.min().board.isGoal() && !pq2.min().board.isGoal()) {
            Node n1 = pq1.delMin();
            Node n2 = pq2.delMin();

            for (Board board : n1.board.neighbors()){
                if (n1.prev == null || !board.equals(n1.prev.board)) {
                    pq1.insert(new Node(board, n1.moves + 1, n1));
                }
            }

            for (Board board : n2.board.neighbors()){
                if (n2.prev == null || !board.equals(n2.prev.board)) {
                    pq2.insert(new Node(board, n2.moves + 1, n2));
                }
            }
        }

        if (!pq1.min().board.isGoal()) {
            moves = -1;
        } else {
            this.solution = pq1.delMin();
            moves = solution.moves;
        }

        System.out.println();
    }

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private int manhattan;
        private Node prev;


        public Node(Board board, int moves, Node prev){
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhattan = board.manhattan();
        }

        @Override
        // Highest priority - smallest distance
        public int compareTo(Node o) {
            if (this.manhattan + moves > o.manhattan + o.moves){
                return 1;
            } else if (this.manhattan + moves < o.manhattan + o.moves) {
                return -1;
            } else{
                return 0;
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
       return moves;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
       if(isSolvable()) {
           Stack<Board> stack = new Stack<>();
           Node current = solution;
           while (current.prev != null) {
               stack.push(current.board);
               current = current.prev;
           }
           stack.push(current.board);

           // From stack to queue
           Queue<Board> queue = new LinkedList<>();
           while (!stack.isEmpty()) {
               queue.add(stack.pop());
           }

           // From queue to stack -> order reversed
           while(!queue.isEmpty()) {
               stack.push(queue.remove());
           }

           return stack;
       } else {
           return null;
       }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In("puzzle4x4-50.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
