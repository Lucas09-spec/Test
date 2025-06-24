package Rol.com.example.ROL.DataLoader;



import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import Rol.com.example.ROL.Model.RolModel;
import Rol.com.example.ROL.Service.RolService;

@Component
public class DataLoader implements CommandLineRunner {

    private final RolService rolService;

    public DataLoader(RolService rolService) {
        this.rolService = rolService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (rolService.getRol().isEmpty()) {
            rolService.guardarRol(new RolModel(null, "administrador"));
            rolService.guardarRol(new RolModel(null, "coordinador"));
            rolService.guardarRol(new RolModel(null, "tecnico"));
            rolService.guardarRol(new RolModel(null, "empleado"));
            rolService.guardarRol(new RolModel(null, "cliente"));

            System.out.println("Roles precargados insertados.");
        }
    }
}