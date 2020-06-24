package recipeApp;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Value {

    @Element
    private double numberValue;

    @Attribute
    private units unit;

    public Value() {

    }

    public enum units {
        TSP,
        TBSP,
        FL_OZ,
        CUP,
        PINT,
        QUART,
        GALLON,
        LB,
        OZ,
        G,
        KG,
        NUMBER
    }

    public double getNumberValue() {
        return numberValue;
    }

    public Value(double numberValue, units unit) {
        this.numberValue = numberValue;
        this.unit = unit;
    }

    public String getUnit() {
        return unit.toString();
    }

    public static Value.units unitConverter(String unitValue) {
        Value.units unitVar;

        switch (unitValue) {
            case "per TSP":
                unitVar = Value.units.TSP;
                break;
            case "per TBSP":
                unitVar = Value.units.TBSP;
                break;
            case "per FL OZ":
                unitVar = Value.units.FL_OZ;
                break;
            case "per NUMBER":
                unitVar = Value.units.NUMBER;
                break;
            case "per CUP":
                unitVar = Value.units.CUP;
                break;
            case "per PINT":
                unitVar = Value.units.PINT;
                break;
            case "per QUART":
                unitVar = Value.units.QUART;
                break;
            case "per GALLON":
                unitVar = Value.units.GALLON;
                break;
            case "per LB":
                unitVar = Value.units.LB;
                break;
            case "per OZ":
                unitVar = Value.units.OZ;
                break;
            case "per G":
                unitVar = Value.units.G;
                break;
            default:
                unitVar = Value.units.KG;
                break;
        }
        return unitVar;
    }
}
