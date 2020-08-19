package demo.plugin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SassError {

    private int status;
    private String file;
    private int line;
    private int column;
    private String message;
    private String formatted;
}
