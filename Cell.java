import javax.swing.*;
import java.awt.*;

public class Cell
{

    private Coordinate coord;
    private Color color;
    public Cell(Coordinate coord, Color color)
    {
        this.coord=coord;
        this.color=color;

    }

    public Coordinate getCoord(){
        return coord;
    }
    public void setColor(Color color){
        this.color=color;
    }
    public Color getColor(){
        return color;
    }
}


