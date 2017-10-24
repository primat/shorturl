package ca.primat.shorturl.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
public class Base62ServiceTest {

    private Base62Service base62Service;

    @Before
    public void setUp() {
        base62Service = new Base62Service();
    }

    @Test
    public void testEncode() {
        assertThat(base62Service.encode(0L)).isEqualTo("0");
        assertThat(base62Service.encode(1L)).isEqualTo("1");
        assertThat(base62Service.encode(10L)).isEqualTo("a");
        assertThat(base62Service.encode(35L)).isEqualTo("z");
        assertThat(base62Service.encode(36L)).isEqualTo("A");
        assertThat(base62Service.encode(61L)).isEqualTo("Z");
        assertThat(base62Service.encode(839299365868340222L)).isEqualTo("ZZZZZZZZZY");
        assertThat(base62Service.encode(839299365868340223L)).isEqualTo("ZZZZZZZZZZ");
        assertThat(base62Service.encode(839299365868340224L)).isEqualTo("10000000000");
        assertThatThrownBy( () -> base62Service.encode(-1L) ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testDecode() {
        assertThat(base62Service.decode("0")).isEqualTo(0L);
        assertThat(base62Service.decode("1")).isEqualTo(1L);
        assertThat(base62Service.decode("a")).isEqualTo(10L);
        assertThat(base62Service.decode("z")).isEqualTo(35L);
        assertThat(base62Service.decode("A")).isEqualTo(36L);
        assertThat(base62Service.decode("Z")).isEqualTo(61L);
        assertThat(base62Service.decode("ZZZZZZZZZY")).isEqualTo(839299365868340222L);
        assertThat(base62Service.decode("ZZZZZZZZZZ")).isEqualTo(839299365868340223L);
        assertThat(base62Service.decode("10000000000")).isEqualTo(839299365868340224L);
        assertThatThrownBy( () -> base62Service.decode("-1") ).isInstanceOf(IllegalArgumentException.class);
    }
}
