package kolok2._2_mernaStanica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class pr {
    public static void main(String[] args) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = df.parse("10.12.2013 21:30:15");
        Date date1 = df.parse("11.12.2013 22:30:15");
        long difference = Math.abs(date.getTime() - date1.getTime());
        long diff = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        System.out.println(diff);
    }
}
