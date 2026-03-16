package de.seitz.licklib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LicklibApplicationTests {

	Calculator underTest = new Calculator();

	@Test
	void itShoudAddNumbers() {
		// given (arrange)
		int numberOne = 20;
		int numberTwo = 30;

		// when (act)
		int result = underTest.add(numberOne, numberTwo);

		// then (assert)
		int expectedResult = 51;
		assertThat(result).isEqualTo(expectedResult);
	}

	class Calculator {
		int add(int a, int b) {return a + b;}
	}

}
