package titan.lightbatis.dataset;

public interface DataQuery {

    public DataRow findBy(String column, Object value);

}
