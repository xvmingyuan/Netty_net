package decorator;

public class Client {
    public static void main(String[] args) {

        Component component = new ConcreteComponent2(new ConcreteComponent1(
                new ConcreteComponent()));

        component.doSomething();
    }
}
