package app.Algorithms.DynamicProgramming;

import app.Algorithms.Algorithm;
import app.Entities.Parcel;
import app.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which conducts the dynamic programming algorithm.
 * @since 1.0
 */
public class DynamicProgramming extends Algorithm<List<Parcel>> {

    /**
     * constructor for the DynamicProgramming class.
     * @param inputParcels          array list containing the input types of the parcels
     * @param typeCounts            integer which tracks how often parcel types are used.
     * @since 1.0
     */
    public DynamicProgramming(List<Parcel> inputParcels, int[] typeCounts) {
        super(inputParcels, typeCounts);
    }

    /**
     * method used to solve the knapsack problem, in here the values get initialized and computed along with the
     * weights, weightCapacity and the total counter -the arrays get filled with values-.
     * Hereafter, the maxScore is calculated by using the #knapsack(int[], int[], int) method.
     * @see                                     #knapsack(int[], int[], int)
     * @return                                  returns an empty array list.
     * @since 1.0
     */
    @Override
    public List<Parcel> solve() {

        int allObjectsCount = 0;
        int valuesIdx = 0, weightIdx = 0;
        int weightCapacity = Constants.MAX_VOLUME_OF_CONTAINER;

        for(Character key : this.typeCounts.keySet())
            allObjectsCount += this.typeCounts.get(key);

        int values[] = new int[allObjectsCount];
        int weights[] = new int[allObjectsCount];

        for(int i=0;i<this.typeCounts.get('l');i++) {
            values[valuesIdx++] = Constants.PARCEL_L_INITIAL_SCORE;
            weights[weightIdx++] = Constants.VOLUME_OF_PENTOMINO;
        }

        for(int i=0;i<this.typeCounts.get('p');i++) {
            values[valuesIdx++] = Constants.PARCEL_P_INITIAL_SCORE;
            weights[weightIdx++] = Constants.VOLUME_OF_PENTOMINO;
        }

        for(int i=0;i<this.typeCounts.get('t');i++) {
            values[valuesIdx++] = Constants.PARCEL_T_INITIAL_SCORE;
            weights[weightIdx++] = Constants.VOLUME_OF_PENTOMINO;
        }

        for(int i=0;i<this.typeCounts.get('a');i++) {
            values[valuesIdx++] = Constants.PARCEL_A_INITIAL_SCORE;
            weights[weightIdx++] = Constants.VOLUME_OF_PARCEL_A;
        }

        for(int i=0;i<this.typeCounts.get('b');i++) {
            values[valuesIdx++] = Constants.PARCEL_B_INITIAL_SCORE;
            weights[weightIdx++] = Constants.VOLUME_OF_PARCEL_B;
        }

        for(int i=0;i<this.typeCounts.get('c');i++) {
            values[valuesIdx++] = Constants.PARCEL_C_INITIAL_SCORE;
            weights[weightIdx++] = Constants.VOLUME_OF_PARCEL_C;
        }

        this.maxScore = this.knapsack(values, weights, weightCapacity);
        System.out.println(Constants.PRINT_MAX_SCORE_STRING + this.maxScore);

        return new ArrayList<>();
    }

    /**
     * This method is used to calculate the maxScore of the algorithm. First the length of the double array is
     * initialized by using the length of the weights array and the weightCapacity.
     * Hereafter, the initial state of the matrix is set up and finally the highest values are inserted into the
     * DPMatrix, which eventually outputs the maximum score.
     * @param values                    array containing the values of the parcels
     * @param weights                   array containing the weights of the parcels
     * @param weightCapacity            constant defining the max capacity of the matrix
     * @retun                           returns the maximum computed score of the matrix
     * @since 1.0
     */
    private int knapsack(int values[], int weights[], int weightCapacity) {
        int N = weights.length;
        int[][] DPMatrix = new int[N + 1][weightCapacity + 1];

        for (int col = 0; col <= weightCapacity; col++) DPMatrix[0][col] = 0;
        for (int row = 0; row <= N; row++) DPMatrix[row][0] = 0;

        for (int item = 1; item <= N; item++){
            for (int weight = 1; weight <= weightCapacity; weight++){
                if (weights[item - 1]<=weight){
                    DPMatrix[item][weight] = Math.max (
                            values[item - 1] + DPMatrix[item - 1][weight - weights[item - 1]],
                            DPMatrix[item - 1][weight]);
                } else {
                    DPMatrix[item][weight] = DPMatrix[item-1][weight];
                }
            }
        }

        return DPMatrix[N][weightCapacity];
    }
}
