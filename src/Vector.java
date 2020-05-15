public class Vector {

    double x;
    double y;
    private Double magnitude;


    public Vector(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public void add(Vector other) {
        this.magnitude=null;
        this.x = this.x + other.x;
        this.y = this.y + other.y;
    }

    public void scale(double factor) {
        this.magnitude=null;
        this.x = this.x * factor;
        this.y = this.y * factor;
    }

    public Double getMagnitude() {
        if(magnitude==null) magnitude = Math.sqrt((Math.pow(x,2)+Math.pow(y,2)));
        return magnitude;
    }

    public static Vector rotate(double angle, Vector vector) {
        Vector result = new Vector(vector.x, vector.y);
        double x1 = vector.x;
        double y1 = vector.y;
        result.x = Math.cos(Math.toRadians(angle)) * x1 - Math.sin(Math.toRadians(angle)) * y1;
        result.y = Math.sin(Math.toRadians(angle)) * x1 + Math.cos(Math.toRadians(angle)) * y1;
        return result;
    }
}
