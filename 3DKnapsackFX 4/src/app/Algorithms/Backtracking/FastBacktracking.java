package app.Algorithms.Backtracking;

import app.Algorithms.Algorithm;
import app.Utilities.Constants;
import app.Entities.Parcel;

import java.util.List;

/**
 * Class which conducts the FastBackTracking algorithm.
 * @since 1.0
 */
public class FastBacktracking extends Algorithm<char[][][]> {

    /**
     * Constructor for the FastBacktracking class.
     * @param inputParcels          array list containing the input types of the parcels
     * @param typeCounts            integer which tracks how often parcel types are used.
     * @since 1.0
     */
    public FastBacktracking(List<Parcel> inputParcels, int[] typeCounts) {
        super(inputParcels, typeCounts);
    }

    /**
     * Method which first checks the container for empty places and then copies
     * a full container into it. This method is as the backbone for the fast backtracking algorithm.
     *
     * @return                          returns a container containing the final solution.
     * @since 1.0
     */
    @Override
    public char[][][] solve() {
        int currentScore = 0;
        this.allParcels.sort((p, q) -> Integer.compare(q.getValue(), p.getValue()));

        for (int k = 0; k < Constants.CONTAINER_LENGTH; k++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
                    if (this.bestContainer[i][j][k] != ' ') continue;

                    this.copyFromTo(this.bestContainer, this.container);

                    backtrackingRecursionFast(0, Constants.FAST_BACKTRACKING_DEPTH, i, j, k, currentScore);

                    this.adjustTypeCounts(this.globalTypeCounts);
                    currentScore = this.maxScore;
                }
            }
        }

        System.out.println(Constants.PRINT_MAX_SCORE_STRING + this.maxScore);

        return this.bestContainer;
    }

    /**
     * Recursive method which is used for the backtracking algorithm.
     * First the validity gets checked, where after the parcel gets placed and recorded that it has been used.
     *
     * Simultaneously, the score gets updated if the current score is higher. The next empty space is found by the
     * For()loops and the recursion of the method happens. When
     * however the For()loops do not find an empty space the parcel gets deleted.
     * @param idx                                       counter for the number of iterations the method can make
     * @param end                                       the maximum iterations the method can make
     * @param ni                                        dimension of parcel
     * @param nj                                        dimension of parcel
     * @param nk                                        dimension of parcel
     * @param currentScore                              the current score of the algorithm
     * @since 1.0
     */
    private void backtrackingRecursionFast(int idx, int end, int ni, int nj, int nk, int currentScore) {
        if(idx > end) return;

        for(Parcel currentParcel : this.allParcels){
            Character type = currentParcel.getType();

            if(this.typeCounts.get(type) == 0) continue;

            if(isValidForPosition(ni, nj, nk, currentParcel)){
                currentScore += currentParcel.getValue();
                this.typeCounts.put(type, this.typeCounts.get(type) - 1);
                this.placeParcel(ni, nj, nk, currentParcel);

                if(currentScore > this.maxScore){
                    this.maxScore = currentScore;
                    this.saveContainer();
                    this.saveGlobalTypeCounts();
                }

                boolean found = false;
                for(int k = 0;k<Constants.CONTAINER_LENGTH && !found;k++) {
                    for (int j = 0; j < Constants.CONTAINER_HEIGHT && !found; j++) {
                        for (int i = 0; i < Constants.CONTAINER_WIDTH && !found; i++) {
                            if(this.container[i][j][k] == Constants.NOT_TRAVERSED_EMPTY_SYMBOL){
                                found = true;
                                backtrackingRecursionFast(idx + 1, end, i, j, k, currentScore);
                            }
                        }
                    }
                }

                currentScore -= currentParcel.getValue();
                this.typeCounts.put(type, this.typeCounts.get(type) + 1);
                this.revertPlaceParcel(ni, nj, nk, currentParcel);
            }
        }
    }
}
