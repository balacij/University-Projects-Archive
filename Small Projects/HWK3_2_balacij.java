/*
Name: Jason Balaci
MacID: balacij
Description: This program takes one string and prints out all non-contiguous tuple subsets of strings.
*/
public class HWK3_2_balacij { // make the class! :)

    public static void main(String[] args) { // create the main method, which the JVM will look for in running this class
        nonContiguousSubsets(args[0]); // invoke a method which calls another method which finds all non-contiguous subsets, like a proxy or middleman
    }

    public static void nonContiguousSubsets(String set) { // define a method which will start the recursive nonContiguousSubsets_inner method with anticipated default arguments
        nonContiguousSubsets_inner(set, new String[0], 0); // invoke nonContiguousSubsets_inner with the standard arguments
    }

    /**
     * Algorithm (recursive): This algorithm will take in a set of characters in the form of a String and it will
     * 'skip-one-and-add-one' until it has accumulated all possible non-contiguous subsets of the String, then it will
     * print out each of the found sets with cardinality larger than 1.
     *
     * Uses very similar logic to HWK3_3_balacij.subsetsOfSum method.
     *
     * @param set the String containing all values in the given set
     * @param accumulator a String array which is used by an inner recursive method (intended to be used only by the method).
     *                    The first time that this method is called, this parameter is expected to be an empty String array.
     * @param skip an integer used to show which elements of the set have already been exposed to the algorithm.
     *             The first time that this method is called, this parameter is expected to be a 0.
     */
    private static void nonContiguousSubsets_inner(String set, String[] accumulator, int skip) { // define a recursive method called nonContiguousSubsets_inner
        if (skip < set.length()) { // if we've yet to process the entire set, ...
            // skip a character
            nonContiguousSubsets_inner(set, accumulator, skip + 1); // invoke itself again, but this time, skipping a character

            // accumulate a character
            String[] withOneMoreCharacter; // define a String[] array of unknown size which will be determined in the next block of code
            if (accumulator.length == 0) { // if the accumulator array is empty, ...
                withOneMoreCharacter = new String[1]; // ignore it! We need to make a new String array of size 1
                withOneMoreCharacter[0] = set.substring(skip, skip + 1); // make the only element of the next accumulator a String containing a single character, the skip-th character
            } else { // in the event that the method has already accumulated some elements, we need to determine where to put the next character
                String lastFound = accumulator[accumulator.length - 1]; // define a String called lastFound containing the last found contiguous subset in the provided accumulator array
                boolean adjacent = set.indexOf(lastFound) + lastFound.length() - skip == 0; // define a boolean which will determine if the skip-th character is adjacent to the last found contiguous subset

                if (adjacent) { // if lastFound and the skip-th character are contiguous (adjacent), ...
                    withOneMoreCharacter = new String[accumulator.length]; // the next accumulator array will have the same size but the last contiguous subset will be one character larger
                    System.arraycopy(accumulator, 0, withOneMoreCharacter, 0, accumulator.length); // copy over the values already found (so that this doesnt interfere with other recursive calls)
                    withOneMoreCharacter[accumulator.length - 1] = accumulator[accumulator.length - 1] + set.charAt(skip); // set the last found contiguous subset to the concatenation result of the last found contiguous subset with the skip-th character
                } else { // this code block will only be executed if the last found subset and the skip-th character are not contiguous (adjacent)
                    withOneMoreCharacter = new String[accumulator.length + 1]; // make a new String array of size accumulator.length + 1 (the extra 1 is to add another contiguous subset)
                    System.arraycopy(accumulator, 0, withOneMoreCharacter, 0, accumulator.length); // copy over the values already found (so that this doesnt interfere with other recursive calls)
                    withOneMoreCharacter[accumulator.length] = set.substring(skip, skip + 1); // set the last element to a String containing a single character (the skip-th character)
                }
            }
            nonContiguousSubsets_inner(set, withOneMoreCharacter, skip + 1); // invoke itself again, but this time, with one more character
        } else if (accumulator.length > 1) { // if the accumulated array has at least two elements, then we have a non-contiguous subset, so...
            System.out.print("{"); // print out the first curly brace
            for (int i = 0; i < accumulator.length - 1; i++) { // iterate over all the Strings in the accumulated array, and ...
                System.out.print(accumulator[i]); // print each String
                System.out.print(", "); // print out a comma and spacer to space each element (non-contiguous subset)
            }
            System.out.print(accumulator[accumulator.length - 1]); // print out the last element ( it is safe to assume that there is a 'last element' because we already checked if there is at least 2 elements)
            System.out.println("}"); //print out the trailing curly brace, with a new line character
        }
    }

}
