package net.vpc.scholar.hadruwaves.mom.project.common;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 8 juin 2004
 * Time: 01:13:23
 * To change this template use File | Settings | File Templates.
 */
public class VariableExpression implements Comparable,Cloneable {
    private String name;
    private String expression;
    private String desc;
    private VarUnit unit;

    public VariableExpression(String name, String expression, VarUnit unit,String desc) {
        this.name = name;
        this.expression = expression;
        this.unit = unit;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int compareTo(Object o) {
        if (!(o instanceof VariableExpression)) {
            return -1;
        }
        VariableExpression v = (VariableExpression) o;
        int x = name.compareTo(v.name);
        if (x != 0) {
            return x;
        }
//        x=expression.compareTo(v.expression);
//        if(x!=0){
//            return x;
//        }
//        x=expression.compareTo(v.expression);
//        if(x!=0){
//            return x;
//        }
        return 0;
    }

    public VarUnit getUnit() {
        return unit;
    }

    public void setUnit(VarUnit unit) {
        this.unit = unit;
    }
    

    @Override
    public VariableExpression clone(){
        try {
            return (VariableExpression)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
