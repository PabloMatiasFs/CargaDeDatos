/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.Services;

import com.company.Controller.Controller;
import com.company.Entity.PersonaModel;
import com.company.Entity.Persona;
import com.company.Repository.RepositoryPersona;
import com.company.component.PersonaConver;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Notebook
 */
@Service("ServicePersonaImp")
public class ServicePersonaImp implements ServicePersona {
    
    private static final Log LOG = LogFactory.getLog(ServicePersonaImp.class);

    @Autowired
    @Qualifier("repositoryPersona")
    private RepositoryPersona repositoryPersona;
    
    @Autowired
    @Qualifier("personaConver")
    private PersonaConver personaConver;
    
    @Override
    public List<Persona>ListPersonas() {
        LOG.info("Call "+" ListPersonas()");
        List<Persona> personas = repositoryPersona.findAll();
        List<PersonaModel> personaModel = new ArrayList<PersonaModel>();
        for (Persona persona : personas) {
            personaModel.add(personaConver.personaModelPersona(persona));
        }
        return personas;
    }

    @Override
    public PersonaModel addPersona(PersonaModel personaModel) {
        LOG.info("Call "+" addPersona()");
        Persona persona = repositoryPersona.save(personaConver.personaPersonaModel(personaModel));
        return personaConver.personaModelPersona(persona);
    }

    @Override
    public void DeletPersona(int id) {
        Persona persona = findPersonaById(id);
        LOG.info("Call "+" DeletPersona()");
        if (null != persona) {
            repositoryPersona.delete(persona);
        }
    }

    @Override
    public PersonaModel UpdatePersona(PersonaModel personaModel) {
        LOG.info("Call "+" UpdatePersona()");
        if (personaModel.getId_persona() != null && personaModel.getId_persona() > 0) {
            Persona persona = repositoryPersona.save(personaConver.personaPersonaModel(personaModel));
            return personaConver.personaModelPersona(persona);
        }
        return null;
    }

    @Override
    public Persona findPersonaById(int id) {
        LOG.info("Call "+" DeletPersona()");
        return repositoryPersona.findById(id);
    }
    
    public PersonaModel findPersonaModelById(int id){
        return personaConver.personaModelPersona(findPersonaById(id));
    }
   

}
