import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class Test {
    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
        String ids = "124,56";
        String[] split = ids.split(",");
        for (String s : split) {
//            list.add(s);

        System.out.println(s);}
    }

    ArrayList<Long> longs = new ArrayList<Long>();
}
