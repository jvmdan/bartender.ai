package ai.bartender.model.quantities;

public interface Unit {

    Unit getDefaultUnit();

    double getConversionFactor();

}
