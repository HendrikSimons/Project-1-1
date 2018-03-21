package app.Entities;

public class ParcelComponent {

    /**
     * Class used to be able to alter the individual parcel components.
     * @param x     dimension of the parcel component
     * @param y     dimension of the parcel component
     * @param z     dimension of the parcel component
     * @since 1.0
     */
    private int x;
    private int y;
    private int z;

    /**
     * Constructor for the ParcelComponent class.
     * @since 1.0
     */
    public ParcelComponent(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    /**
     * Copy constructor for the parcel component class.
     * @since 1.0
     */
    public ParcelComponent(ParcelComponent other){
        this.setX(other.x);
        this.setY(other.y);
        this.setZ(other.z);
    }

    /**
     * getters and setters for the ParcelComponent class
     * @since 1.0
     */
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
