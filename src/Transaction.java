import java.math.BigDecimal;
import java.util.Date;

public record Transaction(long debitingAccID, long creditingAccID, BigDecimal amount, Date date, String gctDate) {


}
