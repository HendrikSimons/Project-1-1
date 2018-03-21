package app.Algorithms;

import javafx.geometry.Point3D;
import app.Factories.ParcelFactory;
import app.Utilities.Constants;
import app.Entities.Parcel;
import app.Entities.ParcelComponent;

import java.util.*;

public abstract class Algorithm<T> {

    /**
     * The abstract class Algorithm is used by all the algorithms. It is in other words the centralized class which
     * contains the tools for the different algorithm variations to work with.
     * @param globalTypeCounts
     * @param inputParcels
     * @param allParcels
     * @param solutionParcels
     * @param finalSolutionParcels
     * @param container
     * @param bestContainer
     * @param typeCounts
     * @param maxScore
     * @param solutionFound
     * @since 1.0
     */
    protected int[] globalTypeCounts;
    protected List<Parcel> inputParcels;
    protected List<Parcel> allParcels;
    protected List<Parcel> solutionParcels;
    protected List<Parcel> finalSolutionParcels;
    protected char[][][] container;
    protected char[][][] bestContainer;
    protected Map<Character, Integer> typeCounts;

    protected int maxScore;
    protected boolean solutionFound;

    /**
     * the constructor for the algorithm class.
     * @since 1.0
     */
    protected Algorithm(List<Parcel> inputParcels, int[] typeCounts){
        this.maxScore = 0;
        this.inputParcels = inputParcels;
        this.globalTypeCounts = new int[Constants.SIZE_OF_PARCEL_TYPES];

        this.typeCounts = new HashMap<>();
        this.allParcels = new ArrayList<>();
        for(Parcel parcel : this.inputParcels){
            this.allParcels.addAll(parcel.getAllVariations());
        }

        this.allParcels.sort(Comparator.comparingInt(Parcel::getValue));

        this.adjustTypeCounts(typeCounts);
        this.saveGlobalTypeCounts();

        this.solutionParcels = new ArrayList<>();
        this.finalSolutionParcels = new ArrayList<>();

        this.container = new char[Constants.CONTAINER_WIDTH][Constants.CONTAINER_HEIGHT][Constants.CONTAINER_LENGTH];
        this.bestContainer = new char[Constants.CONTAINER_WIDTH][Constants.CONTAINER_HEIGHT][Constants.CONTAINER_LENGTH];
        this.makeContainerEmpty(this.container);
        this.makeContainerEmpty(this.bestContainer);
    }

    /**
     * Method used to track the amount of parcels of a certain type are inserted into the Cargo space.
     * @since 1.0
     */
    protected void adjustTypeCounts(int[] typeCounts){
        this.typeCounts.put('p', typeCounts[0]);
        this.typeCounts.put('l', typeCounts[1]);
        this.typeCounts.put('t', typeCounts[2]);
        this.typeCounts.put('a', typeCounts[3]);
        this.typeCounts.put('b', typeCounts[4]);
        this.typeCounts.put('c', typeCounts[5]);
    }

