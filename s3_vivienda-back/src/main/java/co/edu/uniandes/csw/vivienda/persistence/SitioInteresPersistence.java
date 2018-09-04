/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.vivienda.persistence;

import co.edu.uniandes.csw.vivienda.entities.SitioInteresEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author msalcedo
 */
@Stateless
public class SitioInteresPersistence {
        private static final Logger LOGGER = Logger.getLogger(SitioInteresPersistence.class.getName());
    
       @PersistenceContext(unitName = "UniviviendaPU")
    protected EntityManager em;

    /**
     * Método para persisitir la entidad en la base de datos.
     *
     * @param sitioInteresEntity objeto sitioInteres que se creará en la base de datos
     * @return devuelve la entidad creada con un id dado por la base de datos.
     */
    public SitioInteresEntity create(SitioInteresEntity sitioInteresEntity) {
        LOGGER.log(Level.INFO, "Creando un sitioInteres nuevo");

        em.persist(sitioInteresEntity);
        LOGGER.log(Level.INFO, "Saliendo de crear un sitioInteres nuevo");
        return sitioInteresEntity;
    }
    
    public List<SitioInteresEntity> findAll() {
        LOGGER.log(Level.INFO, "Consultando todos los sitioInteres");
        // Se crea un query para buscar todas las authores en la base de datos.
        TypedQuery query = em.createQuery("select u from SitioInteresEntity u", SitioInteresEntity.class);
        // Note que en el query se hace uso del método getResultList() que obtiene una lista de authores.
        return query.getResultList();
    }
        
    /**
     * Busca si hay alguna arrendador con el id que se envía de argumento
     *
     * @param sitioInteresId: id correspondiente al sitioInteres buscado.
     * @return un sitioInteres.
     */
    public SitioInteresEntity find(Long sitioInteresId) {
        LOGGER.log(Level.INFO, "Consultando el sitioInteres con id={0}", sitioInteresId);
        return em.find(SitioInteresEntity.class, sitioInteresId);
    }
    
      /**
     * Actualiza un sitioInteres.
     *
     * @param sitioInteresEntity
     * @return un sitioInteres con los cambios aplicados.
     */
    public SitioInteresEntity update(SitioInteresEntity sitioInteresEntity) {
        LOGGER.log(Level.INFO, "Actualizando el arrendador con id={0}", sitioInteresEntity.getId());
        return em.merge(sitioInteresEntity);
    }
    
        /**
     * Borra un sitioInteres de la base de datos recibiendo como argumento el id del sitioInteres
     *
     * @param sitioInteresId: id correspondiente al sitioInteres a borrar.
     */
    public void delete(Long sitioInteresId) {

        LOGGER.log(Level.INFO, "Borrando el sitioInteres con id={0}", sitioInteresId);
        SitioInteresEntity sitioInteresEntity = em.find(SitioInteresEntity.class, sitioInteresId);
        em.remove(sitioInteresEntity);
    }
}
