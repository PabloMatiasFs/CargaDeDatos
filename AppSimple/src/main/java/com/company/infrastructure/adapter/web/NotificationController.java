package com.company.infrastructure.adapter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para manejar notificaciones en redirects
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController {

    /**
     * Redirige a la lista con mensaje de éxito
     */
    @GetMapping("/success")
    public String showSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Operación completada exitosamente");
        return "redirect:/personas/listado";
    }

    /**
     * Redirige a la lista con mensaje de error
     */
    @GetMapping("/error")
    public String showError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Ocurrió un error en la operación");
        return "redirect:/personas/listado";
    }

    /**
     * Redirige a la lista con mensaje de advertencia
     */
    @GetMapping("/warning")
    public String showWarning(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("warning", "Advertencia: Verifique los datos");
        return "redirect:/personas/listado";
    }
} 