    /**
     * Method used to clear the container for reusing purposes.
     * @since 1.0
     */
    private void makeContainerEmpty(char[][][] container){
        for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int k = 0; k < Constants.CONTAINER_LENGTH; k++) {
                    container[i][j][k] = Constants.NOT_TRAVERSED_EMPTY_SYMBOL;
                }
            }
        }
    }

    /**
     * used to copy the current container and store it. This is done by storing the container in the variable
     * bestContainer.
     * @since 1.0
     */
    protected void saveContainer(){
        for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                System.arraycopy(this.container[i][j], 0,
                        this.bestContainer[i][j], 0, Constants.CONTAINER_LENGTH);
            }
        }
    }

    /**
     * method to retrieve the location of the first empty spot inside the container -cargo space-
     * @return         returns the 3D point of the first empty point in the container array.
     * @return         return null if there is no empty spot found.
     * @since 1.0
     */
    protected Point3D getFirstEmpty(char[][][] container){
        for(int k = 0;k<Constants.CONTAINER_LENGTH;k++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
                    if(container[i][j][k] == Constants.NOT_TRAVERSED_EMPTY_SYMBOL)
                        return new Point3D(i, j, k);
                }
            }
        }

        return null;
    }

    /**
     * Used to copy a container to another container, source -> destination.
     * @since 1.0
     */
    protected void copyFromTo(char[][][] srcContainer, char[][][] destContainer){
        for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                System.arraycopy(srcContainer[i][j], 0,
                        destContainer[i][j], 0, Constants.CONTAINER_LENGTH);
            }
        }
    }

    /**
     * Prints the container to the command prompt.
     * @since 1.0
     */
    protected void printContainer(char[][][] container) {
        System.out.println("----------------------------------------------------------------");
        for(int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int k = 0; k < Constants.CONTAINER_LENGTH; k++) {
                    System.out.print(this.container[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println("\n");
        }
        System.out.println("----------------------------------------------------------------");
    }

    /**
     * checks if the container is full, if so it returns boolean value true. If not the method returns
     * boolean value false.
     * @return                  true
     * @return                  false
     * @since 1.0
     */
    private boolean checkIfFull(){
        for(int k=0; k < Constants.CONTAINER_LENGTH; k++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
                    if(this.container[i][j][k] == Constants.NOT_TRAVERSED_EMPTY_SYMBOL)
                        return false;
                }
            }
        }

        return true;
    }

    /**
     * used to save the global count of the parcel types.
     * @since 1.0
     */
    protected void saveGlobalTypeCounts() {
        this.globalTypeCounts[0] = this.typeCounts.get('p');
        this.globalTypeCounts[1] = this.typeCounts.get('l');
        this.globalTypeCounts[2] = this.typeCounts.get('t');
        this.globalTypeCounts[3] = this.typeCounts.get('a');
        this.globalTypeCounts[4] = this.typeCounts.get('b');
        this.globalTypeCounts[5] = this.typeCounts.get('c');
    }

    /**
     * This method is used to check if a parcel is 'valid' in other words able to be placed on a certain
     * coordinate.
     * @param i                                 the X dimension of the parcel incremented with
     *                                           the X coordinate of the position.
     * @param j                                 the Y dimension of the parcel incremented with
     *                                           the Y coordinate of the position.
     * @param k                                 the Z dimension of the parcel incremented with
     *                                           the Z coordinate of the position
     * @return                      true, if valid. / false, if invalid.
     * @since 1.0
     */
    protected boolean isValidForPosition(int i, int j, int k, Parcel variationParcel) {
        for(ParcelComponent component : variationParcel.getComponents()){
            int nX = component.getX() + i;
            int nY = component.getY() + j;
            int nZ = component.getZ() + k;

            if(nX < 0 || nX >= Constants.CONTAINER_WIDTH) return false;
            if(nY < 0 || nY >= Constants.CONTAINER_HEIGHT) return false;
            if(nZ < 0 || nZ >= Constants.CONTAINER_LENGTH) return false;
            if(this.container[nX][nY][nZ] != Constants.NOT_TRAVERSED_EMPTY_SYMBOL) return false;
        }

        return true;
    }

    /**
     * The method tryToPlaceParcel tries to fit the respective parcel into the container and if possible returns
     * the coordinate that can accommodate the parcel given.
     */
    protected int[] tryToPlaceParcel(Parcel parcel) {

        for(int k=0;k<Constants.CONTAINER_LENGTH;k++){
            for(int j=0;j<Constants.CONTAINER_HEIGHT;j++){
                for(int i=0;i<Constants.CONTAINER_WIDTH;i++){

                    List<ParcelComponent> components = parcel.getComponents();

                    boolean canBePlaced = true;
                    for(ParcelComponent component : components){
                        int newX = component.getX() + i;
                        int newY = component.getY() + j;
                        int newZ = component.getZ() + k;

                        if(newX < 0 || newX >= Constants.CONTAINER_WIDTH) canBePlaced = false;
                        if(newY < 0 || newY >= Constants.CONTAINER_HEIGHT) canBePlaced = false;
                        if(newZ < 0 || newZ >= Constants.CONTAINER_LENGTH) canBePlaced = false;
                        if(canBePlaced && this.container[newX][newY][newZ] != Constants.NOT_TRAVERSED_EMPTY_SYMBOL)
                            canBePlaced = false;
                    }

                    if(!canBePlaced) continue;

                    return new int[]{1, i, j, k};
                }
            }
        }

        return new int[]{0};
    }

    /**
     * Places the parcel on the given coordinates.
     * @since 1.0
     */
    protected void placeParcel(int i, int j, int k, Parcel parcel){
        for(ParcelComponent component : parcel.getComponents()){
            int newX = component.getX() + i;
            int newY = component.getY() + j;
            int newZ = component.getZ() + k;

            this.container[newX][newY][newZ] = parcel.getType();
            component.setX(newX);
            component.setY(newY);
            component.setZ(newZ);
        }
    }

    /**
     * works the same as the placeParcel() method but the coordinates of the component dimensions are reverted.
     * @since 1.0
     */
    protected void revertPlaceParcel(int i, int j, int k, Parcel parcel){
        for(ParcelComponent component : parcel.getComponents()){
            int newX = component.getX();
            int newY = component.getY();
            int newZ = component.getZ();

            this.container[newX][newY][newZ] = Constants.NOT_TRAVERSED_EMPTY_SYMBOL;
            component.setX(newX - i);
            component.setY(newY - j);
            component.setZ(newZ - k);
        }
    }

    /**
     * copies and therefore saves the solutions inside the array list solutionParcels.
     * @since 1.0
     */
    protected void saveSolutions(List<Parcel> solution) {
        this.solutionParcels = new ArrayList<>();
        for(Parcel parcel : solution) this.solutionParcels.add(new Parcel(parcel));
    }

    /**
     * An abstract method for all the children that solves the problem.
     * @return the solution
     */
    public abstract T solve();

    /**
     * getter for the maximum score variable: maxScore
     * @since 1.0
     */
    public int getMaxScore() {
        return this.maxScore;
    }

    /**
     * A method that optimizes the cargo space arrangement - finds 2 B and replases them with 3 A => +1 point
     * @param solutionParcels
     * @return additional points
     */
    protected int optimizeCargoSpace(List<Parcel> solutionParcels) {
        int additionalScore = 0;
        for(int i=0; i < solutionParcels.size(); i++){
            Parcel parcel = solutionParcels.get(i);
            if(parcel.getType() != 'b') continue;

            List<ParcelComponent> components = parcel.getComponents();

            int x = components.get(0).getX();
            int y = components.get(0).getY();
            int z = components.get(0).getZ();

            for(int j=0; j < solutionParcels.size(); j++){
                if(i == j) continue;

                Parcel other = solutionParcels.get(j);
                List<ParcelComponent> comp = other.getComponents();

                int ox = comp.get(0).getX();
                int oy = comp.get(0).getY();
                int oz = comp.get(0).getZ();

                if(ox == x && oy == y && oz == z + 3){
                    Parcel a1 = ParcelFactory.getParcelByIdAndValue(3, Constants.PARCEL_A_INITIAL_SCORE)
                            .getAllVariations().get(0);
                    Parcel a2 = ParcelFactory.getParcelByIdAndValue(3, Constants.PARCEL_A_INITIAL_SCORE)
                            .getAllVariations().get(0);
                    Parcel a3 = ParcelFactory.getParcelByIdAndValue(3, Constants.PARCEL_A_INITIAL_SCORE)
                            .getAllVariations().get(0);

                    this.placeParcel(x, y, z, a1);
                    this.placeParcel(x, y, z + 2, a2);
                    this.placeParcel(x, y, z + 4, a3);

                    additionalScore++;

                    solutionParcels.add(a1);
                    solutionParcels.add(a2);
                    solutionParcels.add(a3);

                    solutionParcels.removeIf(e -> e.getId() == other.getId() || e.getId() == parcel.getId());
                }
            }
        }

        return additionalScore;
    }
}
