package ix.lab05;

import static org.junit.Assert.assertEquals;
import ix.lab05.processing.RMSE;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RMSETest {

    public static final Map<Integer, Double> GROUND_TRUTH = new HashMap<Integer, Double>();
    public static final Map<Integer, Double> PREDICTED = new HashMap<Integer, Double>();
    public static final double TRUE_RMSE = 2.1181753;

    static {
        GROUND_TRUTH.put(1, 2.5);
        GROUND_TRUTH.put(2, 2.0);
        GROUND_TRUTH.put(3, 3.0);

        PREDICTED.put(1, 0.6);
        PREDICTED.put(2, 4.7);
        PREDICTED.put(3, 1.4);
        PREDICTED.put(4, 1.0);
    }

    @Test
    public void testRMSE() {
        double error = RMSE.evaluate(GROUND_TRUTH, PREDICTED);
        assertEquals(TRUE_RMSE, error, 0.0001);
    }
}
