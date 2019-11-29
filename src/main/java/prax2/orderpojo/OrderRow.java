package prax2.orderpojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRow {
    private  Long orderId = -1L;
    @NonNull private String itemName;
    @Min(1) @NonNull private  Integer quantity;
    @Min(1) @NonNull private  Integer price;

}
