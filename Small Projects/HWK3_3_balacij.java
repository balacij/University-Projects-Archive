/*
Name: Jason Balaci
MacID: balacij
Description: This program will take a set of non-negative integers and find all possible subsets that sum up to a specified value.
*/

import java.util.Arrays; // import the Arrays class from the standard Java library

public class HWK3_3_balacij { // create the class! :)

    public static void main(String[] args) { // create the main method, which the JVM will look for in running this class
        int desiredSum = Integer.parseInt(args[args.length - 1]); // parse the desired sum of the subsets
        int[] nums = new int[args.length - 1]; // define an integer array which will contain all the numbers
        for (int i = 0; i < args.length - 1; i++) { // iterate over all the numbers, ...
            nums[i] = Integer.parseInt(args[i]); // parsing them and placing them inside of the nums array
        }

        subsetsOfSum(desiredSum, nums); // invoke subsetsOfSum... which will start the recursion
    }

    public static void subsetsOfSum(int desiredSum, int[] nums) { // define a method which will prepare the arguments and invoke the recursive inner subsetsOfSum_innder method
        int[] copy = new int[nums.length]; // create a deep copy of the nums array in case we need it later
        System.arraycopy(nums, 0, copy, 0, nums.length); // copy over the values from nums to copy
        Arrays.sort(copy); // sort the copy using a sort method from java.util.Arrays
        subsetsOfSum_inner(desiredSum, 0, copy, new int[0]); // start up the recursive method with it's standard default values
    }

    /**
     * This is a very simple recursive algorithm which involves 'skip-one-and-add-one' logic to create power sets (added).
     *
     * @param desiredSum The desired sum of the subsets.
     * @param skip Recursive step: number of elements to skip (because they've already been processed/will be processed by recursion).
     * @param sortedNums The initially provided set of integers, sorted.
     * @param added Recursive step: elements added.
     */
    private static void subsetsOfSum_inner(int desiredSum, int skip, int[] sortedNums, int[] added) { // define the recursive method that will find all the subsets that sum up to some particular value
        int sum = sum(added); // calculate the sum of the added ones
        if (sum == desiredSum && added.length != 0) { // if the sum is the target sum and the set isn't empty, then...
            print(added); // print the set! :)
        } else if (skip < sortedNums.length  && sum < desiredSum) { // otherwise, if skip hasn't yet passed through all the elements and the sum is still too small, then...
            subsetsOfSum_inner(desiredSum, skip + 1, sortedNums, added); // skip value and perform recursion
            subsetsOfSum_inner(desiredSum, skip + 1, sortedNums, addOne(added, sortedNums[skip])); // add one value and perform recursion
        }
    }

    public static int[] addOne(int[] nums, int val) { // create a method which will return a new array with all the provided elements, as well as another provided value
        int[] result = new int[nums.length + 1]; // define a new resultant array
        System.arraycopy(nums, 0, result, 0, nums.length); // copy over the old values
        result[result.length - 1] = val; // set the last value to the new value
        return result; // return the resultant
    }

    public static int sum(int[] nums) { // create a method which will return the sum of the provided integers
        int total = 0; // create an accumulator number
        for (int i = 0; i < nums.length; i++) { // iterate over the numbers, ...
            total += nums[i]; // adding them to the accumulator
        }
        return total; // return the found total
    }

    public static void print(int[] nums) { // create a method which will print out all the elements in an integer array, in a pretty way
        System.out.print("{"); // print out an opening curly brace
        for (int i = 0; i < nums.length - 1; i++) { // iterate over all but the last integers, ...
            System.out.print(nums[i]); // printing them
            System.out.print(", "); // printing a comma and separator
        }
        if (nums.length > 0) // if we've got at least one value in nums, then ...
            System.out.print(nums[nums.length - 1]); // print out the last value
        System.out.println("}"); // print out the trailing/closing curly brace
    }

}
