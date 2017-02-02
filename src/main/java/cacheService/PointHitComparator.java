package cacheService;
import java.util.Comparator;

public class PointHitComparator implements Comparator<PointHit> {
    @Override
    public int compare(PointHit o1, PointHit o2) {
    	return Float.compare(o1.getM(), o2.getM());
    }
}