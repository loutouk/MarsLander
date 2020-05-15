public class PhysicObject {

    double mass;
    Vector netForce;
    Vector velocity;
    Vector position;

    public PhysicObject(double mass, Vector position, Vector velocity, Vector netForce) {
        this.mass = mass;
        this.velocity = velocity;
        this.position = position;
        this.netForce = netForce;
    }

    public PhysicObject(double mass, Vector position) {
        this(mass, position, new Vector(0.0,.0), new Vector(0.0,.0));
    }

    public void update(double elapsedTime){
        // a = Fnet / m
        Vector acceleration = new Vector(netForce.x,netForce.y);
        acceleration.scale(1.0/mass);

        // save initial velocity for computing travelled distance later on
        Vector initialVelocity = new Vector(velocity.x, velocity.y);

        // final velocity = initial velocity + a * t
        // we can alter acceleration vector as we will not use it later
        acceleration.scale(elapsedTime);
        velocity.add(acceleration);

        // d = (vi + vf) / 2 * t
        Vector distance = new Vector(initialVelocity.x, initialVelocity.y);
        distance.add(velocity);
        distance.scale(1.0/2.0);
        distance.scale(elapsedTime);

        // update position
        position.add(distance);
    }

}
