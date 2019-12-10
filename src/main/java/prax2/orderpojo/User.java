package prax2.orderpojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="USERS")
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;
    @Column(name="first_name")
    private String firstName;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "AUTHORITIES",  joinColumns = @JoinColumn(name = "username"
            , referencedColumnName = "username"))
    private List<Authorities> authorities;
}
