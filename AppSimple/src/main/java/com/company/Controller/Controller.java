/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.Controller;

import com.company.Entity.PersonaModel;
import com.company.Entity.Persona;
import com.company.Repository.RepositoryPersona;
import com.company.Services.ServicePersona;
import com.company.Services.ServicePersonaImp;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 *
 * @author Notebook
 */
@RestController
@RequestMapping("/personas")
public class Controller {

    public static final String PA_STRING = "index";
    public static final String FROM_STRING = "index2";

    private static final Log LOG = LogFactory.getLog(Controller.class);

    @Autowired
    @Qualifier("ServicePersonaImp")
    private ServicePersonaImp servicePersonaImp;

    @Autowired
    public ServicePersona servicePersona;
    
    //Vuelve a la vista principal( Donde de alistan las personas) al momento de cancelar la futura carga de datos.
    @RequestMapping(value = "/cancelar", method = RequestMethod.GET)
    public ModelAndView Cancelar() {
        return listarModelAndView();
    }
    
    //Alista Todas las personas guardadas en la base de datos, usando Los servicios ya creados. 
    @RequestMapping(value = "/listado", method = RequestMethod.GET)
    public ModelAndView listarModelAndView() {
        LOG.info("Call " + " ListPersonas()");
        ModelAndView mav = new ModelAndView(PA_STRING);
        mav.addObject("personas", servicePersonaImp.ListPersonas());
        return mav;
    }
    
    //Toma los datos del Formulario y Crea un objeto.
    @RequestMapping(value = "/addpersona", method = RequestMethod.GET)
    public ModelAndView VistaForm(Model model) {
        ModelAndView mav = new ModelAndView(FROM_STRING);
        model.addAttribute("person", new PersonaModel());
        LOG.info("Call " + " Crear Persona ()");
        return mav;
    }
    
    //Toma ese objeto Creado y lo guarda en la base de datos mediante los Servicios ya creados.
    @RequestMapping(value = "/addpersona", method = RequestMethod.POST)
    public String AddPersona(@ModelAttribute(name = "person") PersonaModel personaModel, Model model, HttpServletResponse httpResponse) throws IOException {
        LOG.info("Call" + " Guardar Persona ()" + personaModel.toString());
        model.addAttribute("person", personaModel);
        servicePersonaImp.addPersona(personaModel);
        httpResponse.sendRedirect("/personas/listado");
        return "redirect:/personas/listado";
    }
    
    //Elimina una persona Mediante un Id.
    @RequestMapping(value = "/eliminarpersona", method = RequestMethod.GET)
    public ModelAndView EliminarPersona(@RequestParam(name = "id", required = true) int id) {
        LOG.info("Call " + " EliminarPersona()");
        servicePersonaImp.DeletPersona(id);
        return listarModelAndView();
    }
    
    //Edita Las personas tomandolas por el Id.
    @RequestMapping(value = "/editarpersona", method = RequestMethod.GET)
    public ModelAndView EditPersona(@RequestParam(name = "id", required = false) int id,Model model) {
        PersonaModel personaModel = new PersonaModel();
        if (id != 0) {
             personaModel = servicePersonaImp.findPersonaModelById(id);
             servicePersonaImp.UpdatePersona(personaModel);
        }
        model.addAttribute("person",personaModel);
        return new ModelAndView(FROM_STRING);
    }
}
