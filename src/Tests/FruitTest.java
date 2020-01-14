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
        fruit f = new fruit(0, new Point3D(4, 2, 0), 5);
        fruit f2 = new fruit(0, 4, 2, 0, 4);
        if (f.toString().equals(f2.toString())) {
            fail("Constractor problem");
        }
        f2.setValue(5);
        if (!f.toString().equals(f2.toString())) {
            fail("Setweight problem");
        }
        f2.setID(1);
        if (f.toString().equals(f2.toString())) {
            fail("Setid problem");
        }

    }
}
