package prax2;

import lombok.Getter;
import prax2.orderpojo.Order;
import prax2.orderpojo.OrderRow;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class PaymentPlan {
    private @Min(3) long sum;
    private @Min(1) int interval;
    private  @Getter LocalDate date;
    private  @Getter int payment = 0;
    private int payed = 0;
    private int payedTimes = 0;

    public PaymentPlan(Order o, LocalDate startLocal, LocalDate endLocal) {
        this.sum = getSum(o);
        this.interval = getInterval(startLocal, endLocal, sum);
        date = startLocal;
        payment = Long.valueOf(sum).intValue() / interval;
    }
    private Long getSum(Order o) {
        @NotNull List<OrderRow> rows = o.getOrderRows();
        return rows.stream().mapToLong(row -> row.getPrice() * row.getQuantity()).sum();
    }

    private int getInterval(LocalDate startLocal, LocalDate endLocal, long sum) {
        int  period = 12 * (endLocal.getYear() - startLocal.getYear()) + (endLocal.getMonth().getValue()
                - startLocal.getMonth().getValue()) + 1;
        if (period > sum / 3) {
            period = Long.valueOf(sum).intValue() / 3;
        }
        return period;
    }

    public boolean next() {
        if (payedTimes < interval) {
            if (payedTimes > 0) {
                date = date.plusMonths(1).minusDays(date.getDayOfMonth() - 1);
                payment = Long.valueOf((sum - payed) / (interval - payedTimes)).intValue();
            }
            payed += payment;
            payedTimes++;
            return true;
        } else {
            return false;
        }
    }
}
