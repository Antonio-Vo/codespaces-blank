import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class date{
    public static void main(String[] args) {

        LocalDateTime date1 = LocalDateTime.of(2025, 11, 24, 9, 11);
        LocalDateTime date2 = LocalDateTime.of(2024, 3, 24, 1, 53);

        System.out.println(date1);
        System.out.println(date2);
        if (date1.isBefore(date1)) {
             System.out.println(date1);
        } else if (date1.isAfter(date2)) {
            
        }
    }
 }