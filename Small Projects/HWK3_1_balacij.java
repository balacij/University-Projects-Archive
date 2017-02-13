/*
Name: Jason Balaci
MacID: balacij
Description: Accepts two integer arguments n and k, then outputs n choose k (binomial coefficient).
*/
public class HWK3_1_balacij { // create the class! :)

    public static void main(String[] args) { // create the main method, which the JVM will look for in running this class
        int n = Integer.parseInt(args[0]), k = Integer.parseInt(args[1]); // parse two integers; n and k
        System.out.println(binomial(n, k)); // print out (n choose k)
    }

    public static int binomial(int n, int k) { // create a recursive binomial method that implements binomial coefficient
        return (n == k || k == 0) ? 1 : binomial(n - 1, k - 1) + binomial(n - 1, k); // if n == k or k == 0, then we return 1 otherwise we recursively call binomial twice, with n-1,k-1, and n-1,k
    }

}
