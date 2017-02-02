package cacheService;


public class PointHit{

    private float hit;
    private float m;
    
    public PointHit(float m, float hit){
        this.m=m;
        this.hit=hit;
    }

    void setHit(float hit){
        this.hit=hit;
    }

    void setM(float m){
        this.m=m;
    }

    float getHit(){
        return hit;
    }

    float getM(){
        return m;
    }
}