package prax2.orderpojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.CollectionTable;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_task")
public class Order {

    @Id
    @SequenceGenerator(name="seq1", sequenceName="seq1", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq1")
    private Long id;

    @Size(min = 2)
    @Column(name = "order_number")
    @NonNull
    private String orderNumber;

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_row", joinColumns = @JoinColumn(name = "orders_id", referencedColumnName = "id"))
    private List<OrderRow> orderRows;

}
