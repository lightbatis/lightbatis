package titan.lightbatis.jsv.engine;


import com.google.common.collect.Range;

public class V8Functions {

    /**
     * [a..b)  {x | a <= x < b}    closedOpen
     * @param a
     * @param b
     * @param x
     * @return
     */
    public boolean rangeCloseOpen(Integer a, Integer b, Integer x) {
        Range<Integer> range = Range.closedOpen(a, b);
        return range.contains(x);
    }
    public boolean rangeCloseOpen(Double a, Double b, Double x) {
        Range<Double> range = Range.closedOpen(a, b);
        return range.contains(x);
    }

    public boolean rangeCloseOpen(Float a, Float b, Float x) {
        Range<Float> range = Range.closedOpen(a, b);
        return range.contains(x);
    }
}
