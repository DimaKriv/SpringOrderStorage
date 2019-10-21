package prax2.orderdao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConnectionInfo {
    private String dbUrl;
    private String dbUser;
    private String dbPss;
}
