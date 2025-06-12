package bcc.javaJostle;

import java.util.ArrayList;

public class MyRobot extends Robot{

    int debugTick = 0; // Timer to release debug messages
    //int movementStage = 0;
    int movementTicks = 0;
    //int movementMode = 1;
    int movementStage = 0;

    int oldX = 0;
    int oldY = 0;

    int[] enemyXOld = new int[16];
    int[] enemyXNew = new int[16];

    int[] enemyYOld = new int[16];
    int[] enemyYNew = new int[16];

    public MyRobot(int x, int y){
        super(x, y, 1, 1, 3, 5,"bob", "robotError.png", "defaultProjectileImage.png");
        // Health: 1, Speed: 3, Attack Speed: 1, Projectile Strength: 5
        // Total = 10
        // Image name is "myRobotImage.png"
    }

    public void think(ArrayList<Robot> robots, ArrayList<Projectile> projectiles, Map map, ArrayList<PowerUp> powerups){
       /* Implement your robot's logic here
         For example, you can move towards the nearest robot or shoot at a target
         to move, choose a direciton to go
         to move left - use xMovement = -1
         to move right - use xMovement = 1
         to move up - use yMovement = -1
         to move down - use yMovement = 1
         You can ONLY move in one direction at a time, if your output doesn't match the above you will get an error

         to shoot, use shootAtLocation(x, y) where x and y are the coordinates of the target
         only shoot when canAttack() is true!
        */
       // System.out.println("Thinking...");
        
        /* Pseudocode:

            gets distance from a robot using the getDistance() method

            Movement: check if the closest robot is further away than a set distance
                true:    
                    move closer using the pathfinding algorithm if the robot is further away than 3 spaces
                false:
                    strafe to dodge bullets if there are no walls in its path

            check if the robot is able to shoot
                check if there are any robots alive other than this
                    get the position and velocity of the robot
                    shoot at the predicted position of the enemy by adding the current position to the velocity times the distance times the bullet speed
        */

       // Variables

        debugTick ++;

        // Enemy distance calcuations
        double minDistance = Integer.MAX_VALUE;
        int closestBot = -1;

        for (int i = 0; i < robots.size(); i ++){ // Checks for the current closest robot that is not this
            if (getDistance(robots.get(i)) < minDistance && !robots.get(i).equals(this) && robots.get(i).isAlive()){
                minDistance = getDistance(robots.get(i));
                closestBot = i;
            }
        }

        // Enemy velocity calculations

        int[] enemyXVel = new int[16];
        int[] enemyYVel = new int[16];

        for (int i = 0; i < robots.size(); i ++){ // Replaces the old and new x and y values to use to estimate dx/dt and dy/dt
            enemyXOld[i] = enemyXNew[i];
            enemyYOld[i] = enemyYNew[i];

            enemyXNew[i] = robots.get(i).getX();
            enemyYNew[i] = robots.get(i).getY();
        }

        for (int i = 0; i < robots.size(); i ++){ // Estimates enemy velocity by using 2 measured positions
            enemyXVel[i] = enemyXNew[i] - enemyXOld[i];
            enemyYVel[i] = enemyYNew[i] - enemyYOld[i];
        }

        // Movement (Outer Circle)
        /*

        if (enemyXVel[closestBot] == 0 && enemyYVel[closestBot] == 0){
            movementMode = 0;l
        }
        else {
            movementMode = 1;
        }

        if (movementMode == 1){
            if (movementStage < 1){
                xMovement = 1;
            }
            else if (movementStage < 2){
                xMovement = -1;
            }
            else if (movementStage < 3){
                xMovement = 1;
            }
            else {
                xMovement = -1;
            }
            if (oldX == super.getX() && oldY == super.getY()){
                if (movementStage == 3){
                    movementStage = 0;
                }
                else {
                    movementStage ++;
            }
        }
        }
        else {
            if (movementStage < 1){
                xMovement = 1;
            }
            else if (movementStage < 2){
                yMovement = 1;
            }
            else if (movementStage < 3){
                xMovement = -1;
            }
            else {
                yMovement = -1;
            }
            if (oldX == super.getX() && oldY == super.getY() && (movementTicks % 2 == 0)){
                if (movementStage == 3){
                    movementStage = 0;
                }
                else {
                    movementStage ++;
                }
        }
        }
        */
        

        // Movement (Circle)
        
        if (movementStage == 0){
            xMovement = 1;
        }
        else if (movementStage == 1){
            yMovement = 1;
        }
        else if (movementStage == 2){
            xMovement = -1;
        }
        else {
            yMovement = -1;
        }

        movementTicks ++;

        if (movementTicks > 100){
            movementTicks = 0;
            if (movementStage == 3){
                movementStage = 0;
            }
            else {
                movementStage ++;
            }
        }
        

        // Movement (DeltaX and Y)
        /*
        int randomOffset = (int)((Math.random() * 40) + 10);

        if (Math.random() > 0.5){
            if (Math.abs(robots.get(closestBot).getX() - this.getX()) > 150){
                if (robots.get(closestBot).getX() + randomOffset > this.getX()){
                    this.xMovement = 1;
                }
                else if (robots.get(closestBot).getX() + randomOffset < this.getX()){
                    this.xMovement = -1;
                }
            }

        }
        else {
            if (Math.abs(robots.get(closestBot).getY() - this.getY()) > 150){
                if (robots.get(closestBot).getY() + randomOffset > this.getY()){
                    this.yMovement = 1;
                }
                else if (robots.get(closestBot).getY() + randomOffset < this.getY()){
                    this.yMovement = -1;
                }
            }
        }
        */


        // Movement (Djikstra's Algo) (unfinished)

        /*
        if (minDistance < 32 * 5){
            boolean[][] visitedSpaces = new boolean[24][18];
            int[][] distance = new int[24][18];
        
            for (int row = 0; row < 24; row ++){ // Sets all rows to the max distance
                for (int col = 0; col < 18; col ++){
                    distance[row][col] = Integer.MAX_VALUE;
                }
            }
        
            for (int row = 0; row < 24; row ++){
                for (int col = 0; col < 18; col ++){
                    if (map.getTiles()[row][col] != 1){
                        visitedSpaces[row][col] = true;
                    }
                }
            }
        }
        */

        // Aiming
        if (canAttack()){
            if (debugTick > 29){
                System.out.println("attempting to shoot at nearest robot");
            }

            if (closestBot >= 0){
                shootAtLocation(leadingCoords(robots.get(closestBot).getX(), enemyXVel[closestBot], getDistance(robots.get(closestBot))), leadingCoords((robots.get(closestBot).getY()), enemyYVel[closestBot], getDistance(robots.get(closestBot))));
            }
        }

        if (debugTick > 29){ // Resets debug ticks every 0.5 seconds (?)
            System.out.println("Closest bot index: " + closestBot);
            System.out.println("Closest bot distance: " + getDistance(robots.get(closestBot)));
            System.out.println("Closest bot x velocity: " + enemyXVel[closestBot]);
            System.out.println("Closest bot y velocity: " + enemyYVel[closestBot]);

            System.out.println("Closest bot x position: " + enemyXNew[closestBot]);
            System.out.println("Closest bot y position: " + enemyYNew[closestBot]);
            debugTick = 0;
        }

        oldX = super.getX(); // Updates old x value
        oldY = super.getY(); // Updates old y value

        movementTicks ++;
    }

    // Returns the distance between the player and another robot
    public double getDistance(Robot robot){
        double distance = 0;

        distance = Math.sqrt(Math.pow((double)(robot.getX() - this.getX()), 2) + Math.pow((double)(robot.getY() - this.getY()), 2)); // compares the x and y values of both robots
        
        return distance;
    }

    // Returns the predicted position of a robot given a position, velocity, and distance
    public int leadingCoords(int currPos, int vel, double dist){
        int predictedPos;

        predictedPos = (int)(currPos + (vel * (dist / 15))); // Predicts the robot's position given its current velocity

        return predictedPos;
    }
}
