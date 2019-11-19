package titan.lightbatis.sample.domain;

import titan.lightbatis.sample.annotations.Operator;

public enum Paths implements IPath{
    id("id",Long.class)
    ;

    String property = null;
    Class<?> type = null;

    Paths(String property, Class<?> type) {
        this.property = property;
        this.type = type;
    }
    @Override
    public String getProperty() {
        return null;
    }

    @Override
    public Class<?> getType() {
        return null;
    }

    @Override
    public <T extends Operator> T getOperator() {
        return null;
    }
}

class APath<K extends Operator> implements IPath {

    @Override
    public String getProperty() {
        return null;
    }

    @Override
    public Class<?> getType() {
        return null;
    }

    @Override
    public K getOperator() {
        return null;
    }

    public static void main(String[] args) {
        APath<NumberOperator> a = new APath<>();

    }
}

