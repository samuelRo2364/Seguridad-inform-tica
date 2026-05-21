package com.example.cookies.controller;

import com.example.cookies.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private final String USER = "samuel";
    private final String PASSWORD = "123";

    @GetMapping("/")
    public String inicio() {

        return """
                <h1>Login JWT + Cookies</h1>

                <a href='/login?usuario=samuel&password=123'>
                Iniciar Sesión
                </a>

                <br><br>

                <a href='/perfil'>
                Ver Perfil
                </a>

                <br><br>

                <a href='/logout'>
                Cerrar Sesión
                </a>
                """;
    }

    @GetMapping("/login")
    public String login(
            @RequestParam String usuario,
            @RequestParam String password,
            HttpSession session,
            HttpServletResponse response
    ) {

        if (!usuario.equals(USER) ||
                !password.equals(PASSWORD)) {

            return "Usuario o contraseña incorrectos";
        }

        session.setAttribute("usuario", usuario);

        String token =
                JwtUtil.generarToken(usuario);

        Cookie jwtCookie =
                new Cookie("jwt", token);

        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");

        response.addCookie(jwtCookie);

        return """
                Login exitoso

                JWT generado correctamente

                Cookie guardada
                """;
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session) {

        String usuario =
                (String) session.getAttribute("usuario");

        if (usuario == null) {

            return "No hay sesión activa";
        }

        return """
                Perfil del usuario:

                Usuario: """ + usuario;
    }

    @GetMapping("/logout")
    public String logout(
            HttpSession session,
            HttpServletResponse response
    ) {

        session.invalidate();

        Cookie jwtCookie =
                new Cookie("jwt", null);

        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");

        response.addCookie(jwtCookie);

        return "Sesión cerrada";
    }
}