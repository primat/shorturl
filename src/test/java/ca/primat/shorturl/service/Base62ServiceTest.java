package ca.primat.shorturl.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Base62ServiceTest {

    @Autowired
    private Base62Service base62Service;

    // Encoding method tests

    @Test
    public void testEncode0() {
        Assert.assertEquals("0", base62Service.encode(0L));
    }

    @Test
    public void testEncode1() {
        Assert.assertEquals("1", base62Service.encode(1L));
    }

    @Test
    public void testEncode10() {
        Assert.assertEquals("a", base62Service.encode(10L));
    }

    @Test
    public void testEncode35() {
        Assert.assertEquals("z", base62Service.encode(35L));
    }

    @Test
    public void testEncode61() {
        Assert.assertEquals("Z", base62Service.encode(61L));
    }

    @Test
    public void testEncode62() {
        Assert.assertEquals("10", base62Service.encode(62L));
    }

    @Test
    public void testEncodeMaxMinusOne() {
        Assert.assertEquals("ZZZZZZZZZY", base62Service.encode(839299365868340222L));
    }

    @Test
    public void testEncodeMax() {
        Assert.assertEquals("ZZZZZZZZZZ", base62Service.encode(839299365868340223L));
    }


    // Decoding method tests

    @Test
    public void testDecode0() {
        Assert.assertEquals(0L, base62Service.decode("0"));
    }

    @Test
    public void testDecode1() {
        Assert.assertEquals(1L, base62Service.decode("1"));
    }

    @Test
    public void testDecodeMax() {
        Assert.assertEquals(839299365868340223L, base62Service.decode("ZZZZZZZZZZ"));
    }

    @Test
    public void testDecode10() {
        Assert.assertEquals(10L, base62Service.decode("a"));
    }

    @Test
    public void testDecode35() {
        Assert.assertEquals(35L, base62Service.decode("z"));
    }

    @Test
    public void testDecode61() {
        Assert.assertEquals(61L, base62Service.decode("Z"));
    }

    @Test
    public void testDecode62() {
        Assert.assertEquals(62L, base62Service.decode("10"));
    }


//    @Test
//    public void testDecodeMaxPlusOne() {
//        Assert.assertEquals(839299365868340223L, base62Service.decode("10000000000"));
//    }

//    @Test
//    public void testEncodeMaxPlusOne() {
//        Assert.assertEquals("10000000000", base62Service.encode(839299365868340224L));
//    }
}
