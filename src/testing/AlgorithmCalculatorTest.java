package testing;

import calculator.AlgorithmCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlgorithmCalculatorTest {

	@Test
	public void testPi() throws Exception {
		AlgorithmCalculator a = new AlgorithmCalculator();
		Double erg = new Double(a.pi(4).doubleValue());
		Double wert= new Double (3.1416);
		assertEquals(wert, erg);
	}
}