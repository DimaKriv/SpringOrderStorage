package prax2.orderpojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id = -1L;

    @Size(min = 2)
    @NonNull  private String orderNumber;
    @Valid private List<OrderRow> orderRows;

    public void setNew() {
        id = -1L;
    }
}
