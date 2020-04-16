package com.mycompany.selectionproblem;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Sydney
 */
public class SelectionProblem {

    private static int numExecutions; // Number of times multiplication will be done
    private static int n = 10;
    private static int[] A;
    private static long start, end, elapsedMerge, elapsedIter, elapsedRecurse, elapsedMM;
    private static double mergeSeconds, iterSeconds, recSeconds, mmSeconds;
    private static Random rand = new Random();

    public static void main(String[] args) {
        boolean check = true;
        // If a number of executions is provided, use it
        if (args.length > 0) {
            numExecutions = Integer.parseInt(args[0]);
        } else {
            // Default to 20 times
            numExecutions = 20;
        }
        System.out.println("Number of executions: " + numExecutions);
        while (true) {
            int[] x = {1, n / 4, n / 2, (3 * n) / 4, n};
            A = createArray(n);

            for (int j = 0; j < numExecutions; j++) {
                for (int k : x) {
                    k--; // change k to index-based

                    start = System.nanoTime();
                    selectMerge(A, k);
                    end = System.nanoTime();
                    elapsedMerge += end - start;

                    start = System.nanoTime();
                    selectIterative(A, k);
                    end = System.nanoTime();
                    elapsedIter += end - start;

                    start = System.nanoTime();
                    selectRecursive(A, k);
                    end = System.nanoTime();
                    elapsedRecurse += end - start;

                    start = System.nanoTime();
                    selectMedian(A, k);
                    end = System.nanoTime();
                    elapsedMM += end - start;
                }
            }
            // Multiplying n by 5 and 2 alternating
            n = check ? n * 5 : n * 2;
            check ^= true;

            // Taking the average times of the algorithms
            elapsedMerge = average(elapsedMerge);
            elapsedIter = average(elapsedIter);
            elapsedRecurse = average(elapsedRecurse);
            elapsedMM = average(elapsedMM);

            // Converting nanoseconds to seconds for readability
            mergeSeconds = (double) elapsedMerge / 1_000_000_000;
            iterSeconds = (double) elapsedIter / 1_000_000_000;
            recSeconds = (double) elapsedRecurse / 1_000_000_000;
            mmSeconds = (double) elapsedMM / 1_000_000_000;

            System.out.println("Size n = " + n + ":\n\tMergeSort: " + elapsedMerge + " nanoseconds or " + mergeSeconds
                    + " seconds\n\tQuicksort Iterative: " + elapsedIter + " nanoseconds or " + iterSeconds
                    + " seconds\n\tQuicksort Recursive: " + elapsedRecurse + " nanoseconds or " + recSeconds
                    + " seconds\n\tMedian of Medians: " + elapsedMM + " nanoseconds or " + mmSeconds
                    + " seconds");

        }
    }

    /**
     * This method gives the average time length by dividing the given total
     * nanoseconds of an algorithm by the number of executions
     *
     * @param total total nanoseconds of an algorithm
     * @return the average time taken to execute the algorithm
     */
    private static long average(long total) {
        return total / numExecutions;
    }

    /**
     * This method creates and fills an array of n size and fills it with random
     * numbers
     *
     * @param n size of array to be created
     * @return array with random integers
     */
    private static int[] createArray(int n) {
        int[] temp = new int[n];

        for (int i = 0; i < n; i++) {
            temp[i] = rand.nextInt();
        }
        return temp;
    }

    /**
     * Finds the kth smallest element of an array after using merge sort
     *
     * @param arr array to be sorted
     * @param k kth element to be found
     */
    private static void selectMerge(int[] arr, int k) {
        // Create a copy of the array for to allow for multiple executions
        int[] temp = arr;
        int result;
        sort(temp, 0, temp.length - 1);
        result = temp[k];
    }

