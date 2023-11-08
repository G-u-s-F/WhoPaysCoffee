package ioc.codemugteam.whopayscoffee;

public class Usuari {

    private String name, email;

    public Usuari(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
