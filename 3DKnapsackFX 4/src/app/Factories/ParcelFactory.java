package app.Factories;

import app.Entities.Parcel;
import app.Entities.ParcelComponent;
import app.Utilities.Constants;

/**
 * The class ParcelFactory constructs the components of the desired parcel types:
 * l;  * p;  * t;  * a;  * b;  * c;
 * @since 1.0
 */
public class ParcelFactory {

    /**
     * The method getParcelByIdAndValue constructs the parcels and uses a switch case to jump between the desired
     * parcels. The switch case switches by using its ID as a referencing point; also to catch ID's that are not
     * declared in our switch case we use a default case to prevent complications.
     * @param id                        gives the combined parcel components a type identification
     * @param value                     Connects the parcel with its inserted value
     * @return new Parcel(...)                        null if invalid type is inserted
     * @since 1.0
     */
    public static Parcel getParcelByIdAndValue(int id, int value){
        switch (id){
            case 0: return new Parcel(0, 0, 0, value, 'l', "xyzxyzxyzxyzfxyzxyzxyzxyz")
                    .add(new ParcelComponent(0, 0, 0))
                    .add(new ParcelComponent(0, 0, 1))
                    .add(new ParcelComponent(0, 0, 2))
                    .add(new ParcelComponent(0, 0, 3))
                    .add(new ParcelComponent(1, 0, 0));
            case 1: return new Parcel(0, 0, 0, value, 'p', "xyzxyzxyzxyzfxyzxyzxyzxyz")
                    .add(new ParcelComponent(0, 0, 1))
                    .add(new ParcelComponent(0, 0, 0))
                    .add(new ParcelComponent(0, 0, 2))
                    .add(new ParcelComponent(1, 0, 0))
                    .add(new ParcelComponent(1, 0, 1));
            case 2: return new Parcel(0, 0, 0, value, 't', "xyzxyzxyzxyz")
                    .add(new ParcelComponent(0, 0, 0))
                    .add(new ParcelComponent(1, 0, 1))
                    .add(new ParcelComponent(1, 0, 2))
                    .add(new ParcelComponent(1, 0, 0))
                    .add(new ParcelComponent(2, 0, 0));
            case 3: return new Parcel(0, 0, 0, value, 'a', "abc")
                    .add(new ParcelComponent(0, 0, 0))
                    .add(new ParcelComponent(0, 0, 3))
                    .add(new ParcelComponent(0, 0, 1))
                    .add(new ParcelComponent(0, 0, 2))
                    .add(new ParcelComponent(0, 1, 3))
                    .add(new ParcelComponent(0, 1, 0))
                    .add(new ParcelComponent(0, 1, 1))
                    .add(new ParcelComponent(0, 1, 2))
                    .add(new ParcelComponent(1, 0, 3))
                    .add(new ParcelComponent(1, 0, 0))
                    .add(new ParcelComponent(1, 0, 1))
                    .add(new ParcelComponent(1, 0, 2))
                    .add(new ParcelComponent(1, 1, 3))
                    .add(new ParcelComponent(1, 1, 0))
                    .add(new ParcelComponent(1, 1, 1))
                    .add(new ParcelComponent(1, 1, 2));
            case 4: return new Parcel(0, 0, 0, value, 'b', "abcabc")
                    .add(new ParcelComponent(0, 0, 0))
                    .add(new ParcelComponent(0, 1, 3))
                    .add(new ParcelComponent(0, 1, 0))
                    .add(new ParcelComponent(0, 1, 1))
                    .add(new ParcelComponent(0, 1, 2)) // 4
                    .add(new ParcelComponent(0, 0, 3))
                    .add(new ParcelComponent(0, 0, 1))
                    .add(new ParcelComponent(0, 0, 2)) // 8
                    .add(new ParcelComponent(0, 2, 3))
                    .add(new ParcelComponent(0, 2, 0))
                    .add(new ParcelComponent(0, 2, 1))
                    .add(new ParcelComponent(0, 2, 2)) // 12
                    .add(new ParcelComponent(1, 1, 3))
                    .add(new ParcelComponent(1, 1, 0))
                    .add(new ParcelComponent(1, 1, 1))
                    .add(new ParcelComponent(1, 1, 2)) // 16
                    .add(new ParcelComponent(1, 0, 3))
                    .add(new ParcelComponent(1, 0, 0))
                    .add(new ParcelComponent(1, 0, 1))
                    .add(new ParcelComponent(1, 0, 2)) // 20
                    .add(new ParcelComponent(1, 2, 3))
                    .add(new ParcelComponent(1, 2, 0))
                    .add(new ParcelComponent(1, 2, 1))
                    .add(new ParcelComponent(1, 2, 2)); // 24
            case 5: return new Parcel(0, 0, 0, value, 'c', "a")
                    .add(new ParcelComponent(1, 1, 2))
                    .add(new ParcelComponent(1, 1, 0))
                    .add(new ParcelComponent(1, 1, 1)) // 3
                    .add(new ParcelComponent(1, 0, 2))
                    .add(new ParcelComponent(1, 0, 0))
                    .add(new ParcelComponent(1, 0, 1)) // 6
                    .add(new ParcelComponent(1, 2, 2))
                    .add(new ParcelComponent(1, 2, 0))
                    .add(new ParcelComponent(1, 2, 1)) // 9
                    .add(new ParcelComponent(0, 1, 2))
                    .add(new ParcelComponent(0, 1, 0))
                    .add(new ParcelComponent(0, 1, 1)) // 12
                    .add(new ParcelComponent(0, 0, 2))
                    .add(new ParcelComponent(0, 0, 0))
                    .add(new ParcelComponent(0, 0, 1)) // 15
                    .add(new ParcelComponent(0, 2, 2))
                    .add(new ParcelComponent(0, 2, 0))
                    .add(new ParcelComponent(0, 2, 1)) // 18
                    .add(new ParcelComponent(2, 1, 2))
                    .add(new ParcelComponent(2, 1, 0))
                    .add(new ParcelComponent(2, 1, 1)) // 21
                    .add(new ParcelComponent(2, 0, 2))
                    .add(new ParcelComponent(2, 0, 0))
                    .add(new ParcelComponent(2, 0, 1)) // 24
                    .add(new ParcelComponent(2, 2, 2))
                    .add(new ParcelComponent(2, 2, 0))
                    .add(new ParcelComponent(2, 2, 1)); // 27
            default:
                System.out.println(Constants.NO_APPROPRIATE_PARCEL_MSG);
                return null;
        }
    }
}
