public class movableObject {
    private int objectX;
    private int objectY;
    private int iterationOfMove;
    private int speed;
    private long last_frame_time;
    float delta_time;
    float x;

    public movableObject(int objectX, int objectY, int speed){
        this.objectX = objectX;
        this.objectY = objectY;
        iterationOfMove = 0;
        this.speed = speed;
        last_frame_time = System.nanoTime();
    }

    public void setIterationOfMove(int iterationOfMove) {
        this.iterationOfMove = iterationOfMove;
    }

    public int getObjectX(){
        return objectX;
    }

    public int getObjectY() {
        return objectY;
    }

    public int getIterationOfMove() {
        return iterationOfMove;
    }

    public void incIterationOfMove(){
        iterationOfMove++;
    }

    public void moveLeft(){
        long current_system_time = System.nanoTime();
        delta_time = ((current_system_time - last_frame_time)  * 0.000001f);
        last_frame_time = current_system_time;
        objectX = (int) (objectX - (delta_time * speed));
    }


}

