package pl.put.poznan.checker.logic;

public interface Visitable {
    void accept(Visitor visitor);
}
