package prax2;

import prax2.orderpojo.ErrorsJson;
import prax2.orderpojo.Mistake;
import prax2.orderpojo.Order;

import java.util.List;

public class OrderValidation {
    private ErrorsJson checkOrderNumberAndReturnErrors(String number) {
        if (number.length() < 2) {
            ErrorsJson error = new ErrorsJson();
            error.setErrors(List.of(new Mistake("too_short_number")));
            return error;
        }
        return null;
    }

    public ErrorsJson checkOrder(Order o) {
        return checkOrderNumberAndReturnErrors(o.getOrderNumber());
    }
}
