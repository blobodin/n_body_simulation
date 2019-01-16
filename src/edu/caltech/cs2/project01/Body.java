package edu.caltech.cs2.project01;
import edu.caltech.cs2.libraries.IBody;
import edu.caltech.cs2.libraries.Vector2D;
import static java.lang.Math.*;

public class Body implements IBody<Body> {
  private final double mass;
  private final String filename;
  private Vector2D position;
  private Vector2D velocity;
  private Vector2D force;
  private static final double G = (.0000000000667);

  public Body(Vector2D position, Vector2D velocity, double mass, String filename) {
    this.position = position;
    this.velocity = velocity;
    this.mass = mass;
    this.filename = filename;
    this.force = new Vector2D(0, 0);


  }

  public Body(double xPos, double yPos, double xVel, double yVel, double mass, String filename) {
    this(new Vector2D(xPos, yPos), new Vector2D(xVel, yVel), mass, filename);

  }

  public Body(double xPos, double yPos, double xVel, double yVel) {
    this(new Vector2D(xPos, yPos), new Vector2D(xVel, yVel), 0.0, null);
  }

  private Vector2D getForceFrom(Body other) {
    double r = distanceTo(other);
    double F = (G * this.mass * other.mass) / (pow(r, 2));
    double cos = (other.position.getX() - this.position.getX()) / r;
    double sin = (other.position.getY() - this.position.getY()) / r;
    double x = (F * cos);
    double y = (F * sin);
    Vector2D force = new Vector2D(x, y);
    return force;

  }

  @Override
  public void calculateNewForceFrom(Body[] bodies) {
    double net_force_x = 0.0;
    double net_force_y = 0.0;
    Vector2D net = new Vector2D(0, 0);
    for (Body b : bodies) {
      if (b != this) {
        net = this.getForceFrom(b);
        net_force_x += net.getX();
        net_force_y += net.getY();
      }
    }
    this.force = new Vector2D(net_force_x, net_force_y);

  }

  @Override
  public void updatePosition(double dt) {
    double xAcc = this.force.getX() / this.mass;
    double yAcc = this.force.getY() / this.mass;
    this.velocity = new Vector2D(
            Double.valueOf(this.velocity.getX()) +
                    Double.valueOf((dt * xAcc)),
            Double.valueOf(this.velocity.getY()) +
                    Double.valueOf((dt * yAcc)));
    this.position = new Vector2D(
            Double.valueOf(this.position.getX()) +
                    Double.valueOf(dt * this.velocity.getX()),
            Double.valueOf(this.position.getY()) +
                    Double.valueOf(dt * this.velocity.getY()));
  }

  @Override
  public Vector2D getCurrentPosition() {

    return this.position;
  }

  @Override
  public String getFileName() {
    return this.filename;
  }

  private double distanceTo(Body other) {
    double distance = sqrt(pow((this.position.getX() - other.position.getX()), 2)
            + pow((this.position.getY() - other.position.getY()), 2));
    return distance;
  }

  public String toString() {
    Vector2D position = this.getCurrentPosition();
    return String.format("%11.4e %11.4e %11.4e %11.4e %11.4e %12s ", position.getX(), position.getY(),
            this.velocity.getX(), this.velocity.getY(), this.mass, this.getFileName()).stripTrailing();
  }
}
