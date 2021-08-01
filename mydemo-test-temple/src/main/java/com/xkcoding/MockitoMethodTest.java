package com.xkcoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author xugangq
 * @date 2021/6/9 10:16
 */
@MockitoSettings(strictness = Strictness.WARN)
public class MockitoMethodTest {
    @Captor
    private ArgumentCaptor<List<String>> captor;

    //实例化虚拟对象
    @Test
    @DisplayName("实例化虚拟对象")
    public void mockitoVirtualObject() {
        // You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);

        // Stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());
        //调用真实方法
        when(mockedList.contains("first")).thenCallRealMethod();

        // Following prints "first"
        System.out.println(mockedList.get(0));
        // Following throws runtime exception
        // System.out.println(mockedList.get(1));
        // Following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        System.out.println(mockedList.contains("first"));
        // Although it is possible to verify a stubbed invocation, usually it's just redundant
        // If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        // If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mockedList).get(0);
    }

    //参数匹配
    @Test
    @DisplayName("参数匹配")
    public void argsMatch() {
        // You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);
        // Stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");
        // Stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
        when(mockedList.contains(argThat((String ele) -> ele.startsWith("s")))).thenReturn(false);
        // Following prints "element"
        System.out.println(mockedList.get(999));
        System.out.println(mockedList.contains("start"));
        // You can also verify using an argument matcher
        verify(mockedList).get(anyInt());
        // Argument matchers can also be written as Java 8 Lambdas
        verify(mockedList).add(argThat((String str) -> str.length() > 5));
    }

    //校验次数
    @Test
    @DisplayName("校验次数")
    public void checkInvokeCount() {
        LinkedList<String> mockedList = mock(LinkedList.class);
        // Use mock
        mockedList.add("once");
        mockedList.add("twice");
        mockedList.add("twice");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        // Follow two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        // Exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        // Verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        // Verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(5)).add("three times");
    }

    //校验顺序
    //有时对于一些行为，有先后顺序之分，所以，当我们在校验时，就需要考虑这个行为的先后顺序
    @Test
    @DisplayName("校验顺序")
    public void checkOrder() {
        // A. Single mock whose methods must be invoked in a particular order
        List<String> singleMock = mock(List.class);
        // Use a single mock
        singleMock.add("was added first");
        singleMock.add("was added second");
        // Create an inOrder verifier for a single mock
        InOrder inOrder = inOrder(singleMock);
        // Following will make sure that add is first called with "was added first, then with "was added second"
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        // B. Multiple mocks that must be used in a particular order
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);
        // Use mocks
        firstMock.add("was called first");
        secondMock.add("was called second");
        // Create inOrder object passing any mocks that need to be verified in order
        inOrder = inOrder(firstMock, secondMock);
        // Following will make sure that firstMock was called before secondMock
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");
    }

    //测试异常抛出
    @Test
    @DisplayName("测试异常抛出")
    public void exceptionTest() {
        List<String> data = mock(List.class);

        // stubbing, 调用方法抛出异常
        when(data.get(0)).thenThrow(NullPointerException.class);
        doThrow(NullPointerException.class).when(data).get(1);
        given(data.get(2)).willThrow(new IndexOutOfBoundsException("越界"));

        assertThatThrownBy(() -> data.get(0)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> data.get(1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> data.get(2)).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("越界");
    }

    //监视真实对象
    //前面使用的都是 mock 出来一个对象。这样，当 没有配置/存根 其具体行为的话，结果就会返回 空类型。
    // 而如果使用 特务对象（spy），那么对于 没有存根 的行为，它会调用 原来对象 的方法。可以把 spy 想象成局部 mock。
    @Test
    @DisplayName("监视真实对象1")
    public void spyRealObject1() {
        List list = new LinkedList();
        List spy = spy(list);

        // Optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);
        // Use the spy calls *real* methods
        spy.add("one");
        spy.add("two");

        // Prints "one" - the first element of a list
        System.out.println(spy.get(0));
        // Size() method was stubbed - 100 is printed
        System.out.println(spy.size());
        // Optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");
    }

    //由于 spy 是局部 mock，所以有时候使用 when(Object) 时，无法做到存根作用。此时，就可以考虑使用 doReturn() | Answer() | Throw() 这类方法进行存根
    @Test
    @DisplayName("监视真实对象2")
    public void spyRealObject2() {
        List list = new LinkedList();
        List spy = spy(list);
        // Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
        //when(spy.get(0)).thenReturn("foo");
        // You have to use doReturn() for stubbing
        //when(spy.get(0)).thenReturn("ss");
        doReturn("foo").when(spy).get(0);

        Object o = spy.get(0);
        System.out.println(o);
    }

    //参数捕获argsCapture
    @Test
    @DisplayName("参数捕获argsCapture")
    public void argsCapture() {
        List<String> asList = Arrays.asList("someElement_test", "someElement");
        final List<String> mockedList = mock(List.class);
        mockedList.addAll(asList);

        verify(mockedList).addAll(captor.capture()); // When verify,you can capture the arguments of the calling method
        final List<String> capturedArgument = captor.getValue();
        assertThat(capturedArgument).contains("someElement");
    }

    //answer
    @Test
    @DisplayName("answer")
    public void answerTest() {
        List<String> singleMock = mock(List.class);
        when(singleMock.get(anyInt())).thenAnswer((Answer)invocation -> {
            Object[] args = invocation.getArguments();
            Object mock = invocation.getMock();
            return "called with arguments: " + Arrays.toString(args);
        });

        //Following prints "called with arguments: [foo]"
        System.out.println(singleMock.get(1));
    }
}
