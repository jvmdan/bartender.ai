package uk.scarfe.cocktails.model;

import org.junit.jupiter.api.Test;
import uk.scarfe.cocktails.model.units.Liquid;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void convertMass() {

    }

    @Test
    void convertLiquid() {
        final Quantity<Liquid> millilitres = Quantity.of(1000, Liquid.MILLILITRES);
        final Quantity<Liquid> litres = Quantity.convert(millilitres, Liquid.LITRES);
        assertEquals(Liquid.LITRES, litres.getUnit());
        assertEquals(1, litres.getAmount());
    }

}