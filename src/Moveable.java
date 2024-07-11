package src;

/** Used to describe a moveable object, such as blocks in tetris game*/
public interface Moveable {

    //object might move automatically
    public abstract void autoMove();

    //objects can move to the left
    public abstract void left();

    //objects can move to the right
    public abstract void right();

    //objects can rotate to different directions.
    public abstract void rotate();

    //Allow objects to fall faster to the ground;
    public abstract void drop();

    //set the concrete movement behaviour
    public abstract void move();

}
