package cyclone.otusspring.library.actuator;

import cyclone.otusspring.library.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DashboardInfoContributor implements InfoContributor {

    private final DashboardService dashboardService;


    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Long> map = new HashMap<>();
        map.put("authors-count", dashboardService.getAuthorCount());
        map.put("genres-count", dashboardService.getGenreCount());
        map.put("books-count", dashboardService.getBookCount());

        builder.withDetail("dashboard", map);
    }
}
