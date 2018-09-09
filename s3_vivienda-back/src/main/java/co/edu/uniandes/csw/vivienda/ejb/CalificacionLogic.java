/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.vivienda.ejb;

import co.edu.uniandes.csw.vivienda.entities.CalificacionEntity;
import co.edu.uniandes.csw.vivienda.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.vivienda.persistence.CalificacionPersistence;
import co.edu.uniandes.csw.vivienda.persistence.EstudiantePersistence;
import co.edu.uniandes.csw.vivienda.persistence.ViviendaPersistence;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author calificacion
 */
@Stateless
public class CalificacionLogic {
    private static final Logger LOGGER = Logger.getLogger(CalificacionLogic.class.getName());

    @Inject
    private CalificacionPersistence persistence; // Variable para acceder a la persistencia de la aplicación. Es una inyección de dependencias.

    @Inject
    private EstudiantePersistence persistenceEstudiante;
    
    @Inject
    private ViviendaPersistence persistenceVivienda;
    /**
     * Crea un calificacion en la persistencia.
     *
     * @param calificacionEntity La entidad que representa el calificacion a
     * persistir.
     * @return La entidad del calificacion luego de persistirlo.
     * @throws BusinessLogicException Si el estudiante es invalido.
     * @throws BusinessLogicException Si la vivienda es invalida.
     * @throws BusinessLogicException Si el puntaje es mayor o igual a 0, o menor o igual a 5
     */
    public CalificacionEntity createCalificacion(CalificacionEntity calificacionEntity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de creación del calificacion");
        // Verifica la regla de negocio que dice que no puede haber dos contratos con el mismo ID
        if (calificacionEntity.getEstudiante() == null || persistenceEstudiante.find(calificacionEntity.getEstudiante().getId()) == null) {
            throw new BusinessLogicException("El estudiante es invalido");
        }
        if (calificacionEntity.getVivienda() == null || persistenceVivienda.find(calificacionEntity.getVivienda().getId()) == null ){
            throw new BusinessLogicException("La vivienda es invalida");
        }
        if (calificacionEntity.getPuntaje() >= 0 && calificacionEntity.getPuntaje() <= 5) {
            throw new BusinessLogicException("El puntaje debe ser mayor o igual a 0, o menor o igual a 5");
        }
        // Invoca la persistencia para crear el contrato
        persistence.create(calificacionEntity);
        LOGGER.log(Level.INFO, "Termina proceso de creación del calificacion");
        return calificacionEntity;
    }
    
    public Collection<CalificacionEntity> getCalificaciones(Long id){
        LOGGER.log(Level.INFO, "Inicia proceso de consultar todos los libros");
        Collection<CalificacionEntity> calificacioness = persistence.findAll();
        LOGGER.log(Level.INFO, "Termina proceso de consultar todos los libros");
        return calificacioness;
    }
    
    public CalificacionEntity getCalificacion(Long id){
        LOGGER.log(Level.INFO, "Inicia proceso de consultar todos los calificacions");
        CalificacionEntity calificacion = persistence.find(id);
        if (calificacion == null) {
            LOGGER.log(Level.SEVERE, "El calificacions con el id = {0} no existe", id);
        }
        LOGGER.log(Level.INFO, "Termina proceso de consultar todos los calificacions");
        return calificacion;
    }
    
    public CalificacionEntity updateCalificacion(Long id, CalificacionEntity calificacionEntity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar el calificacion con id = {0}", id);
        if (calificacionEntity.getEstudiante() != null && persistenceEstudiante.find(calificacionEntity.getEstudiante().getId()) == null) {
            throw new BusinessLogicException("El estudiante es invalido");
        }
        if (calificacionEntity.getVivienda() != null && persistenceVivienda.find(calificacionEntity.getVivienda().getId()) == null ){
            throw new BusinessLogicException("La vivienda es invalida");
        }
        if (calificacionEntity.getPuntaje() >= 0 && calificacionEntity.getPuntaje() <= 5) {
            throw new BusinessLogicException("El puntaje debe ser mayor o igual a 0, o menor o igual a 5");
        }
        calificacionEntity.setId(id);
        CalificacionEntity newEntity = persistence.update(calificacionEntity);
        LOGGER.log(Level.INFO, "Termina proceso de actualizar el calificacion con id = {0}", calificacionEntity.getId());
        return newEntity;
    }

    /**
     * Borrar un calificacion
     *
     * @param calificacionId: id del calificacion a borrar
     */
    public void deleteCalificacion(Long calificacionId) {
        LOGGER.log(Level.INFO, "Inicia proceso de borrar el calificacion con id = {0}", calificacionId);
        // Note que, por medio de la inyección de dependencias se llama al método "delete(id)" que se encuentra en la persistencia.
        persistence.delete(calificacionId);
        LOGGER.log(Level.INFO, "Termina proceso de borrar el calificacion con id = {0}", calificacionId);
    }
}
