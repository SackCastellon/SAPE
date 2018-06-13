package es.uji.sape.controller;

import es.uji.sape.dao.BusinessDao;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/preferences")
public class PreferenceController {

    private final @NotNull PreferenceDao prefDao;
    private final @NotNull InternshipOfferDao offerDao;
    private final @NotNull BusinessDao businessDao;

    @Autowired
    public PreferenceController(@NotNull PreferenceDao prefDao, @NotNull InternshipOfferDao offerDao, @NotNull BusinessDao businessDao) {
        this.prefDao = prefDao;
        this.offerDao = offerDao;
        this.businessDao = businessDao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model, Authentication auth) {
        List<Preference> prefs = prefDao.findStudentPreferences(((UserInfo) auth.getPrincipal()).getUsername());
        prefs.forEach(it -> it.setName(offerDao.findNameAndDescription(it.getProjectOfferId())));

        model.addAttribute("prefs", prefs);

        return "/preferences/list";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model, Authentication auth) {
        List<Preference> preferences = prefDao.findStudentPreferences(((UserInfo) auth.getPrincipal()).getUsername());
        Set<Integer> ids = preferences.stream().map(Preference::getProjectOfferId).collect(Collectors.toSet());

        model.addAttribute("internshipOffers", offerDao.findAll().stream()
                .filter(it -> !ids.contains(it.getId()))
                .peek(offer -> {
                    String businessName = businessDao.getName(offer.getContactUsername());
                    offer.setBusinessName(businessName);
                })
                .collect(Collectors.toList()));

        return "/preferences/add";
    }

    @PostMapping("/add/{offerId:[\\d]+}")
    public final @NotNull String processAddSubmit(@PathVariable("offerId") int offerId, Authentication auth) {
        if (!offerDao.find(offerId).isPresent()) return "/preferences/add";
        try {
            List<Preference> prefs = prefDao.findStudentPreferences(((UserInfo) auth.getPrincipal()).getUsername());

            Preference preference = new Preference();
            preference.setPriority(prefs.size() + 1);
            preference.setStudentCode(((UserInfo) auth.getPrincipal()).getUsername());
            preference.setProjectOfferId(offerId);
            preference.setName(offerDao.findNameAndDescription(offerId));

            prefDao.add(preference);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/preferences";
    }

    @GetMapping("/delete/{preferencePriority:[\\d]+}")
    public final @NotNull String processDelete(@PathVariable("preferencePriority") int preferencePriority, Authentication auth) {
        prefDao.delete(preferencePriority, ((UserInfo) auth.getPrincipal()).getUsername());

        List<Preference> prefs = new ArrayList<>(prefDao.findStudentPreferences(((UserInfo) auth.getPrincipal()).getUsername()));
        prefs.sort(Comparator.comparing(Preference::getPriority));

        for (int i = 0; i < prefs.size(); i++) {
            Preference pref = prefs.get(i);
            pref.setPriority(i + 1);
            prefDao.update(pref);
        }

        return "redirect:/preferences";
    }

    @GetMapping("/save")
    public final @NotNull String save(Authentication auth) {
        List<Preference> prefs = prefDao.findStudentPreferences(((UserInfo) auth.getPrincipal()).getUsername());
        return (prefs.size() >= 5) ? "redirect:/?savedPrefs" : "redirect:/";
    }
}
