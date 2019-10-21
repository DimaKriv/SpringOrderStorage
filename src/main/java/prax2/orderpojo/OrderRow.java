package prax2.orderpojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRow {
    private  Long orderId = -1L;
    @NonNull private String itemName;
    @NonNull private  Integer quantity;
    @NonNull private  Integer price;

}
