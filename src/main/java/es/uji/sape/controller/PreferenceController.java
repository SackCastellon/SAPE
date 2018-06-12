package es.uji.sape.controller;

import es.uji.sape.dao.InternshipOfferDao;
import es.uji.sape.dao.PreferenceDao;
import es.uji.sape.model.Preference;
import es.uji.sape.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/preferences")
public class PreferenceController {

    private final @NotNull PreferenceDao preferenceDao;
    private final @NotNull InternshipOfferDao internshipOfferDao;

    @Autowired
    public PreferenceController(@NotNull PreferenceDao preferenceDao, @NotNull InternshipOfferDao internshipOfferDao) {
        this.preferenceDao = preferenceDao;
        this.internshipOfferDao = internshipOfferDao;
    }

    @GetMapping
    @SuppressWarnings("unchecked")
    public final @NotNull String list(@NotNull Model model, Authentication auth, HttpSession session) {
        List<Preference> preferences = preferenceDao.findStudentPreferences(((UserInfo) auth.getPrincipal()).getUsername());

        if (preferences.isEmpty()) {
            preferences = (List<Preference>) session.getAttribute("tempPreferences");
        } else {
            preferences.forEach(it -> it.setName(internshipOfferDao.findNameAndDescription(it.getProjectOfferId())));
        }

        if (preferences == null) preferences = List.of();

        model.addAttribute("preferences", preferences);

        return "/preferences/list";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model, HttpSession session) {
        List<Preference> tempPreferences = Collections.list(session.getAttributeNames()).contains("tempPreferences") ? (List<Preference>) session.getAttribute("tempPreferences") : List.of();
        Set<Integer> ids = tempPreferences.stream().map(Preference::getProjectOfferId).collect(Collectors.toSet());
        model.addAttribute("internshipOffers", internshipOfferDao.findAll().stream().filter(it -> !ids.contains(it.getId())).collect(Collectors.toList()));
        return "/preferences/add";
    }

    @PostMapping("/add/{offerId:[\\d]+}")
    public final @NotNull String processAddSubmit(@PathVariable("offerId") int offerId, Authentication auth, HttpSession session) {
        if (!internshipOfferDao.find(offerId).isPresent()) return "/preferences/add";
        try {
            List<Preference> tempPreferences = Collections.list(session.getAttributeNames()).contains("tempPreferences") ? (List<Preference>) session.getAttribute("tempPreferences") : new ArrayList();

            Preference preference = new Preference();
            preference.setPriority(tempPreferences.size() + 1);
            preference.setStudentCode(((UserInfo) auth.getPrincipal()).getUsername());
            preference.setProjectOfferId(offerId);
            preference.setName(internshipOfferDao.findNameAndDescription(offerId));

            tempPreferences.add(preference);
            session.setAttribute("tempPreferences", tempPreferences);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/preferences";
    }

    @GetMapping("/delete/{preferencePriority:[\\d]+}")
    public final @NotNull String processDelete(@PathVariable("preferencePriority") int preferencePriority, HttpSession session) {
        if (Collections.list(session.getAttributeNames()).contains("tempPreferences")) {
            List<Preference> tempPreferences = (List<Preference>) session.getAttribute("tempPreferences");

            if ((preferencePriority > 0) && (preferencePriority < tempPreferences.size())) {
                tempPreferences.remove(preferencePriority);
                session.setAttribute("tempPreferences", tempPreferences);
            }
        }
        return "redirect:/preferences";
    }

    @GetMapping("/save")
    public final @NotNull String save(HttpSession session) {
        List<Preference> tempPreferences = (List<Preference>) session.getAttribute("tempPreferences");
        tempPreferences.forEach(preferenceDao::add);
        return "redirect:/";
    }
}
