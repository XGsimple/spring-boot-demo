package com.xkcoding.junit;

import com.xkcoding.junit.component.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author xugangq
 * @date 2021/6/9 10:16
 */
@MockitoSettings(strictness = Strictness.WARN)
public class JUnit5Test {
    @Captor
    private ArgumentCaptor<List<String>> captor;

    //注意 @BeforeAll 和 @AfterAll 注解只能修饰静态方法
    @BeforeAll
    public static void init() {
        System.out.println("初始化数据");
    }

    @AfterAll
    public static void cleanup() {
        System.out.println("清理数据");
    }

    @BeforeEach
    public void tearup() {
        System.out.println("当前测试方法开始");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("当前测试方法结束");
    }

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
        mockedList.add("123456");
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
        //使用when方式时，spy.get(0)报错，导致后续mock无法进行，有点类似于切面的after方法
        //when(spy.get(0)).thenReturn("foo");
        //使用doReturn方式时，spy.get(0)正常，后续mock正常
        //doReturn("foo").when(spy).get(0);
        //或者Answer
        doAnswer((Answer)invocation -> {
            return "foo";
        }).when(spy).get(0);
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

    //多次调用同一个测试用例
    @DisplayName("自定义名称重复测试")
    @RepeatedTest(value = 3, name = "{displayName} 第 {currentRepetition} 次")
    public void testRepeated() {
        System.out.println("执行测试");
    }

    //嵌套
    @Nested
    @DisplayName("AssertJ测试")
    static class AssertJTest {
        /**
         * 基本类型
         * 对可以精确匹配的值（比如字符串、整数），用isEqualTo()来比较，对不能精确匹配的值（比如浮点数），用isCloseTo()比较。
         * 对布尔值，用isTrue() 或isFalse()来比较。
         */
        @Test
        public void test_value_equals() {
            String hello = "hello".toUpperCase();
            assertThat(hello).isEqualTo("HELLO");

            int secondsOfDay = 24 * 60 * 60;
            assertThat(secondsOfDay).isEqualTo(86400);
        }

        @Test
        public void test_value_close() {
            double result = 0.1 + 0.1 + 0.1; // 0.30000000000000004
            assertThat(result).isCloseTo(0.3, offset(0.0001)); // 误差值
            assertThat(result).isCloseTo(0.3, withPercentage(0.01)); // 误差百分比
        }

        @Test
        public void test_boolean() {
            boolean flag = "Kubernetes".length() > 8;
            assertThat(flag).isTrue();

            boolean flag2 = "Docker".length() > 8;
            assertThat(flag2).isFalse();
        }

        @Test
        public void test_object_null_or_not_null() {
            Person p1 = new Person("William", 34);
            assertThat(p1).isNotNull();

            Person p2 = null;
            assertThat(p2).isNull();
        }

        @Test
        public void test_object_same_as_other() {
            Person p1 = new Person("William", 34);
            Person p2 = p1;
            assertThat(p1).isSameAs(p2);

            Person p3 = new Person("John", 35);
            assertThat(p1).isNotSameAs(p3);
        }

        /**
         * 单个对象
         * 判断是否为同一个对象用isSameAs()。
         * 如果重写了euqals和hashcode方法，也可以用isEqualTo来判断对象是否相同。
         * 如果只是判断对象的值是否相等，则可以用extracting提取后再判断，或用matches来用lambda表达式判断。
         * 判断是否为null用isNull()或isNotNull()。
         */
        @Test
        public void test_object_equals() {
            Person p1 = new Person("William", 34);
            Person p2 = new Person("William", 34);

            assertThat(p1).isNotSameAs(p2);
            assertThat(p1).isEqualTo(p2); // 如果用isEqualTo判断，则必须要重写equals方法

            // extracting method reference
            assertThat(p1).extracting(Person::getName, Person::getAge).containsExactly("William", 34);
            assertThat(p1).extracting(Person::getName, Person::getAge).containsExactly(p2.getName(), p2.getAge());

            // extracting field
            assertThat(p1).extracting("name", "age").containsExactly("William", 34);
            assertThat(p1).extracting("name", "age").containsExactly(p2.getName(), p2.getAge());

            // matches
            assertThat(p1).matches(x -> x.getName().equals("William") && x.getAge() == 34);
            assertThat(p1).matches(x -> x.getName().equals(p2.getName()) && x.getAge() == p2.getAge());
        }

