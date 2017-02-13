/*
 * Name: Jason Balaci
 * MacID: balacij
 * Description: this program will allow the user to input N values, to which this program will process them and output the average (mean) and the standard deviation.
 */

// include all the necessary files to calculate sqrt, pow, and convert string to double
#include <stdlib.h>
#include <iostream>
#include <vector>
#include <math.h>

using namespace std;

int main() { // create the main function
    vector<double> numbers; // create a vector of doubles
    string input = ""; // create a temp string containing the users input
    while (true) { // create an infinite loop so that we can get as many inputs as the user wants
        cout << "Enter value " << numbers.size() + 1 << ":" << endl; // tell the user to input the ith + 1 element
        cin >> input; // put the input into the input variable

        if (input == "#") // if input is a hashtag, the user is finished with the list, so we can stop iterating, ...
            break; // ...so break

        numbers.push_back(stod(input)); // otherwise, we will add another number to the vector
        //numbers.push_back(atof(input.c_str()));
    }

    double sum = 0; // create a dummy variable sum
    for (int i = 0; i < numbers.size(); ++i) { // iterate over all the numbers
        sum += numbers[i]; // add them to the dummy sum variable
    }
    double mean = sum / numbers.size(); // calculate the mean (average)

    sum = 0; // set sum to 0 so that we can reuse it for the next summation
    for (int i = 0; i < numbers.size(); ++i) { // iterate over all the numbers
        sum += pow(numbers[i] - mean, 2); // add them to the dummy sum variable after being put through f(x)=(x-x*)^2 where x* = mean
    }

    double standardDeviation = sqrt(sum / numbers.size()); // calculate the standard deviation

    cout << "The average is " << (numbers.size() == 0 ? NAN : ((int) mean)) << endl; // print the average
    cout.setf(ios::fixed); // set the output format to show a fixed number of decimal places
    cout.precision(2); // set the precision to 2
    cout << "The standard deviation is " << (numbers.size() <= 1 ? NAN : standardDeviation) << endl; // print the standard deviation

    return 0; // let the program end gracefully by returning 0
}