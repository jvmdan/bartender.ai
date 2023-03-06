package uk.scarfe.cocktails.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import uk.scarfe.cocktails.model.units.Liquid;
import uk.scarfe.cocktails.model.units.Mass;
import uk.scarfe.cocktails.model.units.Unit;

@Getter
@EqualsAndHashCode
public class Quantity<U extends Unit> {

    private final double amount;
    private final U unit;

    // Private constructor to prevent instantiation outside of static factory methods.
    private Quantity(double amount, U unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public static Quantity<Liquid> of(double amount, Liquid unit) {
        return new Quantity<>(amount, unit);
    }

    public static Quantity<Mass> of(double amount, Mass unit) {
        return new Quantity<>(amount, unit);
    }

    public static <T extends Unit> Quantity<T> convert(Quantity<T> from, T to) {
        // Convert a given quantity to a different set of units of the same type (mass / liquid / etc).
        final Unit oldUnit = from.getUnit();
        final double conversionFactor = 1;
        final double newAmount = from.getAmount() * conversionFactor;
        return new Quantity<>(newAmount, to);
    }


}
