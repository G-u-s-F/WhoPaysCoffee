package ioc.codemugteam.whopayscoffee;

public class Grup {

    private int id;
    private String name;

    public Grup (String name, int id){
        this.name = name;
        this.id = id;
    }
    public Grup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }
}
