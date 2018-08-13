package com.azra.controllers;

import com.azra.entities.AzraUser;
import com.azra.repositories.PaymentCycleRepository;
import com.azra.repositories.UserRepository;
import com.azra.services.AzraUserDetailsService;
import com.azra.services.ImageService;
import com.azra.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* Controller class contains methods that handle user requests */

@Controller
public class AzraController {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private AzraUserDetailsService azraUserDetailsService;
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private ImageService imageService;
    @Autowired
    private PaymentCycleRepository paymentCycleRepository;
    @Autowired
    private PaymentService paymentService;

    // returns the index page.
    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if (null != principal) {
            // putting a value in the model makes it available for reference from the HTML page.

            // get the user making this request a.k.a Principal and put them in the model
            AzraUser user = (AzraUser) azraUserDetailsService.loadUserByUsername(principal.getName());
            // add the contribution extras instance to the model.
            model.addAttribute("contExtras", paymentService.getContributionExtras(principal.getName()));
            // add the current user to the model.
            model.addAttribute("user", user);
            // add contributions for the current day or today to the model
            model.addAttribute("contributions", this.paymentService.todaysContribution());
            // add the current payment cycle to the model
            model.addAttribute("currentCycle", this.paymentCycleRepository.getCurrentPaymentCycle(true));
            // return the "dashboard.html" template.
            return "dashboard";
        } else {
            return "index";
        }
    }

    @GetMapping("/Azra/paymentsCycle")
    public String paymentCycle(Principal principal, Model model){
        // get the user making this request and put them in the model
        AzraUser user = (AzraUser) azraUserDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("contExtras", paymentService.getContributionExtras(principal.getName()));
        model.addAttribute("user", user);
        model.addAttribute("currentCycle", this.paymentCycleRepository.getCurrentPaymentCycle(true));
        return "payments_cycle";
    }

    @GetMapping("/Azra/dashboard")
    public String dashboard(Principal principal, Model model) {
        // get the user making this request and put them in the model
        AzraUser user = (AzraUser) azraUserDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("contributions", this.paymentService.todaysContribution());
        model.addAttribute("contExtras", paymentService.getContributionExtras(principal.getName()));
        model.addAttribute("currentCycle", this.paymentCycleRepository.getCurrentPaymentCycle(true));
        return "dashboard";
    }

    @GetMapping("/Azra/members")
    public String members(Principal principal, Model model) {
        // get the user making this request and put them in the model
        AzraUser user = (AzraUser) azraUserDetailsService.loadUserByUsername(principal.getName());
        // get all the members and add them to the model
        model.addAttribute("user", user);
        model.addAttribute("contExtras", paymentService.getContributionExtras(principal.getName()));
        model.addAttribute("members", this.userRepository.findAll());
        model.addAttribute("currentCycle", this.paymentCycleRepository.getCurrentPaymentCycle(true));
        return "members";
    }

    @GetMapping("/Azra/myProfile")
    public String myProfile(Principal principal, Model model) {
        // get the user making this request and put them in the model
        AzraUser user = (AzraUser) azraUserDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("contExtras", paymentService.getContributionExtras(principal.getName()));
        model.addAttribute("user", user);
        model.addAttribute("currentCycle", this.paymentCycleRepository.getCurrentPaymentCycle(true));
        return "my_profile";
    }


    // endpoint to add a new member
    @RequestMapping("/addNewMember")
    public String addNewMember(@RequestParam String name, @RequestParam String phoneNumber, @RequestParam String gender) {
        if (name.trim().isEmpty() || phoneNumber.trim().isEmpty() || gender.trim().isEmpty()) {
            return "redirect:/Azra/members?empty";
        } else {
            boolean isEmpty = Stream.of("Male", "Female", "Other").
                    filter(g -> g.equals(gender)).collect(Collectors.toList()).isEmpty();
            if (isEmpty) {
                return "redirect:/Azra/members?invalidGender";
            } else {
                if (userRepository.existsById(phoneNumber)) {
                    return "redirect:/Azra/members?duplicate";
                }
                AzraUser azraUser = new AzraUser();
                azraUser.setRole("USER");
                azraUser.setPassword(this.passwordEncoder.encode("password"));
                azraUser.setGender(gender);
                azraUser.setProfileImage(imageService.loadDefaultImage());
                azraUser.setName(name);
                azraUser.setUsername(phoneNumber);
                azraUser.setPhoneNumber(phoneNumber);

                this.userRepository.save(azraUser);
                return "redirect:/Azra/members?added";
            }
        }
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestParam String username) {
        this.userRepository.deleteById(username);
        return "redirect:/Azra/members?deleted";
    }

    @RequestMapping("/blockUser")
    public String blockUser(@RequestParam String username) {
        AzraUser azraUser = this.userRepository.findById(username).get();
        azraUser.setEnabled(false);
        this.userRepository.save(azraUser);

        return "redirect:/Azra/members?blocked";
    }


    @RequestMapping("/unBlockUser")
    public String unblockUser(@RequestParam String username) {
        AzraUser azraUser = this.userRepository.findById(username).get();
        azraUser.setEnabled(true);
        this.userRepository.save(azraUser);

        return "redirect:/Azra/members?unblocked";
    }


    @RequestMapping("/updateUsername")
    public String updateUsername(Principal principal, @RequestParam String oldUsername, @RequestParam String newUsername) {
        if (principal.getName().equals(oldUsername.trim())) {
            // okay, the user 'knows' themselves, so we can proceed to change the username
            AzraUser azraUser = this.userRepository.findById(oldUsername).get();
            if (azraUser.isUsernameUpdatable()) {
                AzraUser newAzraUser = new AzraUser();
                newAzraUser.setUsername(newUsername);
                newAzraUser.setPassword(azraUser.getPassword());
                newAzraUser.setEnabled(azraUser.isEnabled());
                newAzraUser.setName(azraUser.getName());
                newAzraUser.setPhoneNumber(azraUser.getPhoneNumber());
                newAzraUser.setGender(azraUser.getGender());
                newAzraUser.setDateJoined(azraUser.getDateJoined());
                newAzraUser.setProfileImage(azraUser.getProfileImage());
                newAzraUser.setRole(azraUser.getRole());
                newAzraUser.setUsernameUpdatable(false);

                this.userRepository.save(newAzraUser);
                this.userRepository.delete(azraUser);
                return "redirect:/Azra/myProfile?updated";
            } else {
                return "redirect:/Azra/myProfile";
            }
        } else {
            return "redirect:/Azra/myProfile?badUsername";
        }
    }


    @RequestMapping("/updatePassword")
    public String updatePassword(Principal principal, @RequestParam String oldPassword, @RequestParam String newPassword) {
        AzraUser azraUser = this.userRepository.findById(principal.getName()).get();
        if (oldPassword.trim().equals(newPassword)) {
            return "redirect:/Azra/myProfile?noMatch";
        } else if (!passwordEncoder.matches(oldPassword, azraUser.getPassword())) {
            return "redirect:/Azra/myProfile?invalidPassword";
        } else {
            azraUser.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(azraUser);
            return "redirect:/Azra/myProfile?success";
        }
    }

    @RequestMapping("/updatePhoneNumber")
    public String updatePhoneNumber(Principal principal, @RequestParam String phoneNumber, @RequestParam String name) {
        AzraUser azraUser = this.userRepository.findById(principal.getName()).get();
        azraUser.setPhoneNumber(phoneNumber);
        azraUser.setName(name);
        this.userRepository.save(azraUser);
        return "redirect:/Azra/myProfile?phoneSuccess";
    }


    @PostMapping("/")
    public String logIn() {
        return "index";
    }

    @GetMapping("/logOut")
    public String logOut() {
        this.httpSession.invalidate();
        return "redirect:/?loggedOut";
    }

    @RequestMapping("/updateImage")
    public String updateImage(Principal principal, @RequestParam MultipartFile profilePic) throws IOException {
        if (profilePic != null && !(profilePic.isEmpty())) {
            AzraUser azraUser = this.userRepository.findById(principal.getName()).get();
            azraUser.setProfileImage(profilePic.getBytes());
            this.userRepository.save(azraUser);
            return "redirect:/Azra/myProfile?updatedImage";
        }
        return "redirect:/Azra/myProfile?imageError";
    }

    @GetMapping("/report")
    public String getReport(Model model){
        model.addAttribute("members", this.userRepository.findAll());
        model.addAttribute("currentCycle", this.paymentCycleRepository.getCurrentPaymentCycle(true));
        model.addAttribute("contributions", this.paymentService.todaysContribution());

        return "report";
    }
}
