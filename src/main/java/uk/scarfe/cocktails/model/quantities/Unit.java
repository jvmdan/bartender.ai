package uk.scarfe.cocktails.model.quantities;

public interface Unit {

    Unit getDefaultUnit();

    double getConversionFactor();

}
