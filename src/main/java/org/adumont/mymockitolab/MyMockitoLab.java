package org.adumont.mymockitolab;

//Let's import Mockito statically so that the code looks clearer
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MyMockitoLab {

	@SuppressWarnings("rawtypes")
	@Mock
	private List mockedList;

	@Before
	public void setup() {
		initMocks(this);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void letsVerifySomeBehaviour() {
		// using mock object
		mockedList.add("one");
		mockedList.clear();

		// checking that we did call the methods
		verify(mockedList).add("one");
		verify(mockedList).clear();
	}

	@Test(expected = RuntimeException.class)
	public void howAboutSomeStubbing() {
		// stubbing
		given(mockedList.get(0)).willReturn("first");
		given(mockedList.get(1)).willThrow(new RuntimeException());

		// following prints "first"
		System.out.println(mockedList.get(0));

		// following prints "null" because get(999) was not stubbed
		System.out.println(mockedList.get(999));

		// following throws runtime exception
		System.out.println(mockedList.get(1) + "\n");

		// Although it is possible to verify a stubbed invocation, usually it's
		// just redundant
		// If your code cares what get(0) returns then something else breaks
		// (often before even verify() gets executed).
		// If your code doesn't care what get(0) returns then it should not be
		// stubbed. Not convinced? See here.

		// never reached because of the runtimeException
		verify(mockedList).get(0);

	}

	@Test
	public void matchersArgument() {
		// stubbing using built-in anyInt() argument matcher
		given(mockedList.get(anyInt())).willReturn("element");

		// // stubbing using hamcrest (let's say isValid() returns your own
		// // hamcrest matcher):
		// given(mockedList.contains(argThat(new IsValidMatcherTest())))
		// .willReturn("element");

		// following prints "element"
		System.out.println(mockedList.get(999));

		// you can also verify using an argument matcher
		verify(mockedList).get(anyInt());
	}

	// /**
	// * Private
	// */
	// @SuppressWarnings({ "unused", "rawtypes" })
	// private class IsValidMatcherTest extends ArgumentMatcher<List> {
	// @Override
	// public boolean matches(Object list) {
	// return true;
	// }
	// }

}
