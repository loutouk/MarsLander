import java.util.ArrayList;

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

    public static Vector isLineCrossingOther(Vector a, Vector b, ArrayList<Vector> lines) {
        for(int i=1 ; i<lines.size() ; i++) if(doIntersect(a,b,lines.get(i-1),lines.get(i))) return lines.get(i-1);
        return null;
    }

    // Given three colinear points p, q, r, the function checks if point q lies on line segment 'pr'
    private static boolean onSegment(Vector p, Vector q, Vector r) {
        if (    q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) {
            return true;
        }
        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    private static int orientation(Vector p, Vector q, Vector r) {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        int val = (int)(q.y - p.y) * (int)(r.x - q.x) - (int)(q.x - p.x) * (int)(r.y - q.y);

        if (val == 0) return 0; // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }

    // The main function that returns true if line segment 'p1q1' and 'p2q2' intersect.
    private static boolean doIntersect(Vector p1, Vector q1, Vector p2, Vector q2) {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    public static boolean isPointBelowLine(PhysicObject physObj, ArrayList<Vector> lines) {

        int groundVecBelow=-1;

        for(int i=1 ; i<lines.size() ; i++){
            if(physObj.position.x>=lines.get(i-1).x && physObj.position.x<=lines.get(i).x){
                groundVecBelow = i-1;
                break;
            }
        }

        if(groundVecBelow==-1){
            // System.err.println("No ground has been detected under the shuttle. X:" + physObj.position.x + " Y:" + physObj.position.y);
        }else{
            // flat
            if(lines.get(groundVecBelow).y == lines.get(groundVecBelow+1).y) {
                return physObj.position.y > lines.get(groundVecBelow).y;
            }
            // up hill
            else if(lines.get(groundVecBelow).y > lines.get(groundVecBelow+1).y){
                double yLength = -(lines.get(groundVecBelow+1).y - lines.get(groundVecBelow).y);
                double xLength = lines.get(groundVecBelow+1).x - lines.get(groundVecBelow).x;
                double radianAngle = Math.atan(yLength/xLength);
                double groundHeight = (physObj.position.x - lines.get(groundVecBelow).x) * Math.tan(radianAngle);
                return groundHeight<physObj.position.y;
            }
            // down hill
            else if(lines.get(groundVecBelow).y < lines.get(groundVecBelow+1).y) {
                double yLength = -(lines.get(groundVecBelow).y - lines.get(groundVecBelow+1).y);
                double xLength = lines.get(groundVecBelow+1).x - lines.get(groundVecBelow).x;
                double radianAngle = Math.atan(yLength/xLength);
                double groundHeight = (physObj.position.x - lines.get(groundVecBelow).x) * Math.tan(radianAngle);
                return groundHeight<physObj.position.y;
            }
        }

        return false;
    }
}
