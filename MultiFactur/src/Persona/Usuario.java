package Persona;

public class Usuario {
    //Contador compartido por todos los usuarios: cada nuevo usuario recibe el siguiente id
    private static int contadorId = 1;

    private final int id;
    private String nombre;
    private String usuario;
    private String contrasena;

    //Constructor
    public Usuario(String nombre, String usuario, String contrasena){
        this.id = contadorId++;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    //Getter y Setter
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
