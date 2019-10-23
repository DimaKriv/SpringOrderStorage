package prax2.orderpojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ErrorsJson {
  private List<Mistake> errors;
}

