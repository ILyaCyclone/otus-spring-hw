package cyclone.otusspring.library;

import cyclone.otusspring.library.dao.DataAccessProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(DataAccessProfiles.ACTIVE)
public class LibraryApplicationTests {

    @Test
    public void contextLoads() {
    }

}
