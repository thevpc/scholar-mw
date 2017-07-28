package net.vpc.scholar.hadrumaths.plot.console;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 20 janv. 2007 14:33:34
 */
public class PlotChoiceMethod extends PlotChoice {
    private Object[] parameters;
    private Method method;

    public PlotChoiceMethod(PlotConsoleProjectTemplate template, String methodName, Object... parameters) {
        super(bestName(template.getClass(), methodName, parameters));
        this.parameters = parameters;
        this.method = resolveMethod(template.getClass(), methodName, parameters);
    }

    private static java.lang.reflect.Method resolveMethod(Class clz, String methodName, Object[] parameters) {
//        System.out.println("Look after " + methodName);
        for (Method method : clz.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == parameters.length) {
                return method;
            }
        }
        throw new NoSuchElementException("Method " + methodName + " not found in " + clz);
    }

    static String bestName(Class clz, String methodName, Object[] parameters) {
        StringBuilder sb = new StringBuilder();
        java.lang.reflect.Method method1 = resolveMethod(clz, methodName, parameters);
        sb.append(method1.getName());
        if (parameters.length > 0) {
            sb.append("(");
            int i = 0;
            for (Annotation[] annotations : method1.getParameterAnnotations()) {
                Param p = null;
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Param) {
                        p = (Param) annotation;
                        break;
                    }
                }
                if (i > 0) {
                    sb.append(",");
                }
                if (p == null) {
                    sb.append(parameters[i]);
                } else {
                    sb.append(p.value() + "=" + parameters[i]);
                }
                i++;
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public void runStudy(PlotConsoleProjectTemplate template) {
        try {
            method.invoke(template, parameters);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
