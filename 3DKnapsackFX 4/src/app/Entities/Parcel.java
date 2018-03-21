package app.Entities;

import app.Utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Parcel {

    /**
     * Initializes the parcels and uses methods to get all the variations of the initialised parcels.
     * After getting the variations these are inserted into an array list from which it is easier to be implemented into
     * our algorithms.
     * @param id                        character containing the identification char of the parcel
     * @param currentId                 gets initialized with 0 to be incremented giving everything a proper id
     * @param components                array list of the parcel components
     * @param x                         dimension of parcel
     * @param y                         dimension of parcel
     * @param z                         dimension of parcel
     * @param value                     assigned value to the parcel
     * @param type                      type of the parcel -closely related to the ID-
     * @param variationPattern          a variable of type string containing the variation patterns of a parcel type
     * @param used                      gives true if a parcel is used; false if not used
     * @param allVariations             an array list containing all variations of the parcels
     * @since 1.0
     */
    private int id;
    private static int currentId = Constants.INITIAL_PARCEL_ID;
    private List<ParcelComponent> components;
    private int x;
    private int y;
    private int z;
    private int value;
    private char type;
    private String variationPattern;
    private boolean used;
    private Map<Character, List<Parcel>> allVariations;

    /**
     * constructor for the parcel class.
     * @since 1.0
     */
    public Parcel(int x, int y, int z, int value, char type, String variationPattern) {
        this.components = new ArrayList<>();
        this.allVariations = new HashMap<>();

        this.setX(x);
        this.setY(y);
        this.setZ(z);

        this.setValue(value);
        this.setType(type);
        this.setVariationPattern(variationPattern);
        this.setUsed(false);
        this.setId(currentId++);
    }

    /**
     * constructor used for copying -copy constructor- the parcel class.
     * @since 1.0
     */
    public Parcel(Parcel other){
        this.components = new ArrayList<>();
        this.allVariations = new HashMap<>();
        for(ParcelComponent parcelComponent : other.components){
            this.components.add(new ParcelComponent(parcelComponent));
        }

        this.setX(other.x);
        this.setY(other.y);
        this.setZ(other.z);

        this.setValue(other.value);
        this.setVariationPattern(other.variationPattern);
        this.setType(other.type);
        this.setUsed(other.used);
        this.setId(other.id);
    }

    /**
     * getters and setters for the variables declared in the Parcel class.
     * @since 1.0
     */
    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public boolean isUsed() {
        return this.used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getVariationPattern() {
        return this.variationPattern;
    }

    private void setVariationPattern(String variationPattern) {
        this.variationPattern = variationPattern;
    }

    public char getType() {
        return this.type;
    }

    private void setType(char type) {
        this.type = type;
    }

    public int getValue() {
        return this.value;
    }

    private void setValue(int value) {
        this.value = value;
    }

    public int getX() {
        return this.x;
    }

    private void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    private void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    private void setZ(int z) {
        this.z = z;
    }

    /**
     * the add method is used to add another parcel compo component to the array list ParcelComponent.
     * @param component                    array list for all the components
     * @return                              component list with the added components.
     * @since 1.0
     */
    public Parcel add(ParcelComponent component){
        this.components.add(new ParcelComponent(component));
        return this;
    }

    /**
     * Getter for all the components.
     * @return components
     */
    public List<ParcelComponent> getComponents() {
        return this.components;
    }

    /**
     * Methods for optimal rotation/flipping of the parcels L,P,T/A,B,C
     */
    private void rotateX(){
        for(ParcelComponent component : this.components){
            int temp = component.getZ();
            component.setZ(component.getY());
            component.setY(-temp);
        }
    }

    private void rotateY(){
        for(ParcelComponent component : this.components){
            int temp = component.getZ();
            component.setZ(component.getX());
            component.setX(-temp);
        }
    }

    private void rotateZ(){
        for(ParcelComponent component : this.components){
            int temp = component.getX();
            component.setX(component.getY());
            component.setY(-temp);
        }
    }

    private  void flip(){
        for(ParcelComponent component : this.components){
            int temp = component.getX();
            component.setX(-temp);
        }
    }

    private void rotateA(){
        for(ParcelComponent component : this.components){
            int temp = component.getY();
            component.setY(component.getZ());
            component.setZ(temp);
        }
    }

    private void rotateB(){
        for(ParcelComponent component : this.components){
            int temp = component.getX();
            component.setX(component.getZ());
            component.setZ(temp);
        }
    }

    private void rotateC(){
        for(ParcelComponent component : this.components){
            int temp = component.getX();
            component.setX(component.getY());
            component.setY(temp);
        }
    }

    /**
     * makes the array list for all the possible variations the parcels can have.
     * this is done by assigning characters to the rotation methods, which connect to the variation pattern
     * given to the parcels in the ParcelFactory class.
     *
     * e.g. the variationPattern for the type 'l'parcel is yzxyzxabcyzxacyzxyzxabcyzxzxzc.
     *      therefore it will first rotateY(), then rotateZ() etc.
     *
     * @see   #rotateX()
     * @see   #rotateY()
     * @see   #rotateZ()
     * @see   #rotateA()
     * @see   #rotateB()
     * @see   #rotateC()
     * @see   #flip()
     * @return  returns the full array list of the variation patterns of the desired type
     * @since 1.0
     */
    public List<Parcel> getAllVariations(){
        this.allVariations.put(this.type, new ArrayList<>());

        for(String step : this.variationPattern.split("")){
            boolean flipping = false;

            switch (step){
                case "x": this.rotateX(); break;
                case "y": this.rotateY(); break;
                case "z": this.rotateZ(); break;
                case "a": this.rotateA(); break;
                case "b": this.rotateB(); break;
                case "c": this.rotateC(); break;
                case "f": this.flip(); flipping = true; break;
            }

            if(!flipping) this.allVariations.get(this.type).add(new Parcel(this));
        }

        return this.allVariations.get(this.type);
    }
}
