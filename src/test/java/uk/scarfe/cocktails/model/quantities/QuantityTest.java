package uk.scarfe.cocktails.model.quantities;

import org.junit.jupiter.api.Test;
import uk.scarfe.cocktails.model.quantities.Liquid;
import uk.scarfe.cocktails.model.quantities.Mass;
import uk.scarfe.cocktails.model.quantities.Quantity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A unit test suite to verify that we can convert between different units of measurement, and
 * that two quantities can be equal despite different units.
 *
 * @author Daniel Scarfe
 */
class QuantityTest {

    @Test
    void convertMass() {
        final Quantity<Mass> grammes = Quantity.of(1000, Mass.GRAMMES);
        final Quantity<Mass> kilogrammes = Quantity.convert(grammes, Mass.KILOGRAMMES);
        assertEquals(Mass.KILOGRAMMES, kilogrammes.getUnit());
        assertEquals(1, kilogrammes.getAmount());
        assertEquals(grammes, kilogrammes);
    }

    @Test
    void convertLiquid() {
        final Quantity<Liquid> millilitres = Quantity.of(1000, Liquid.MILLILITRES);
        final Quantity<Liquid> centilitres = Quantity.convert(millilitres, Liquid.CENTILITRES);
        final Quantity<Liquid> litres = Quantity.convert(millilitres, Liquid.LITRES);
        assertEquals(Liquid.LITRES, litres.getUnit());
        assertEquals(1, litres.getAmount());
        assertEquals(Liquid.CENTILITRES, centilitres.getUnit());
        assertEquals(100, centilitres.getAmount());
        assertEquals(millilitres, centilitres);
        assertEquals(centilitres, litres);
    }

}