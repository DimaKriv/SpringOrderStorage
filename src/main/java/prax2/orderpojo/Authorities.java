package prax2.orderpojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
@Table(name="AUTHORITIES")
public class Authorities {
     private String authority;
}
