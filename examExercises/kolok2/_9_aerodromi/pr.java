package kolok2._9_aerodromi;

import java.time.Duration;

public class pr {
    public static void main(String[] args) {
        Duration duration = Duration.ofMinutes(215);
        System.out.println(duration.toHours() % 24);
        System.out.println(duration.toMinutes() % 60);
    }
}
