import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Test2 {
    @Test
    public  void test2() {
        List<String> list=new ArrayList<String>();

            for (int i = 1; i <= 100; i++) {
                String str=i+"";
                if (i % 3 == 0 || str.contains("3")) {
                    list.add("Fizz");
                } else if (i % 5 == 0 || str.contains("5")){
                    list.add("Buzz");
                }else if (i % 3 == 0 ||i % 5 == 0 || str.contains("3")||str.contains("5")){
                    list.add("FizzBuzz");
                }
                else {
                    list.add(str);
                }
            }
        System.out.println(list);
    }
}
