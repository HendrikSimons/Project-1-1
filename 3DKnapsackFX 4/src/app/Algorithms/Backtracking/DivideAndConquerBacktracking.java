package app.Algorithms.Backtracking;

import javafx.geometry.Point3D;
import app.Algorithms.Algorithm;
import app.Utilities.Constants;
import app.Entities.Parcel;

import java.util.List;

public class DivideAndConquerBacktracking extends Algorithm<char[][][]> {
    /**
     * Class which conducts the DivideAndConquer algorithm.
     * @param inputParcels          array list containing the input types of the parcels
     * @param typeCounts            integer which tracks how often parcel types are used.
     * @since 1.0
     */
    private int currentMaxScore = 0;

    /**
     * Constructor for the DivideAndConquerBacktracking class
     * @since 1.0
     */
    public DivideAndConquerBacktracking(List<Parcel> inputParcels, int[] typeCounts) {
        super(inputParcels, typeCounts);
    }

    /**
     * Method used to find the first empty space in the container, hereafter the recursion method gets called.
     * The result of the
     */
    @Override
    public char[][][] solve() {
        int currentScore = 0;

        while (true) {
            Point3D empty = this.getFirstEmpty(this.container);
            if (empty == null) break;

            int x = (int) empty.getX();
            int y = (int) empty.getY();
            int z = (int) empty.getZ();

            leveledBacktrackingRecursion(currentScore, x, y, z,
                    Constants.INITIAL_DEPTH, Constants.DIVIDE_AND_CONQUER_BACKTRACKING_DEPTH);

            this.solutionFound = false;

            if (this.bestContainer[x][y][z] == Constants.NOT_TRAVERSED_EMPTY_SYMBOL)
                this.bestContainer[x][y][z] = Constants.ALREADY_TRAVERSED_SYMBOL;

            this.adjustTypeCounts(this.globalTypeCounts);

            this.copyFromTo(this.bestContainer, this.container);
            currentScore = 0;
            this.maxScore += currentMaxScore;
            currentMaxScore = 0;

            System.out.println(this.maxScore);
        }

        System.out.println(Constants.PRINT_MAX_SCORE_STRING + this.maxScore);

        return this.bestContainer;
    }

    /**
     * The recursive method for Divide and Conquer Backtracking
     * @param currentScore          the current score
     * @param x         X coordinate
     * @param y         Y coordinate
     * @param z         Z coordinate
     * @param depth     current depth of the recursion
     * @param maxDepth  maximum depth of the recursion
     */
    private void leveledBacktrackingRecursion(int currentScore, int x, int y, int z, int depth, int maxDepth){
        if(depth == maxDepth) return;

        for (Parcel currentParcel : this.allParcels) {
            Character type = currentParcel.getType();

            if(this.typeCounts.get(type) == 0) continue;

            if (isValidForPosition(x, y, z, currentParcel)) {

                this.placeParcel(x, y, z, currentParcel);
                this.typeCounts.put(type, this.typeCounts.get(type) - 1);
                currentScore+= currentParcel.getValue();

                if(this.currentMaxScore < currentScore) {
                    this.copyFromTo(this.container, this.bestContainer);
                    this.currentMaxScore = currentScore;
                    this.saveGlobalTypeCounts();
                }

                boolean found = false;
                for (int k = 0; k < Constants.CONTAINER_LENGTH && !found; k++) {
                    for (int i = 0; i < Constants.CONTAINER_WIDTH && !found; i++) {
                        for (int j = 0; j < Constants.CONTAINER_HEIGHT && !found; j++) {
                            if (container[i][j][k] == ' ') {
                                found = true;

                                leveledBacktrackingRecursion(currentScore, k, j, i, depth+1, maxDepth);
                            }
                        }
                    }
                }

                this.revertPlaceParcel(x, y, z, currentParcel);
                this.typeCounts.put(type, this.typeCounts.get(type) + 1);
                currentScore-= currentParcel.getValue();
            }
        }
    }
}
