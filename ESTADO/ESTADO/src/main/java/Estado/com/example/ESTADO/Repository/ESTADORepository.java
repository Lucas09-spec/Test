package Estado.com.example.ESTADO.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Estado.com.example.ESTADO.Model.ESTADO;
@Repository
public interface ESTADORepository extends JpaRepository<ESTADO,Long>{

}
