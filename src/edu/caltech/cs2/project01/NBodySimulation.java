package edu.caltech.cs2.project01;
import edu.caltech.cs2.libraries.IBody;
import edu.caltech.cs2.libraries.StdDraw;
import edu.caltech.cs2.libraries.Vector2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBodySimulation {

    public static void main(String[] args) throws FileNotFoundException {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String file= args[2];
        Scanner in = new Scanner(new File(file));
        int numBodies = 0;
        int j = 0;
        double radius = 0;
        while (in.hasNextLine()) {
            if (j == 0) {
                numBodies = Integer.parseInt(in.nextLine());
            }
            if (j == 1) {
                radius = Double.parseDouble(in.nextLine());
            }
            j++;
            if (j > 2) {
                break;
            }
        }

        Body[] bodies = readBodies(in, numBodies);
        setupDrawing(radius);
        for (double t = 0.0; t < T; t += dt) {
            drawStep(bodies);
            calculateStep(bodies, dt);
        }
        printState(radius, bodies);
    }

    public static Body[] readBodies(Scanner in, int numBodies) {
        Body[] bodies = new Body[numBodies];
        if (numBodies == 0){
            return bodies;
        }
        int i = 0;
        while(in.hasNextLine()) {
            int j = 0;
            String line = "";
            while(in.hasNext()){
                line += in.next() + " ";
                if (j == 5){
                    break;
                }
                j++;
            }
            String[] parameters = line.trim().split(" ");
            if(parameters.length == 6){
                Body body = new Body(Double.parseDouble(parameters[0]),
                        Double.parseDouble(parameters[1]),
                        Double.parseDouble(parameters[2]),
                        Double.parseDouble(parameters[3]),
                        Double.parseDouble(parameters[4]),
                        parameters[5]);
                bodies[i] = body;

            }
            i++;
            if (i == numBodies) {
                break;
            }
        }
        return bodies;
    }

    public static void calculateStep(Body[] bodies, double dt) {
        for (int i = 0; i < bodies.length; i ++){
            bodies[i].calculateNewForceFrom(bodies);
        }
        for (int i = 0; i < bodies.length; i++) {
            bodies[i].updatePosition(dt);
        }
    }
    public static void setupDrawing(double radius) {
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
    }

    public static void drawStep(IBody[] bodies) {
        StdDraw.picture(0, 0, "data/images/starfield.jpg");
        for (int i = 0; i < bodies.length; i++) {
            double[] coordinates = new double[2];
            Vector2D vector = bodies[i].getCurrentPosition();
            coordinates[0] = vector.getX();
            coordinates[1] = vector.getY();
            String filename = "data/images/" + bodies[i].getFileName();
            StdDraw.picture(coordinates[0], coordinates[1], filename);
        }
        StdDraw.show();
    }

    public static void printState(double radius, IBody[] bodies){
        System.out.printf("%d" + "\n" + "%.2e".strip() + "\n", bodies.length, radius);
        for (int i = 0; i < bodies.length; i++) {
            if (i == bodies.length-1){
                System.out.print(bodies[i].toString());
            }
            else {
                System.out.print(bodies[i].toString() + "\n");
            }
        }
    }
}