package app.Factories;


import app.Utilities.Constants;
import javafx.scene.paint.Color;


public class ColorFactory {

    /**
     * The class ColorFactory assigns the parcels constructed in the ParcelFactory with a distinguishable colour, for
     * visualization purposes. The colours are chosen to be highly distinctive so that during the representation it
     * is easy to see out of which parcel types the full cargo space consists.
     * @param type                              type has the same function as the id variable in #ParcelFactory
     * @return                                  Returns the colour code for the respective parcels. If no valid type
     *                                          found the program uses the colour white for the unknown cargo.
     * @since 1.0
     */
    public static Color getColorByParcelType(char type){
        switch (type){
            case 'a': return Color.valueOf("#099FFF");
            case 'b': return Color.valueOf("#FF6600");
            case 'c': return Color.valueOf("#F2EA02");
            case 'l': return Color.valueOf("#55ffe1");
            case 'p': return Color.valueOf("#a6fd29");
            case 't': return Color.valueOf("#ff3b94");
            default:
                System.out.println(Constants.NO_APPROPRIATE_COLOR_MSG);
                return Color.WHITE;
        }
    }
}
