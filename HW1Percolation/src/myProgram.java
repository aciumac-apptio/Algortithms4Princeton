import java.util.Scanner;

/**
 * Created by Artem on 4/16/2017.
 */
public class myProgram {
    public static void main(String[] args) {
        String fileName = "input2.txt";

        if (args.length > 0) {
            fileName = args[0];
        }
        Scanner scan = new Scanner(fileName);
        System.out.println(scan.nextLine());
        System.out.println("fileName: " + fileName);
    }
}
