package prax2.orderpojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Table(name = "order_row")
public class OrderRow {

    @Column(name = "item_name")
    @NonNull
    private String itemName;

    @Min(1)
    @NonNull
    private  Integer quantity;

    @Min(1)
    @NonNull
    private  Integer price;

}