        /**
         * 数组
         * 用isNull来判断数组是否为null。
         * 用isEmpty来判断数组是否为空（不包含任何元素）。
         * 用hasSize来判断数组的元素个数。
         * 用contains 判断数组中包含指定元素；用containsOnly判断数组中包含全部元素，但是顺序可以不一致；用cotainsExactly判断数组中包含全部元素且顺序需要一致。
         * 如果数组中的元素为对象，则需要通过extracting提取出对象的属性值，再来判断；如果提取出对象的多个属性值时，可以用tuple将多个属性值包装成元组
         * 用atIndex来获取指定位置/索引的元素
         */
        @Test
        public void test_array_null_or_empty() {
            String[] nullNames = null;
            assertThat(nullNames).isNull();

            String[] emptyNames = {};
            assertThat(emptyNames).isEmpty();
            assertThat(emptyNames).hasSize(0);
        }

        @Test
        public void test_array_contains() {
            String[] names = {"Python", "Golang", "Docker", "Java"};

            assertThat(names).contains("Docker");
            assertThat(names).doesNotContain("Haddop");

            assertThat(names).containsExactly("Python", "Golang", "Docker", "Java"); // 完全匹配，且顺序也一致
            assertThat(names).contains("Java", "Docker", "Golang", "Python"); // 完全匹配，顺序可以不一致

            assertThat(names).contains("Docker", atIndex(2)); // names[2]
        }

        /**
         * 集合
         */
        @Test
        public void test_array_object_contains() {
            Person[] names = {new Person("William", 34), new Person("John", 36), new Person("Tommy", 28),
                              new Person("Lily", 32)};

            assertThat(names).extracting(Person::getName).containsExactly("William", "John", "Tommy", "Lily");

            assertThat(names).extracting("name", "age")
                             .containsExactly(tuple("William", 34),
                                              tuple("John", 36),
                                              tuple("Tommy", 28),
                                              tuple("Lily", 32));

            assertThat(names).extracting(x -> x.getName(), x -> x.getAge())
                             .containsExactly(tuple("William", 34),
                                              tuple("John", 36),
                                              tuple("Tommy", 28),
                                              tuple("Lily", 32));
        }

        @Test
        public void test_list_contains() {
            List<Person> names = Arrays.asList(new Person("William", 34),
                                               new Person("John", 36),
                                               new Person("Tommy", 28),
                                               new Person("Lily", 32));

            assertThat(names).extracting(Person::getName).containsExactly("William", "John", "Tommy", "Lily");

            assertThat(names).extracting("name", "age")
                             .containsExactly(tuple("William", 34),
                                              tuple("John", 36),
                                              tuple("Tommy", 28),
                                              tuple("Lily", 32));

            assertThat(names).extracting(x -> x.getName(), x -> x.getAge())
                             .containsExactly(tuple("William", 34),
                                              tuple("John", 36),
                                              tuple("Tommy", 28),
                                              tuple("Lily", 32));
        }

        /**
         * 异常测试
         * <p>
         * 用try catch 来捕捉可能抛出的异常。
         * 用isInstanceOf来判断异常类型。
         * 用hasMessageContaining来模糊匹配异常信息，用hasMessage来精确匹配异常信息。
         * 用hasCauseInstanceOf 来判断异常原因（root cause）的类型。
         * 对checked exception和runtime exception的断言是一样的。
         *
         * @param fileName
         * @return
         * @throws IncorrectFileNameException
         */
        public String readFirstLine(String fileName) throws IncorrectFileNameException {
            try (Scanner file = new Scanner(new File(fileName))) {
                if (file.hasNextLine()) {
                    return file.nextLine();
                }
            } catch (FileNotFoundException e) {
                throw new IncorrectFileNameException("Incorrect file name: " + fileName, e);
            }
            return "";
        }

