package Tests;

import gameClient.fruit;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.fail;

class fruitTest {

    /**
     * Fruit tests.
     *
     * @author Bar Genish
     * @author Elyashiv Deri
     * @author lioz elmalem
     */
    @Test
    void NewFruit_and_to_stirngtest() {
        fruit f = new fruit(new Point3D(4, 2, 0), 5,1);
        fruit f2 = new fruit( 4, 2, 0, 4,1);
        if (f.toString().equals(f2.toString())) {
            fail("Constractor problem");
        }
        f2.set_value(5);
        if (!f.toString().equals(f2.toString())) {
            fail("Setweight problem");
        }
        f2.set_type(-1);
        if (f.toString().equals(f2.toString())) {
            fail("Setid problem");
        }

    }
}
