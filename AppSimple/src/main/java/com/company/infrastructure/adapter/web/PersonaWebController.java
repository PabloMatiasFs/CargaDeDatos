package com.company.infrastructure.adapter.web;

import com.company.application.service.PersonaApplicationService;
import com.company.domain.entity.Persona;
import com.company.infrastructure.adapter.web.dto.PersonaCreateRequest;
import com.company.infrastructure.adapter.web.dto.PersonaResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controlador web para vistas Thymeleaf
 * Mantiene compatibilidad con la interfaz web original
 */
@Controller
@RequestMapping("/personas")
public class PersonaWebController {

    private static final Logger log = LoggerFactory.getLogger(PersonaWebController.class);
    private static final String INDEX_VIEW = "index";
    private static final String FORM_VIEW = "index2";

    private final PersonaApplicationService personaApplicationService;

    public PersonaWebController(PersonaApplicationService personaApplicationService) {
        this.personaApplicationService = personaApplicationService;
    }

    /**
     * Vuelve a la vista principal al cancelar
     */
    @GetMapping("/cancelar")
    public ModelAndView cancelar() {
        return listarPersonas();
    }

    /**
     * Lista todas las personas en la vista principal
     */
    @GetMapping("/listado")
    public ModelAndView listarPersonas() {
        log.info("Listando todas las personas en vista web");
        ModelAndView mav = new ModelAndView(INDEX_VIEW);
        List<Persona> personas = personaApplicationService.obtenerTodasLasPersonas();
        
        // Convertir las entidades de dominio a DTOs para la vista
        List<PersonaResponse> personasResponse = personas.stream()
                .map(persona -> new PersonaResponse(
                    persona.getId() != null ? persona.getId().getValue() : null,
                    persona.getNombre(),
                    persona.getApellido(),
                    persona.getEmail() != null ? persona.getEmail().getValue() : null,
                    persona.getTelefono() != null ? persona.getTelefono().getValue() : null,
                    persona.getDireccion(),
                    persona.getNombreCompleto()
                ))
                .collect(java.util.stream.Collectors.toList());
        
        mav.addObject("personas", personasResponse);
        return mav;
    }

    /**
     * Muestra el formulario para agregar nueva persona
     */
    @GetMapping("/addpersona")
    public ModelAndView mostrarFormulario(Model model) {
        log.info("Mostrando formulario para crear persona");
        ModelAndView mav = new ModelAndView(FORM_VIEW);
        model.addAttribute("person", new PersonaCreateRequest());
        return mav;
    }

    /**
     * Procesa el formulario para agregar nueva persona
     */
    @PostMapping("/addpersona")
    public String agregarPersona(@ModelAttribute("person") PersonaCreateRequest personaRequest, 
                                Model model, RedirectAttributes redirectAttributes) {
        log.info("Procesando formulario para crear persona: {}", personaRequest);
        
        try {
            model.addAttribute("person", personaRequest);
            personaApplicationService.crearPersona(
                    personaRequest.getNombre(),
                    personaRequest.getApellido(),
                    personaRequest.getEmail(),
                    personaRequest.getTelefono(),
                    personaRequest.getDireccion()
            );
            redirectAttributes.addFlashAttribute("success", "✅ Persona creada exitosamente");
            return "redirect:/personas/listado";
        } catch (IllegalArgumentException e) {
            log.error("Error al crear persona: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return FORM_VIEW;
        }
    }

    /**
     * Elimina una persona mediante ID
     */
    @GetMapping("/eliminarpersona")
    public String eliminarPersona(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando persona con ID: {}", id);
        
        boolean eliminada = personaApplicationService.eliminarPersona(id);
        
        if (eliminada) {
            redirectAttributes.addFlashAttribute("success", "✅ Persona eliminada exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ No se pudo eliminar la persona");
        }
        
        return "redirect:/personas/listado";
    }

    /**
     * Muestra formulario para editar persona
     */
    @GetMapping("/editarpersona")
    public ModelAndView editarPersona(@RequestParam("id") Integer id, Model model) {
        log.info("Mostrando formulario para editar persona con ID: {}", id);
        
        Optional<Persona> personaOptional = personaApplicationService.obtenerPersonaPorId(id);
        
        if (personaOptional.isPresent()) {
            Persona persona = personaOptional.get();
            PersonaCreateRequest personaRequest = new PersonaCreateRequest(
                    persona.getNombre(),
                    persona.getApellido(),
                    persona.getEmail().getValue(),
                    persona.getTelefono().getValue(),
                    persona.getDireccion()
            );
            // Agregar el ID para la actualización
            model.addAttribute("person", personaRequest);
            model.addAttribute("personId", id);
            return new ModelAndView(FORM_VIEW);
        } else {
            log.warn("Persona con ID {} no encontrada", id);
            ModelAndView mav = listarPersonas();
            mav.addObject("error", "Persona no encontrada");
            return mav;
        }
    }

    /**
     * Procesa la actualización de persona
     */
    @PostMapping("/editarpersona")
    public String actualizarPersona(@RequestParam(value = "id", required = false) Integer id,
                                   @ModelAttribute("person") PersonaCreateRequest personaRequest,
                                   Model model, RedirectAttributes redirectAttributes) {
        log.info("Actualizando persona con ID: {}, datos: {}", id, personaRequest);
        
        if (id == null) {
            model.addAttribute("error", "ID de persona no proporcionado");
            return FORM_VIEW;
        }
        
        try {
            Optional<Persona> personaActualizada = personaApplicationService.actualizarPersona(
                    id,
                    personaRequest.getNombre(),
                    personaRequest.getApellido(),
                    personaRequest.getEmail(),
                    personaRequest.getTelefono(),
                    personaRequest.getDireccion()
            );
            
            if (personaActualizada.isPresent()) {
                redirectAttributes.addFlashAttribute("success", "✅ Persona actualizada exitosamente");
                return "redirect:/personas/listado";
            } else {
                log.warn("No se pudo actualizar la persona con ID: {}", id);
                model.addAttribute("error", "Persona no encontrada");
                model.addAttribute("personId", id);
                return FORM_VIEW;
            }
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar persona: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("personId", id);
            return FORM_VIEW;
        }
    }

    /**
     * Redirige la raíz a la lista de personas
     */
    @GetMapping("/")
    public String redirectToList() {
        return "redirect:/personas/listado";
    }
}