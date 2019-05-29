package decorator;

public class ConcreteComponent2 extends Decorator {

    public ConcreteComponent2(Component component) {
        super(component);
    }

    @Override
    public void doSomething() {
        super.doSomething();
        this.doAnotherThing();
    }

    private void doAnotherThing() {
        System.out.println("功能C");
    }

}