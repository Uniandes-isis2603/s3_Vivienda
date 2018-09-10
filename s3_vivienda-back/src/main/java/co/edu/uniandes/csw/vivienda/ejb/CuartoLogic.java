package co.edu.uniandes.csw.vivienda.ejb;

import co.edu.uniandes.csw.vivienda.entities.CuartoEntity;
import co.edu.uniandes.csw.vivienda.entities.ViviendaEntity;
import co.edu.uniandes.csw.vivienda.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.vivienda.persistence.CuartoPersistence;
import co.edu.uniandes.csw.vivienda.persistence.ViviendaPersistence;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import java.util.List;

@Stateless
public class CuartoLogic {
    @Inject
    CuartoPersistence cuartoPersistence;

    @Inject
    ViviendaPersistence viviendaPersistence;

    public CuartoEntity addCuarto(Long viviendaId, CuartoEntity cuartoEntity) throws BusinessLogicException{
        ViviendaEntity vivienda = viviendaPersistence.find(viviendaId);
        if(vivienda == null){
            throw new BusinessLogicException("No existe la vivienda");
        }
        cuartoEntity.setVivienda(vivienda);
        cuartoPersistence.create(cuartoEntity);
        return cuartoEntity;
    }

    public List<CuartoEntity> getCuartos(Long viviendaId){
        ViviendaEntity viviendaEntity = viviendaPersistence.find(viviendaId);
        if (viviendaEntity == null){
            return null;
        }
        return viviendaEntity.getCuartos();
    }

    public CuartoEntity getCuarto(Long viviendaId, Long cuartoId) throws BusinessLogicException{
        ViviendaEntity vivienda = viviendaPersistence.find(viviendaId);
        if(vivienda == null){
            return null;
        }
        CuartoEntity cuarto = cuartoPersistence.find(cuartoId);
        if (cuarto == null){
            return null;
        }

        List<CuartoEntity> cuartosVivienda = vivienda.getCuartos();
        int index = cuartosVivienda.indexOf(cuarto);
        if(index >= 0){
            return cuartosVivienda.get(index);
        }
        throw new BusinessLogicException("El cuarto no esta asociado a la vivienda");
    }
}
