/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.Services;

import com.company.Entity.PersonaModel;
import com.company.Entity.Persona;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Notebook
 */
public interface ServicePersona extends Serializable{
    
    public abstract List<Persona>ListPersonas();
    public abstract PersonaModel addPersona(PersonaModel personaModel);
    public abstract Persona findPersonaById(int id);
    public abstract void DeletPersona(int id);
    public abstract PersonaModel UpdatePersona (PersonaModel personaModel);
    public abstract PersonaModel findPersonaModelById(int id);
}
