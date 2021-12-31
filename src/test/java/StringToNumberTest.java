import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

public class StringToNumberTest {
    @Test
    public void testStringToNumber() {
        String str = "00208";
        Integer i = Integer.parseInt(str);
        System.out.println("i=" + i);

    }

    @Test
    public void doubleToStringTest() {
        DecimalFormat df = new DecimalFormat("0.00");
        Double number = 1232.45634343;
        System.out.println("double: " + df.format(number));
    }
}
