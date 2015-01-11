package testing;

import calculator.NetzwerkCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetzwerkCalculatorTest {

	@Test
	public void testAddServer() throws Exception {

	}

	@Test
	public void testPi() throws Exception {
		NetzwerkCalculator a = new NetzwerkCalculator();
		Double erg = new Double(a.pi(4).doubleValue());
		Double wert= new Double (3.1416);
		assertEquals(wert, erg);
	}
}