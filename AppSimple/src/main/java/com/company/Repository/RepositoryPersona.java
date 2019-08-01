/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.Repository;

import com.company.Entity.PersonaModel;
import com.company.Entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Notebook
 */
public interface RepositoryPersona extends JpaRepository<Persona, Integer>{
    
    public abstract Persona findById(int id);
    public Persona save(PersonaModel person);
}
