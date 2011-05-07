package org.adumont.mymockitolab;

//Let's import Mockito statically so that the code looks clearer
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MyMockitoLab {

	@Before
	public void setup() {

	}

	@SuppressWarnings("unchecked")
	@Test
	public void letsVerifySomeBehaviour() {

		// mock creation
		List<String> mockedList = mock(List.class);

		// using mock object
		mockedList.add("one");
		mockedList.clear();

		// checking that we did call the methods
		verify(mockedList).add("one");
		verify(mockedList).clear();
	}

	@Test(expected = RuntimeException.class)
	public void howAboutSomeStubbing() {

		// You can mock concrete classes, not only interfaces
		LinkedList<String> mockedList = mock(LinkedList.class);

		// stubbing
		given(mockedList.get(0)).willReturn("first");
		given(mockedList.get(1)).willThrow(new RuntimeException());

		// following prints "first"
		System.out.println(mockedList.get(0));

		// following prints "null" because get(999) was not stubbed
		System.out.println(mockedList.get(999));

		// following throws runtime exception
		System.out.println(mockedList.get(1));

		// Although it is possible to verify a stubbed invocation, usually it's
		// just redundant
		// If your code cares what get(0) returns then something else breaks
		// (often before even verify() gets executed).
		// If your code doesn't care what get(0) returns then it should not be
		// stubbed. Not convinced? See here.

		// never reached because of the runtimeException
		verify(mockedList).get(0);
	}

}
