package pl.put.poznan.checker.logic;

public class Scenario implements Visitable{
    private String role;
    private String name;

    public Scenario(String name, String role){
        this.name = name;
        this.role = role;
    }

    public String getName(){
        return this.name;
    }

    public String getRole(){
        return this.role;
    }

    public void accept(Visitor visitor) {
        //TODO: Implement further logic here
        visitor.visit(this);
    }
}
