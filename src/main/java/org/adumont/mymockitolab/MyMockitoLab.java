package org.adumont.mymockitolab;

//Let's import Mockito statically so that the code looks clearer
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MyMockitoLab {

	@SuppressWarnings("rawtypes")
	@Mock
	private List mockedList;

	@SuppressWarnings("rawtypes")
	@Mock
	private List mockOne;

	@Mock
	private Object mockTwo;

	@Mock
	private Object mockThree;

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

	@SuppressWarnings("unchecked")
	@Test
	public void verifyNoInteractionWithAMock() {
		// using mocks - only mockOne is interacted
		mockOne.add("one");

		// ordinary verification
		verify(mockOne).add("one");

		// verify that method was never called on a mock
		verify(mockOne, never()).add("two");

		// verify that other mocks were not interacted
		verifyZeroInteractions(mockTwo, mockThree);
	}

	@Mock
	private Toto totoMock;

	private class Toto {
		public Object someMethod(Object... args) {
			// do things
			return null;
		}
	}

	@Test
	public void stubbingConsecutiveCallMethod_1() {
		given(totoMock.someMethod("some arg"))
				.willThrow(new RuntimeException()).willReturn("foo");

		// First call: throws runtime exception:
		try {
			totoMock.someMethod("some arg");
		} catch (Exception e) {
			assert true;
		}

		// Second call: prints "foo"
		System.out.println(totoMock.someMethod("some arg"));

		// Any consecutive call: prints "foo" as well (last stubbing wins).
		System.out.println(totoMock.someMethod("some arg") + "\n");
	}

	@Test
	public void stubbingConsecutiveCallMethod_2() {
		given(totoMock.someMethod("some arg"))
				.willReturn("one", "two", "three");

		// will print "one"
		System.out.println(totoMock.someMethod("some arg"));
		// will print "two"
		System.out.println(totoMock.someMethod("some arg"));
		// will print "three-"
		System.out.println(totoMock.someMethod("some arg"));
	}

	@Test
	public void stubbingWithCallbacks() {
		given(totoMock.someMethod(anyString())).willAnswer(
				new Answer<Object>() {
					@Override
					public Object answer(InvocationOnMock invocation) {
						Object[] args = invocation.getArguments();
						// Object mock = invocation.getMock();
						return "called with arguments: " + args;
					}
				});

		// Following prints "called with arguments: foo"
		System.out.println(totoMock.someMethod("foo"));
	}

	@Test(expected = RuntimeException.class)
	public void familyOfMethodsToStubVoidMethods() {
		willThrow(new RuntimeException()).given(mockedList).clear();

		// following throws RuntimeException:
		mockedList.clear();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void spyingOnRealObject() {
		List list = new LinkedList();
		List spy = Mockito.spy(list);

		// optionally, you can stub out some methods:
		given(spy.size()).willReturn(100);

		// using the spy calls real methods
		spy.add("one");
		spy.add("two");

		// prints "one" - the first element of a list
		System.out.println(spy.get(0));

		// size() method was stubbed - 100 is printed
		System.out.println(spy.size());

		// optionally, you can verify
		verify(spy).add("one");
		verify(spy).add("two");
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void warningOnSpyingMethodFinal() {
		List list = new LinkedList();
		List spy = Mockito.spy(list);

		// Impossible: real method is called so spy.get(0) throws
		// IndexOutOfBoundsException (the list is yet empty)
		try {
			given(spy.get(0)).willReturn("foo");
		} catch (Exception e) {
			// expected exception
		}

		// You have to use doReturn() for stubbing
		willReturn("foo").given(spy).get(0);
	}
}
