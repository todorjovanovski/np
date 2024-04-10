package kolok2._3_kalendarNaNastani;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class pr {
    public static void main(String[] args) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = df.parse("28.12.1888 22:30");
        System.out.println(date.getHours() + date.getMinutes());
        System.out.println(date);
        System.out.println(new SimpleDateFormat("dd MMM, YYY HH:mm").format(date));

    }
}
