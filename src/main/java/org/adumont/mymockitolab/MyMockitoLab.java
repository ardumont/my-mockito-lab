package org.adumont.mymockitolab;

//Let's import Mockito statically so that the code looks clearer
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

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

	@SuppressWarnings("unchecked")
	@Test
	public void verifyNumberOfInvocations() {
		// using mock
		mockedList.add("once");

		mockedList.add("twice");
		mockedList.add("twice");

		mockedList.add("three times");
		mockedList.add("three times");
		mockedList.add("three times");

		mockedList.add("five times");
		mockedList.add("five times");
		mockedList.add("five times");
		mockedList.add("five times");
		mockedList.add("five times");
		mockedList.add("five times");

		// following two verifications work exactly the same - times(1) is used
		// by default
		verify(mockedList).add("once");
		verify(mockedList, times(1)).add("once");

		// exact number of invocations verification
		verify(mockedList, times(2)).add("twice");
		verify(mockedList, times(3)).add("three times");

		// verification using never(). never() is an alias to times(0)
		verify(mockedList, times(0)).add("never happened");
		verify(mockedList, never()).add("never happened");

		// verification using atLeast()/atMost()
		verify(mockedList, atLeastOnce()).add("three times");
		verify(mockedList, atMost(5)).add("three times");

		verify(mockedList, atLeast(2)).add("five times");
		verify(mockedList, atMost(6)).add("five times");
	}

	@Test(expected = RuntimeException.class)
	public void stubbingVoidMethodsWithExceptions() {
		willThrow(new RuntimeException()).given(mockedList).clear();

		// following throws RuntimeException:
		mockedList.clear();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void verificationInOrder() {
		List firstMock = mock(List.class);
		List secondMock = mock(List.class);

		// using mocks
		firstMock.add("was called first");
		secondMock.add("was called second");

		// create inOrder object passing any mocks that need to be verified in
		// order
		InOrder inOrder = Mockito.inOrder(firstMock, secondMock);

		// following will make sure that firstMock was called before secondMock
		inOrder.verify(firstMock).add("was called first");
		inOrder.verify(secondMock).add("was called second");
	}
}
