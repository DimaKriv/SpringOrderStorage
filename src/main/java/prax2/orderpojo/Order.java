package prax2.orderpojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id = -1L;
    @NonNull  private String orderNumber;
    private List<OrderRow> orderRows;

    public void setNew() {
        id = -1L;
    }
}
