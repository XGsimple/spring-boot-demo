package com.xkcoding;

import com.xkcoding.component.Intent;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * 对于标记有 @Mock, @Spy, @InjectMocks 等注解的成员变量的 初始化 到目前为止有 2 种方法：
 * <p>
 * 对 JUnit 测试类添加 @RunWith(MockitoJUnitRunner.class)
 * 在标示有 @Before 方法内调用初始化方法：MockitoAnnotations.initMocks(Object)
 * 上面的测试用例，对于 @Mock 等注解的成员变量的初始化又多了一种方式 MockitoRule。
 * 规则 MockitoRule 会自动帮我们调用 MockitoAnnotations.initMocks(this)
 * 去 实例化 出 注解 的成员变量，我们就无需手动进行初始化了。
 *
 * @author xugangq
 * @date 2021/6/9 10:11
 */
@MockitoSettings(strictness = Strictness.WARN)
public class MockitoInitTest {
    @Mock
    private Intent mIntent;

    //    @Rule
    //    public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void mockAndroid() {
        Intent intent = mockIntent();
        assertThat(intent.getAction()).isEqualTo("com.ycy.model.mockito");
        assertThat(intent.getStringExtra("Name")).isEqualTo("Whyn");
    }

    private Intent mockIntent() {
        when(mIntent.getAction()).thenReturn("com.yn.test.mockito");
        when(mIntent.getStringExtra("Name")).thenReturn("Whyn");
        return mIntent;
    }
}
