package cyclone.otusspring.librarymigration;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellCommands {
    @ShellMethod(value = "Start migration")
    public String startMigration() {
        return "unsupported";
    }
}
