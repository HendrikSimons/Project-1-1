package app.Algorithms.Backtracking;

import javafx.geometry.Point3D;
import app.Algorithms.Algorithm;
import app.Utilities.Constants;
import app.Entities.Parcel;
import app.Entities.ParcelComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which conducts the TraceBackTracking algorithm.
 * @since 1.0
 */
public class TracedBacktracking extends Algorithm<List<Parcel>> {

    /**
     * Constructor for the TracedBackTracking class.
     * @param inputParcels          array list containing the input types of the parcels
     * @param typeCounts            integer which tracks how often parcel types are used.
     * @since 1.0
     */
    public TracedBacktracking(List<Parcel> inputParcels, int[] typeCounts) {
        super(inputParcels, typeCounts);
    }

    /**
     * Method which first checks the container for empty places and then copies
     * a full container into it. This method is used to trace the backtracking algorithm so that it
     * gives a clear view on how the backtracking works.
     *
     * @return                          returns and array list containing the final solution
     */
    @Override
    public List<Parcel> solve() {
        List<Parcel> solutionParcels = new ArrayList<>();
        int currentScore = 0;

        for(int k = 0; k< Constants.CONTAINER_LENGTH; k++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
                    if(this.bestContainer[i][j][k] != ' ') continue;

                    this.copyFromTo(this.bestContainer, this.container);

                    for(Parcel parcel : this.solutionParcels){
                        this.inputParcels.stream()
                                .filter(p -> p.getId() == parcel.getId())
                                .forEach(p -> p.setUsed(true));
                    }

                    backtrackingRecursion(solutionParcels, Constants.INITIAL_DEPTH,
                            Constants.TRACEABLE_BACKTRACKING_DEPTH, i, j, k, currentScore);

                    for(Parcel parcel : this.solutionParcels){
                        if(this.finalSolutionParcels.stream()
                                .noneMatch(p -> p.getId() == parcel.getId()))
                            this.finalSolutionParcels.add(parcel);
                    }

                    currentScore = this.maxScore;
                }
            }
        }

        System.out.println(Constants.PRINT_MAX_SCORE_STRING + this.maxScore);

        return this.finalSolutionParcels;
    }

    /**
     * Recursive method which is used for the backtracking algorithm.
     * First the validity gets checked, where after the parcel gets placed and recorded that it has been used.
     *
     * Simultaneously, the score gets updated if the current score is higher. The next empty space is found by the
     * #getFirstEmpty(container) method its coordinates are saved and the recursion of the method happens. When
     * however the #getFirstEmpty(container) method does not find an empty space the parcel tries a variation of
     * itself after which it gets deleted -when the variation also does not find itself to be placeable.
     * @param idx                                       counter for the number of iterations the method can make
     * @param end                                       the maximum iterations the method can make
     * @param ni                                        dimension of parcel
     * @param nj                                        dimension of parcel
     * @param nk                                        dimension of parcel
     * @param currentScore                              the current score of the algorithm
     * @since 1.0
     */
    private void backtrackingRecursion(List<Parcel> solution, int idx, int end, int ni, int nj, int nk, int currentScore) {
        if(idx > end) return;

        for(Parcel currentParcel : this.inputParcels){

            if(currentParcel.isUsed()) continue;

            for(Parcel variationParcel : currentParcel.getAllVariations()){

                if(isValidForPosition(ni, nj, nk, variationParcel)){
                    currentScore += variationParcel.getValue();
                    currentParcel.setUsed(true);
                    this.placeParcel(ni, nj, nk, variationParcel);
                    solution.add(new Parcel(variationParcel));

                    if(currentScore > this.maxScore){
                        this.maxScore = currentScore;
                        this.saveSolutions(solution);
                        this.saveContainer();
                    }

                    Point3D found = this.getFirstEmpty(this.container);
                    int x = (int) found.getX();
                    int y = (int) found.getY();
                    int z = (int) found.getZ();

                    if(x >= 0){
                        backtrackingRecursion(solution,idx + 1, end, x, y, z, currentScore);
                    }

                    currentScore -= variationParcel.getValue();
                    currentParcel.setUsed(false);
                    this.revertPlaceParcel(ni, nj, nk, variationParcel);
                    solution.removeIf(p -> p.getId() == variationParcel.getId());
                }
            }
        }
    }

}
