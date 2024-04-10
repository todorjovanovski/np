package kolok1._3_mernaStanica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class pr {
    public static void main(String[] args) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date0 = df.parse("17.12.2013 23:50:12");
        System.out.println(date0.getTime());
        Date date = df.parse("17.12.2013 23:51:12");
        System.out.println(date.getTime());
        System.out.println(((double) date0.getTime() - date.getTime() ) / 60000);

        Random random = new Random();
        for(int a = 0; a < 10; a++) {
            System.out.println(random.nextInt(100));
        }
    }
}
