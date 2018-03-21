package app.Algorithms.Greedy;

import app.Algorithms.Algorithm;
import app.Utilities.Constants;
import app.Entities.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which conducts the Greedy algorithm variation FirstFit.
 * @since 1.0
 */
public class GreedyFirstFit extends Algorithm<List<Parcel>> {

    /**
     * constructor for the GreedyFirstFit algorithm.
     * @param inputParcels          array list containing the input types of the parcels
     * @param typeCounts            integer which tracks how often parcel types are used.
     * @since 1.0
     */
    public GreedyFirstFit(List<Parcel> inputParcels, int[] typeCounts) {
        super(inputParcels, typeCounts);
    }

    /**
     * method used to solve the knapsack problem, this is done by first sorting the parcels from highest to lowest
     * value. Hereafter, every position of the container is checked and it is checked if the parcel is valid
     * -so that it can be placed-. Due to the previously computed sorting the most valuable packages will have
     * the highest priority. where after the container is filled until no further parcels can be inserted, the
     * values are then added to and stored in the variable maxScore which is also printed in the console.
     * @see #isValidForPosition(int, int, int, Parcel)
     * @return                              returns the array list solutionParcels containing all the parcels plus
     *                                      the proper variations which need to be placed in the container.
     * @since 1.0
     */
    @Override
    public List<Parcel> solve() {
        List<Parcel> solutionParcels = new ArrayList<>();

        this.inputParcels.sort((o1, o2) -> Double.compare(
                o2.getValue(),
                o1.getValue()));

        for (int k = 0; k < Constants.CONTAINER_LENGTH; k++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {

                    for(Parcel parcel : inputParcels){
                        if(parcel.isUsed()) continue;

                        for(Parcel variation : parcel.getAllVariations()){
                            if(isValidForPosition(i, j, k, variation)){
                                placeParcel(i, j, k, variation);
                                this.maxScore += parcel.getValue();
                                solutionParcels.add(variation);
                                parcel.setUsed(true);
                                break;
                            }
                        }
                    }

                }
            }
        }

        this.maxScore += this.optimizeCargoSpace(solutionParcels);

        System.out.println(Constants.PRINT_MAX_SCORE_STRING + this.maxScore);

        return solutionParcels;
    }
}
