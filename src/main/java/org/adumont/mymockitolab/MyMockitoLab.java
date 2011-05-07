package org.adumont.mymockitolab;

//Let's import Mockito statically so that the code looks clearer
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

		// verification
		verify(mockedList).add("one");
		verify(mockedList).clear();
	}

}
