package es.uji.sape.controller;

import es.uji.sape.dao.AssignmentDao;
import es.uji.sape.dao.BusinessDao;
import es.uji.sape.dao.InternshipOfferDao;
import es.uji.sape.dao.PreferenceDao;
import es.uji.sape.dao.ProjectOfferDao;
import es.uji.sape.dao.TutorDao;
import es.uji.sape.exceptions.HttpUnauthorizedException;
import es.uji.sape.model.Assignment;
import es.uji.sape.model.AssignmentState;
import es.uji.sape.model.OfferState;
import es.uji.sape.model.Preference;
import es.uji.sape.model.ProjectOffer;
import es.uji.sape.model.Role;
import es.uji.sape.model.Tutor;
import es.uji.sape.security.UserInfo;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static es.uji.sape.model.AssignmentState.ACCEPTED;
import static es.uji.sape.model.AssignmentState.PENDING;
import static es.uji.sape.model.AssignmentState.TRANSFERRED;

@Slf4j
@Controller
@RequestMapping("/preferences")
public class PreferenceController {

    private final @NotNull PreferenceDao prefDao;
    private final @NotNull InternshipOfferDao offerDao;
    private final @NotNull ProjectOfferDao projectDao;
    private final @NotNull BusinessDao businessDao;
    private final @NotNull AssignmentDao assignmentDao;
    private final @NotNull TutorDao tutorDao;

    @Autowired
    public PreferenceController(@NotNull PreferenceDao prefDao, @NotNull InternshipOfferDao offerDao, @NotNull BusinessDao businessDao, @NotNull AssignmentDao assignmentDao, @NotNull TutorDao tutorDao, @NotNull ProjectOfferDao projectDao) {
        this.prefDao = prefDao;
        this.offerDao = offerDao;
        this.projectDao = projectDao;
        this.businessDao = businessDao;
        this.assignmentDao = assignmentDao;
        this.tutorDao = tutorDao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model, Authentication auth) {
        UserInfo userInfo = (UserInfo) auth.getPrincipal();
        if (!userInfo.getAuthorities().contains(Role.STUDENT)) throw new HttpUnauthorizedException("Only students can access this page");

        List<Preference> prefs = prefDao.findStudentPreferences(userInfo.getUsername());
        prefs.forEach(it -> it.setName(offerDao.findNameAndDescription(it.getProjectOfferId())));

        model.addAttribute("prefs", prefs);

        return "/preferences/list";
    }

    @GetMapping("/{studentCode}")
    public final @NotNull String listStudent(@NotNull Model model, @PathVariable("studentCode") String studentCode, Authentication auth) {
        List<Preference> prefs = prefDao.findStudentPreferences(studentCode);
        prefs.forEach(it -> it.setName(offerDao.findNameAndDescription(it.getProjectOfferId())));

        Set<Integer> offerIds = prefs.stream().map(Preference::getProjectOfferId).collect(Collectors.toSet());
        List<Integer> usedIds = assignmentDao.findAll().parallelStream().filter(it -> offerIds.contains(it.getProjectOfferId())).filter(it -> Set.of(ACCEPTED, PENDING, TRANSFERRED).contains(it.getState())).map(Assignment::getProjectOfferId).collect(Collectors.toList());

        List<Integer> notAvailablePriorities = prefs.stream().filter(it -> usedIds.contains(it.getProjectOfferId())).map(Preference::getPriority).collect(Collectors.toList());

        model.addAttribute("prefs", prefs);
        model.addAttribute("studentCode", studentCode);
        model.addAttribute("notAvailablePriorities", notAvailablePriorities);
        model.addAttribute("status", List.<Pair<Integer, AssignmentState>>of()); // TODO Pasar lista de IDs de ofertas justo a su estado por si ya se ha signado una o se ha rechazado

        return "/preferences/list";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model, Authentication auth) {
        List<Preference> preferences = prefDao.findStudentPreferences(((UserDetails) auth.getPrincipal()).getUsername());
        Set<Integer> ids = preferences.stream().map(Preference::getProjectOfferId).collect(Collectors.toSet());

        final List<Integer> projectIds = projectDao.findAll().stream().filter(it -> it.getState() == OfferState.ACCEPTED).map(ProjectOffer::getId).collect(Collectors.toList());

        model.addAttribute("internshipOffers", offerDao.findAll().stream()
                .filter(it -> projectIds.contains(it.getId()))
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
            List<Preference> prefs = prefDao.findStudentPreferences(((UserDetails) auth.getPrincipal()).getUsername());

            Preference preference = new Preference();
            preference.setPriority(prefs.size() + 1);
            preference.setStudentCode(((UserDetails) auth.getPrincipal()).getUsername());
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
        prefDao.delete(preferencePriority, ((UserDetails) auth.getPrincipal()).getUsername());

        List<Preference> prefs = new ArrayList<>(prefDao.findStudentPreferences(((UserDetails) auth.getPrincipal()).getUsername()));
        prefs.sort(Comparator.comparing(Preference::getPriority));

        for (int i = 0; i < prefs.size(); i++) {
            Preference pref = prefs.get(i);
            pref.setPriority(i + 1);
            prefDao.update(pref);
        }

        return "redirect:/preferences";
    }

    @GetMapping("/assign/{studentCode}/{preferencePriority:[\\d]+}")
    public final @NotNull String processAssign(@PathVariable("studentCode") String studentCode, @PathVariable("preferencePriority") int preferencePriority) {
        prefDao.findStudentPreferences(studentCode).stream().filter(it -> it.getPriority() == preferencePriority).mapToInt(Preference::getProjectOfferId).findFirst().ifPresent(
                offerId -> projectDao.find(offerId).ifPresent(offer -> {
                    List<String> tutorCodes = tutorDao.findAll().parallelStream().filter(it -> it.getItinerary() == offer.getItinerary()).map(Tutor::getCode).collect(Collectors.toList());

                    if (!tutorCodes.isEmpty()) {
                        val i = ThreadLocalRandom.current().nextInt(tutorCodes.size());
                        val tutorCode = tutorCodes.get(i);

                        val assignment = new Assignment();
                        assignment.setProjectOfferId(offerId);
                        assignment.setStudentCode(studentCode);
                        assignment.setTutorCode(tutorCode);
                        assignment.setTutorName(tutorDao.getName(tutorCode));
                        assignment.setName(projectDao.findNameAndDescription(offerId));
                        assignment.setObjectives(projectDao.findObjectives(offerId));
                        assignment.setStartDate(offerDao.findStartDate(offerId));
                        assignment.setProposalDate(LocalDate.now());
                        assignment.setState(PENDING);

                        assignmentDao.add(assignment);
                    }
                })
        );

        return String.format("redirect:/preferences/%s", studentCode);
    }


}
