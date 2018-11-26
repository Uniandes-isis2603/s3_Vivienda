/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.vivienda.ejb;

import co.edu.uniandes.csw.vivienda.entities.ContratoEntity;
import co.edu.uniandes.csw.vivienda.entities.ViviendaEntity;
import co.edu.uniandes.csw.vivienda.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.vivienda.persistence.ContratoPersistence;
import co.edu.uniandes.csw.vivienda.persistence.ViviendaPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author estudiante
 */
@Stateless
public class ContratoLogic {

    private static final Logger LOGGER = Logger.getLogger(ContratoLogic.class.getName());

    @Inject
    private ContratoPersistence persistence;

    @Inject
    private ViviendaPersistence viviendaPersistence;

    /**
     * Guardar un nuevo contrato
     *
     * @param contratoEntity La entidad de tipo contrato del nuevo contrato a
     * persistir.
     * @return La entidad luego de persistirla
     * @throws BusinessLogicException Si el metodoPago es inválido o ya existe
     * en la persistencia.
     */
    public ContratoEntity createContrato(ContratoEntity contratoEntity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de creación del contrato");
        if (contratoEntity.getVivienda() == null || viviendaPersistence.find(contratoEntity.getVivienda().getId()) == null) {
            throw new BusinessLogicException("La vivienda es inválida");
        }
        if (!validateMetodoPago(contratoEntity.getMetodoPago())) {
            throw new BusinessLogicException("El metodoPago es inválido");
        }
        persistence.create(contratoEntity);
        LOGGER.log(Level.INFO, "Termina proceso de creación del contrato");
        return contratoEntity;
    }

    /**
     * Devuelve todos los contratos que hay en la base de datos.
     *
     * @return Lista de entidades de tipo contrato.
     */
    public List<ContratoEntity> getContratos() {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar todos los contratos");
        List<ContratoEntity> contratos = persistence.findAll();
        LOGGER.log(Level.INFO, "Termina proceso de consultar todos los contratos");
        return contratos;
    }

    /**
     * Busca un contrato por ID
     *
     * @param contratoId El id del contrato a buscar
     * @return El contrato encontrado, null si no lo encuentra.
     */
    public ContratoEntity getContrato(Long contratoId) {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar el contrato con id = {0}", contratoId);
        ContratoEntity contratoEntity = persistence.find(contratoId);
        if (contratoEntity == null) {
            LOGGER.log(Level.SEVERE, "El contrato con el id = {0} no existe", contratoId);
        }
        LOGGER.log(Level.INFO, "Termina proceso de consultar el contrato con id = {0}", contratoId);
        return contratoEntity;
    }

    /**
     * Actualizar un contrato por ID
     *
     * @param contratoId El ID del contrato a actualizar
     * @param contratoEntity La entidad del contrato con los cambios deseados
     * @return La entidad del contrato luego de actualizarla
     * @throws BusinessLogicException Si el metodoPago de la actualizacion es
     * invalido
     */
    public ContratoEntity updateContrato(Long contratoId, ContratoEntity contratoEntity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar el contrato con id = {0}", contratoId);
        if (!validateMetodoPago(contratoEntity.getMetodoPago())) {
            throw new BusinessLogicException("El metodoPago es inválido");
        }
        ContratoEntity newContrato = null;
        if (getContrato(contratoId) != null) {
            newContrato = persistence.update(contratoEntity);
        }
        LOGGER.log(Level.INFO, "Termina proceso de actualizar el contrato con id = {0}", contratoEntity.getId());
        return newContrato;
    }
    
        public ContratoEntity actualizarContrato(Long viviendaId, Long contratoId, ContratoEntity contratoEntity) throws BusinessLogicException {
        if (contratoEntity.getMetodoPago() != null && contratoEntity.getMetodoPago() <= 0) {
            throw new BusinessLogicException("El metodoPago debe ser mayor a 0");
        }
        ViviendaEntity vivienda = viviendaPersistence.find(viviendaId);
        contratoEntity.setVivienda(vivienda);
        contratoEntity.setId(contratoId);
        persistence.update(contratoEntity);
        return contratoEntity;
    }

    /**
     * Eliminar un contrato por ID
     *
     * @param contratoId El ID del contrato a eliminar
     * @throws co.edu.uniandes.csw.vivienda.exceptions.BusinessLogicException
     */
    public void deleteContrato(Long contratoId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de borrar el contrato con id = {0}", contratoId);
        if (persistence.find(contratoId) == null) {
            throw new BusinessLogicException("El contrato no existe");
        }
        persistence.delete(contratoId);
        LOGGER.log(Level.INFO, "Termina proceso de borrar el contrato con id = {0}", contratoId);
    }

    /**
     * Verifica que el metodoPago no sea invalido.
     *
     * @param metodoPago a verificar
     * @return true si el metodoPago es valido.
     */
    private boolean validateMetodoPago(Integer metodoPago) {
        return metodoPago > 0;
    }

    /**
     * Verifica que el id no sea invalido.
     *
     * @param id a verificar
     * @return true si el metodoPago es valido.
     */
    private boolean validateId(Long id) {
        return !(id == null || id < 0);
    }

    public void generarDatos() {
        List<ContratoEntity> contratosViejos = getContratos();

        for (ContratoEntity co : contratosViejos) {
            try {
                deleteContrato(co.getId());
            } catch (BusinessLogicException e) {
                LOGGER.log(Level.INFO, "Error en el proceso de borrar el contrato de la vivienda con id = {0}", co.getId());
            }
        }

        List<ViviendaEntity> viviendasViejas = viviendaPersistence.findAll();
        for (ViviendaEntity vivienda : viviendasViejas) {
            viviendaPersistence.delete(vivienda.getId());
        }

        String[] ciudades = new String[]{
            "Bogotá", "Cali", "Medellín", "Barranquilla", "Cucuta", "Bucaramanga"
        };
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            ViviendaEntity v = new ViviendaEntity();
            v.setNombre("Vivienda " + (i + 1));
            v.setCiudad(ciudades[rand.nextInt(ciudades.length)]);
            v.setDireccion(String.format("Calle %d #%d-%d apto %d0%d", rand.nextInt(100), rand.nextInt(100), rand.nextInt(80), rand.nextInt(10), rand.nextInt(5)));
            v.setImgUrl("assets/img/vivienda" + (i + 1) + ".jpg");
            v.setDescripcion("Una casa (del latín casa, choza) es una edificación destinada para ser habitada.");
            v.setTipo("B");

            List<String> includedServices = new ArrayList<>(5);
            includedServices.add("Internet");
            includedServices.add("Agua");
            includedServices.add("Gas");
            includedServices.add("Electricidad");
            v.setServiciosIncluidos(includedServices);
            viviendaPersistence.create(v);
        }

        List<ViviendaEntity> viviendas = viviendaPersistence.findAll();
        rand = new Random();
        for (int i = 0; i < 10; i++) {
            ContratoEntity c = new ContratoEntity();
            c.setMetodoPago(rand.nextInt(100));
            int anio = rand.nextInt(5) + 2011;
            int mes = rand.nextInt(12);
            int dia = rand.nextInt(28);
            c.setFechaInicio(anio + "-" + mes + "-" + dia + "");
            c.setFechaFin((anio + 2) + "-" + mes + "-" + dia + "");
            c.setVivienda(viviendas.get(rand.nextInt(viviendas.size())));
            try {
                createContrato(c);
            } catch (BusinessLogicException e) {
                LOGGER.log(Level.INFO, "Hubo un error creando el contrato");
            }
        }

    }

}
