import org.junit.jupiter.api.Test;

public class StringToNumberTest {
    @Test
    public void testStringToNumber() {
        String str = "00208";
        Integer i = Integer.parseInt(str);
        System.out.println("i=" + i);

    }
}
