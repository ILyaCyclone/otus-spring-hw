package cyclone.otusspring.library.controller.view;

import cyclone.otusspring.library.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboardView(Model model) {
        model.addAttribute("authorCount", dashboardService.getAuthorCount());
        model.addAttribute("bookCount", dashboardService.getBookCount());
        model.addAttribute("genreCount", dashboardService.getGenreCount());

        return "dashboard";
    }

}
