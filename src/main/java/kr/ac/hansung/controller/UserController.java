package kr.ac.hansung.controller;

import kr.ac.hansung.dto.PasswordChangeDto;
import kr.ac.hansung.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/password")
    public String passwordForm(Model model) {
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "user/password";
    }

    @PostMapping("/user/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute PasswordChangeDto dto,
            Model model,
            RedirectAttributes ra) {

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다");
            return "user/password";
        }

        try {
            userService.changePassword(userDetails.getUsername(),
                    dto.getCurrentPassword(), dto.getNewPassword());
            ra.addFlashAttribute("successMessage", "비밀번호가 변경되었습니다");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/password";
        }

        return "redirect:/home";
    }
}