    /**
     * Merges two sub-arrays of arr
     *
     * @param arr main array
     * @param l left of first sub-array
     * @param m end of first sub-array
     * @param r end of second sub-array
     */
    private static void merge(int[] arr, int l, int m, int r) {
        // Find sizes of two subarrays to be merged 
        int n1 = m - l + 1;
        int n2 = r - m;
        int L[] = new int[n1];
        int R[] = new int[n2];

        // Copy arrays
        for (int i = 0; i < n1; ++i) {
            L[i] = arr[l + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = arr[m + 1 + j];
        }

        // Begin merging temp arrays
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        // Copy leftover elements of L
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        // Copy leftover elements of Rs
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    /**
     * Main sorting function for merge sort algorithm
     *
     * @param arr array to be sorted
     * @param l left bound
     * @param r right bound
     */
    private static void sort(int[] arr, int l, int r) {
        if (l < r) {
            // Find midpoint 
            int m = (l + r) / 2;

            // Sort two halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    /**
     * Finds the kth smallest element of an array using iterative Quicksort
     *
     * @param arr array to be sorted
     * @param k element to be found
     */
    private static void selectIterative(int[] arr, int k) {
        // Create a copy of the array for to allow for multiple executions
        int[] temp = arr;
        int result;
        quickSortIterative(temp, 0, temp.length - 1, k);
        result = temp[k];
    }

    /**
     * Takes in a random element to pivot around
     *
     * @param arr array to be manipulated
     * @param left left bound
     * @param right right bound
     * @return
     */
    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];

        int i = (left - 1);
        for (int j = left; j <= right - 1; j++) {
            // If current element is smaller, swap elements
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap pivot
        int temp = arr[i + 1];
        arr[i + 1] = arr[right];
        arr[right] = temp;

        return i + 1;
    }

    /**
     * An iterative implementation of random Quicksort
     *
     * @param arr array to be sorted
     * @param left left bound
     * @param right right bound
     */
    private static void quickSortIterative(int[] arr, int left, int right, int k) {
        // Create an auxiliary stack 
        int[] stack = new int[right - left + 1];

        int top = -1;
        stack[++top] = left;
        stack[++top] = right;

        // While stack is non-empty, pop values
        while (top >= 0) {
            right = stack[top--];
            left = stack[top--];

            // Pick a random pivot position, move it to the end of the array
            int temp = left + rand.nextInt(right - left);
            swap(arr, temp, right);
            int p = partition(arr, left, right);
            if (p == k) {
                break;
            }

            // Add in remaining elements on left side of pivot
            if (p - 1 > left) {
                stack[++top] = left;
                stack[++top] = p - 1;
            }

            // Add in remaining elements on right side of pivot
            if (p + 1 < right) {
                stack[++top] = p + 1;
                stack[++top] = right;
            }
        }
    }

    /**
     * Finds the kth smallest element in an array using recursive Quicksort
     *
     * @param arr array to be sorted
     * @param k smallest element
     */
    private static void selectRecursive(int[] arr, int k) {
        // Create a copy of the array for to allow for multiple executions
        int[] temp = arr;
        int result;
        quickSortRecursive(temp, 0, temp.length - 1, k);
        result = temp[k];
    }

    /**
     * A recursive implementation of Quicksort
     *
     * @param arr array to be sorted
     * @param left left bound
     * @param right right bound
     */
    private static void quickSortRecursive(int[] arr, int left, int right, int k) {
        if (left < right) {
            // Pick a random pivot position, move it to the end of the array
            int temp = left + rand.nextInt(right - left);
            swap(arr, temp, right);
            int p = partition(arr, left, right);
            // If the kth smallest element has been partitioned, break
            if (p == k) {
                return;
            }
            // Recursively sort elements before and after partition
            quickSortRecursive(arr, left, p - 1, k);
            quickSortRecursive(arr, p + 1, right, k);
        }
    }

    /**
     * Finds the kth smallest element in an array using recursive Quicksort and
     * Median of Medians rule
     *
     * @param arr array to be sorted
     * @param k smallest element
     */
    private static void selectMedian(int[] arr, int k) {
        // Create a copy of the array for to allow for multiple executions
        int[] temp = arr;
        int result;
        result = kthSmallest(temp, 0, temp.length - 1, k);
    }

    /**
     * Finds the median of a given array
     *
     * @param arr array to be searched through
     * @param i left bound
     * @param n right bound
     * @return the median of the given array
     */
    private static int findMedian(int[] arr, int i, int n) {
        // Sorting array
        if (i <= n) {
            Arrays.sort(arr, i, n);
        } else {
            Arrays.sort(arr, n, i);
        }
        return arr[n / 2]; // Return middle element 
    }

    /**
     * Finds the kth smallest element in a given array using Recursive Quicksort
     * with Median of Medians ruling
     *
     * @param arr array to be sorted
     * @param left left bound
     * @param right right bound
     * @param k kth smallest element
     * @return the kth smallest element of the given array
     */
    private static int kthSmallest(int[] arr, int left, int right, int k) {
        // k must be a valid index
        if (k > 0 && k <= right - left + 1) {
            // Number of elements in the bounds within the array
            int n = right - left + 1;

            // Dividing the array into slices of 5 elements
            int i;

            // Using a floor of (n + 4) / 5 groups, store medians in another array
            int[] median = new int[(n + 4) / 5];
            for (i = 0; i < n / 5; i++) {
                median[i] = findMedian(arr, left + i * 5, 5);
            }

            // Last group with less than 5 elements
            if (i * 5 < n) {
                median[i] = findMedian(arr, left + i * 5, n % 5);
                i++;
            }

            // Find median of all medians recursively
            int medOfMed = (i == 1) ? median[i - 1] : kthSmallest(median, 0, i - 1, i / 2);

            // Partition the array around the median of medians
            int pos = mmPartition(arr, left, right, medOfMed);
            if (pos - left == k) {
                return arr[pos];
            }
            // Recursively partition if not found
            if (pos - left > k) {
                return kthSmallest(arr, left, pos - 1, k);
            }
            return kthSmallest(arr, pos + 1, right, k - pos + left - 1);
        }

        // k is out of bounds 
        return Integer.MAX_VALUE;
    }

    /**
     * Partition helper for median of medians
     *
     * @param arr array to be sorted
     * @param left left bound
     * @param right right bound
     * @param x element to be partitioned
     * @return
     */
    private static int mmPartition(int[] arr, int left, int right, int x) {
        // Search for x, swap with last element in bound
        int i;
        for (i = left; i < right; i++) {
            if (arr[i] == x) {
                break;
            }
        }
        swap(arr, i, right);

        // Standard partition
        return partition(arr, left, right);
    }

    /**
     * Swaps two elements in an array
     *
     * @param arr array with elements to be swapped
     * @param i index of first element
     * @param j index of second element
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
