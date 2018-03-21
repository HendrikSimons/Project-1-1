package app.Algorithms.Greedy;

import app.Algorithms.Algorithm;
import app.Entities.Parcel;
import app.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which conducts the Greedy algorithm variation NextFit.
 * @since 1.0
 */
public class GreedyNextFit extends Algorithm<List<Parcel>> {

    /**
     * constructor of the GreedyNextFit class.
     * @param inputParcels          array list containing the input types of the parcels
     * @param typeCounts            integer which tracks how often parcel types are used.
     * @since 1.0
     */
    public GreedyNextFit(List<Parcel> inputParcels, int[] typeCounts) {
        super(inputParcels, typeCounts);
    }

    @Override
    public List<Parcel> solve() {
        List<Parcel> solutionParcels = new ArrayList<>();

        this.inputParcels.sort((o1, o2) -> Double.compare(
                o2.getValue(),
                o1.getValue()));

        for(Parcel parcel : inputParcels){
            for(Parcel variation : parcel.getAllVariations()){
                int[] success = tryToPlaceParcel(variation);

                if(success[0] == Constants.PLACED_PARCEL_SUCCESS_CODE){
                    placeParcel(success[1], success[2], success[3], variation);
                    this.maxScore += parcel.getValue();
                    solutionParcels.add(variation);
                    break;
                }
            }
        }

        this.maxScore += this.optimizeCargoSpace(solutionParcels);

        System.out.println(Constants.PRINT_MAX_SCORE_STRING + this.maxScore);

        return solutionParcels;
    }
}
