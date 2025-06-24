package Usuario.com.example.USUARIO.Controller;
import Usuario.com.example.USUARIO.Security.LoginRequest;
import Usuario.com.example.USUARIO.Security.MessageResponse;
import Usuario.com.example.USUARIO.Model.Usuario;
import Usuario.com.example.USUARIO.Repository.UsuarioRepository;
import Usuario.com.example.USUARIO.Security.JwtResponse;
import Usuario.com.example.USUARIO.Security.JwtUtils;
import Usuario.com.example.USUARIO.Security.UserDetailsImpl;
import Usuario.com.example.USUARIO.Service.UsuarioService;
import Usuario.com.example.USUARIO.WebUsuario.UserUsuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

   
    @Autowired
    private UserUsuario userUsuario;

    @Autowired 
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (!userUsuario.rolExistePorId(usuario.getRol())) {
            return ResponseEntity.badRequest().body("Error: El ID de rol no existe.");
        }
        usuarioService.crearUsuario(usuario); 
        return ResponseEntity.status(201).body("Usuario creado correctamente.");
    }

  

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);


            return ResponseEntity.ok(new MessageResponse("Te has logeado correctamente"));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("Correo o contraseña incorrectos"));
        }
    }
}
