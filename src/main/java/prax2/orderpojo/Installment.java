package prax2.orderpojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Installment {
    private int amount;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

}


