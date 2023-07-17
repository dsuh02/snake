
public class Coordinate
{
    private int r,c;
    public Coordinate(int r, int c)
        {
        this.c=c;;
        this.r=r;
        }
   public int getR()
        {
        return r;
        }
    public int getC()
        {
          return c;
        }
    public boolean equals(Coordinate a)
    {
        if(getR()==a.getR() && getC()==a.getC()) return true;
        return false;
    }
    }        