        /**
         * java custom exception:
         * https://www.baeldung.com/java-new-custom-exception
         */

        public class IncorrectFileNameException extends Exception {
            public IncorrectFileNameException(String message) {
                super(message);
            }

            public IncorrectFileNameException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        @Test
        public void test_exception_ArithmeticException() {
            try {
                double result = 1 / 0;
            } catch (Exception e) {
                // check exception type
                assertThat(e).isInstanceOf(ArithmeticException.class);

                // check exception message
                assertThat(e).hasMessageContaining("/ by zero");
            }
        }

        @Test
        public void test_exception_ArrayIndexOutOfBoundsException() {
            try {
                String[] names = {"William", "John", "Tommy", "Lily"};
                String name = names[4];
            } catch (Exception e) {
                // check exception type
                assertThat(e).isInstanceOf(ArrayIndexOutOfBoundsException.class);

                // check exception message
                assertThat(e).hasMessage("4");
            }
        }

        @Test
        public void test_exception_IllegalStateException() {
            try {
                List<String> names = Arrays.asList("William", "John", "Tommy", "Lily");
                Iterator<String> iter = names.iterator();
                iter.remove();
            } catch (Exception e) {
                // check exception type
                assertThat(e).isInstanceOf(IllegalStateException.class);
            }
        }

        @Test
        public void test_custom_exception() {
            try {
                String line = readFirstLine("./unknown.txt");
            } catch (Exception e) {
                // check exception type
                assertThat(e).isInstanceOf(IncorrectFileNameException.class);

                // check exception message
                assertThat(e).hasMessageContaining("Incorrect file name");

                // check cause type
                assertThat(e).hasCauseInstanceOf(FileNotFoundException.class);
            }
        }

    }

    //=========================================================参数测试=========================================================
    @Nested
    @DisplayName("参数测试")
    static class ParamTest {

        /**
         * https://segmentfault.com/a/1190000039852984
         *
         * @param string
         * @ValueSource: 为参数化测试指定入参来源，支持八大基础类以及String类型,Class类型
         * @NullSource: 表示为参数化测试提供一个null的入参
         * @EnumSource: 表示为参数化测试提供一个枚举入参
         * @CsvSource： 表示读取CSV格式内容作为参数化测试入参
         * @CsvFileSource： 表示读取指定CSV文件内容作为参数化测试入参
         * @MethodSource： 表示读取指定方法的返回值作为参数化测试入参(注意方法返回需要是一个流)
         * @ArgumentsSource： 指定一个自定义的，可重用的ArgumentsProvider。
         */
        //参数测试
        @ParameterizedTest
        @ValueSource(strings = {"one", "two", "three"})
        @DisplayName("ValueSource参数化测试")
        public void parameterizedTest1(String string) {
            assertThat(string).isNotBlank();
        }

        @ParameterizedTest
        @MethodSource("method")
        @DisplayName("方法来源参数")
        public void testWithExplicitLocalMethodSource(String name) {
            Assertions.assertNotNull(name);
        }

        private static Stream<String> method() {
            return Stream.of("apple", "banana");
        }

        @ParameterizedTest
        @CsvSource({"steven,18", "jack,24"})
        @DisplayName("参数化测试-csv格式")
        public void parameterizedTest3(String name, Integer age) {
            System.out.println("name:" + name + ",age:" + age);
            Assertions.assertNotNull(name);
            Assertions.assertTrue(age > 0);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/persons.csv")  //指定csv文件位置
        @DisplayName("参数化测试-csv文件")
        public void parameterizedTest2(String name, Integer age) {
            System.out.println("name:" + name + ",age:" + age);
            Assertions.assertNotNull(name);
            Assertions.assertTrue(age > 0);
        }

        @ParameterizedTest
        @EnumSource(value = TimeUnit.class, mode = EXCLUDE, names = {"DAYS", "HOURS"})
        @DisplayName("参数化测试-枚举")
        void testWithEnumSourceExclude(TimeUnit timeUnit) {
            assertFalse(EnumSet.of(TimeUnit.DAYS, TimeUnit.HOURS).contains(timeUnit));
        }
    }

}
