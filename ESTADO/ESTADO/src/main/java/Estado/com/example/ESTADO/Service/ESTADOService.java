package Estado.com.example.ESTADO.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Estado.com.example.ESTADO.Model.ESTADO;
import Estado.com.example.ESTADO.Repository.ESTADORepository;

@Service
public class ESTADOService {

    @Autowired 
    private ESTADORepository estadoRepository; 

    // Recupera todos los estados
    public List<ESTADO> obEstados(){
        return estadoRepository.findAll();
    }

    // Guarda o actualiza un estado
    public ESTADO guardarEstado(ESTADO nuevo){
        return estadoRepository.save(nuevo);
    }
    
    // Busca un estado por su ID (útil para el PUT)
    public Optional<ESTADO> buscarEstadoPorId(Long id) {
        return estadoRepository.findById(id);
    }
    
    // Elimina un estado dado su ID (método para DELETE)
    public void eliminarEstado(Long id) {
        estadoRepository.deleteById(id);
    }
}

