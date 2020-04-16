/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
                    k--;
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
     * numbers 0-99 inclusive
     *
     * @param n size of array to be created
     * @return array with random integers 0-99 inclusive
     */
    private static int[] createArray(int n) {
        Random rand = new Random();
        int[] temp = new int[n];

        for (int i = 0; i < n; i++) {
            temp[i] = rand.nextInt(100);
        }
        return temp;
    }

    /**
     * Finds the kth element of an array after using merge sort
     *
     * @param arr array to be sorted
     * @param k kth element to be found
     */
    private static void selectMerge(int[] arr, int k) {
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

        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i) {
            L[i] = arr[l + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = arr[m + 1 + j];
        }

        /* Merge the temp arrays */
        // Initial indexes of first and second subarrays 
        int i = 0, j = 0;

        // Initial index of merged subarry array 
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

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
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
            // Find the middle point 
            int m = (l + r) / 2;

            // Sort first and second halves 
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves 
            merge(arr, l, m, r);
        }
    }

    /**
     *
     * @param arr
     * @param k
     */
    private static void selectIterative(int[] arr, int k) {
        int[] temp = arr;
        int result;
        quickSortIterative(temp, 0, temp.length - 1, k);
        result = temp[k];
    }

    /**
     *
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private static int partition(int arr[], int low, int high) {
        int pivot = arr[high];

        // index of smaller element 
        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {
            // If current element is smaller than or 
            // equal to pivot 
            if (arr[j] <= pivot) {
                i++;

                // swap arr[i] and arr[j] 
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot) 
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /**
     *
     * @param arr
     * @param l
     * @param h
     */
    private static void quickSortIterative(int arr[], int l, int h, int k) {
        // Create an auxiliary stack 
        int[] stack = new int[h - l + 1];

        // initialize top of stack 
        int top = -1;

        // push initial values of l and h to stack 
        stack[++top] = l;
        stack[++top] = h;

        // Keep popping from stack while is not empty 
        while (top >= 0) {
            // Pop h and l 
            h = stack[top--];
            l = stack[top--];

            // Set pivot element at its correct position 
            // in sorted array 
            int p = partition(arr, l, h);
            if (p == k) {
                break;
            }

            // If there are elements on left side of pivot, 
            // then push left side to stack 
            if (p - 1 > l) {
                stack[++top] = l;
                stack[++top] = p - 1;
            }

            // If there are elements on right side of pivot, 
            // then push right side to stack 
            if (p + 1 < h) {
                stack[++top] = p + 1;
                stack[++top] = h;
            }
        }
    }

    /**
     *
     * @param arr
     * @param low
     * @param high
     */
    private static void quickSortRecursive(int arr[], int low, int high, int k) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is 
            now at right place */
            int pi = partition(arr, low, high);
            if (pi == k) {
                return;
            }
            // Recursively sort elements before 
            // partition and after partition 
            quickSortRecursive(arr, low, pi - 1, k);
            quickSortRecursive(arr, pi + 1, high, k);
        }
    }

    /**
     *
     * @param arr
     * @param k
     */
    private static void selectRecursive(int[] arr, int k) {
        int[] temp = arr;
        int result;
        quickSortRecursive(temp, 0, temp.length - 1, k);
        result = temp[k];
    }

    /**
     *
     * @param arr
     * @param k
     */
    private static void selectMedian(int[] arr, int k) {
        int[] temp = arr;
        int result;
        result = kthSmallest(temp, 0, temp.length - 1, k);
    }

    /**
     *
     * @param arr
     * @param i
     * @param n
     * @return
     */
    private static int findMedian(int arr[], int i, int n) {
        if (i <= n) {
            Arrays.sort(arr, i, n); // Sort the array 
        } else {
            Arrays.sort(arr, n, i);
        }
        return arr[n / 2]; // Return middle element 
    }

    /**
     *
     * @param arr
     * @param l
     * @param r
     * @param k
     * @return
     */
    private static int kthSmallest(int arr[], int l, int r, int k) {
        // If k is smaller than  
        // number of elements in array 
        if (k > 0 && k <= r - l + 1) {
            int n = r - l + 1; // Number of elements in arr[l..r] 

            // Divide arr[] in groups of size 5,  
            // calculate median of every group 
            //  and store it in median[] array. 
            int i;

            // There will be floor((n+4)/5) groups; 
            int[] median = new int[(n + 4) / 5];
            for (i = 0; i < n / 5; i++) {
                median[i] = findMedian(arr, l + i * 5, 5);
            }

            // For last group with less than 5 elements 
            if (i * 5 < n) {
                median[i] = findMedian(arr, l + i * 5, n % 5);
                i++;
            }

            // Find median of all medians using recursive call. 
            // If median[] has only one element, then no need 
            // of recursive call 
            int medOfMed = (i == 1) ? median[i - 1]
                    : kthSmallest(median, 0, i - 1, i / 2);

            // Partition the array around a random element and 
            // get position of pivot element in sorted array 
            int pos = partition(arr, l, r, medOfMed);
            if (pos == k) {
                return pos;
            }

            // If position is same as k 
            if (pos - l == k - 1) {
                return arr[pos];
            }
            if (pos - l > k - 1) // If position is more, recur for left 
            {
                return kthSmallest(arr, l, pos - 1, k);
            }

            // Else recur for right subarray 
            return kthSmallest(arr, pos + 1, r, k - pos + l - 1);
        }

        // If k is more than number of elements in array 
        return Integer.MAX_VALUE;
    }

    /**
     *
     * @param arr
     * @param l
     * @param r
     * @param x
     * @return
     */
    private static int partition(int arr[], int l,
            int r, int x) {
        // Search for x in arr[l..r] and move it to end 
        int i;
        for (i = l; i < r; i++) {
            if (arr[i] == x) {
                break;
            }
        }
        swap(arr, i, r);

        // Standard partition algorithm 
        i = l;
        for (int j = l; j <= r - 1; j++) {
            if (arr[j] <= x) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, r);
        return i;
    }

    /**
     *
     * @param arr
     * @param i
     * @param j
     * @return
     */
    private static int[] swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return arr;
    }
}
