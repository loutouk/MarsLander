public class SpaceShuttle extends PhysicObject {

    public static final int MAX_ANLGE_CHANGE = 15;
    public static final int MAX_THRUST_CHANGE = 1;
    public static final int MAX_THRUST_VALUE = 4;
    public static final int MAX_LANDING_VSPEED = 40;
    public static final int MAX_LANDING_HSPEED = 20;
    public static final int MIN_LANDING_WIDTH = 1000;

    double fuel = 2000.0;
    int angle = 0;
    double lastThrust = 0.0;

    public SpaceShuttle(double mass, Vector position) {
        super(mass, position);
    }

    public void rotate(int angle){
        this.angle = this.angle + angle;
    }

    public void thrust(int power){
        fuel-=power;
        Vector acceleration = new Vector(0,-power);
        acceleration = Vector.rotate((double)angle, acceleration);
        netForce.add(acceleration);
        lastThrust = power;
    }


}
