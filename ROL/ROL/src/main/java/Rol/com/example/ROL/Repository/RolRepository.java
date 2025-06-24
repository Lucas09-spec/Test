package Rol.com.example.ROL.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Rol.com.example.ROL.Model.RolModel;

@Repository
public interface RolRepository extends JpaRepository<RolModel,Long>{

}
