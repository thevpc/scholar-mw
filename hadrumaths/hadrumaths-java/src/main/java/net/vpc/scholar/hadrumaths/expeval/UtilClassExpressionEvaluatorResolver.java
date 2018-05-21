package net.vpc.scholar.hadrumaths.expeval;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class UtilClassExpressionEvaluatorResolver implements ExpressionEvaluatorResolver {
    private List<Class> importedTypes = new ArrayList<>();
    private List<Class> importedMethods = new ArrayList<>();
    private List<Class> importedFields = new ArrayList<>();
    private boolean importsFirst = false;

    public UtilClassExpressionEvaluatorResolver(boolean importsFirst, Class... types) {
        this.importsFirst = importsFirst;
        importedTypes.addAll(Arrays.asList(types));
    }

    public void addImportFields(Class type){
        importedFields.add(type);
    }
    public void addImportMethods(Class type){
        importedMethods.add(type);
    }

    @Override
    public ExpressionNode resolveVar(String name, boolean checkEmptyArgMethods) {
        LinkedHashSet<Class> t=new LinkedHashSet<>(importedTypes);
        t.addAll(importedFields);
        //check for fields
        for (Class type : t) {
            Field field = null;
            try {
                field = type.getField(name);
            } catch (Exception ex) {
                //
            }
            if (field != null) {
                return new FieldExpressionNode(field);
            }
        }
        if (checkEmptyArgMethods) {
            return resolveFunction(name, new Class[0]);
        }
        throw new IllegalArgumentException("Var " + name + " Not Found");
    }

    @Override
    public ExpressionNode resolveFunction(String name, Class[] args) {
        switch (name) {
            case "+": {
                if (args.length == 1) {
                    //unary
                    Class arg = args[0];
                    if (Number.class.isAssignableFrom(arg)) {
                        return new IdentityUnaryOp(name, arg);
                    }
                    return new IdentityUnaryOp(name, arg);
                } else if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "add", "radd", "add");
                }
                break;
            }
            case "-": {
                if (args.length == 1) {
                    //unary
                    Class arg = args[0];
                    ExpressionNode m = getUnaryExpressionNode(name, args, arg, "neg", "neg");
                    if (m != null) return m;
                } else if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "sub", "rsub", "sub");
                }
                break;
            }
            case "*": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "mul", "rmul", "mul");
                }
                break;
            }
            case "/": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "div", "rdiv", "div");
                }
                break;
            }
            case "^": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "xor", "rxor", "xor");
                }
                break;
            }
            case "~": {
                if (args.length == 1) {
                    //unary
                    Class arg = args[0];
                    ExpressionNode m = getUnaryExpressionNode(name, args, arg, "tilde", "tilde");
                    if (m != null) return m;
                } else if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "power", "rpower", "power");
                }
                break;
            }
            case "**": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "scalarProduct", "rscalarProduct", "scalarProduct");
                }
                break;
            }
            case "->": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, "rightArrow", "rrightArrow", "rightArrow");
                }
                break;
            }
            default: {
                for (Class type : importedTypes) {
                    Method m = MethodUtils.getMatchingAccessibleMethod(type, name, args);
                    if (m != null) {
                        return new ExpressionFunction(name, m.getReturnType()) {
                            @Override
                            public Object evaluate(Object[] args, ExpressionEvaluatorContext context) {
                                try {
                                    return m.invoke(type, args);
                                } catch (IllegalAccessException e) {
                                    throw new IllegalArgumentException(e);
                                } catch (InvocationTargetException e) {
                                    throw new IllegalArgumentException(e.getTargetException());
                                }
                            }
                        };
                    }
                }
            }
        }
        throw new IllegalArgumentException("Unsupported " + name + "" + Arrays.toString(args));
    }

    private ExpressionNode getUnaryExpressionNode(String name, Class[] args, Class arg, String directName, String staticName) {
        Class arg1 = args[0];
        int[] order = !(importsFirst) ? new int[]{1, 2} : new int[]{2, 1};
        LinkedHashSet<Class> t=new LinkedHashSet<>(importedTypes);
        t.addAll(importedMethods);

        for (int i = 0; i < order.length; i++) {
            switch (order[i]) {
                case 1: {
                    Method m = MethodUtils.getMatchingAccessibleMethod(arg1, directName);
                    if (m != null) {
                        return new UnaryOp(name, m.getReturnType()) {
                            @Override
                            public Object evaluate(Object a) {
                                try {
                                    return m.invoke(a);
                                } catch (IllegalAccessException e) {
                                    throw new IllegalArgumentException(e);
                                } catch (InvocationTargetException e) {
                                    throw new IllegalArgumentException(e.getTargetException());
                                }
                            }
                        };
                    }
                    break;

                }
                case 2: {
                    for (Class type : t) {
                        Method sm = MethodUtils.getMatchingAccessibleMethod(type, staticName, arg);
                        if (sm != null) {
                            return new UnaryOp(name, sm.getReturnType()) {
                                @Override
                                public Object evaluate(Object a) {
                                    try {
                                        return sm.invoke(type, a);
                                    } catch (IllegalArgumentException e) {
                                        throw e;
                                    } catch (IllegalAccessException e) {
                                        throw new IllegalArgumentException(e);
                                    } catch (InvocationTargetException e) {
                                        throw new IllegalArgumentException(e.getTargetException());
                                    }
                                }
                            };
                        }
                    }
                    break;
                }
            }
        }

        throw new IllegalArgumentException("Not Found " + name + " " + arg1.getSimpleName());
    }

    private ExpressionNode getBinaryExpressionNode(String name, Class[] args, String directName, String
            reverseName, String staticName) {
        Class arg1 = args[0];
        Class arg2 = args[1];
        int[] order = !(importsFirst) ? new int[]{1, 2} : new int[]{2, 1};
        LinkedHashSet<Class> t=new LinkedHashSet<>(importedTypes);
        t.addAll(importedMethods);
        for (int i = 0; i < order.length; i++) {
            switch (order[i]) {
                case 1: {
                    Method m = MethodUtils.getMatchingAccessibleMethod(arg1, directName, arg2);
                    if (m != null) {
                        return new BinaryOp(name, m.getReturnType()) {
                            @Override
                            public Object evaluate(Object a, Object b) {
                                try {
                                    return m.invoke(a, b);
                                } catch (IllegalAccessException e) {
                                    throw new IllegalArgumentException(e);
                                } catch (InvocationTargetException e) {
                                    throw new IllegalArgumentException(e.getTargetException());
                                }
                            }
                        };
                    }

                    Method rm = MethodUtils.getMatchingAccessibleMethod(arg2, reverseName, arg1);
                    if (rm != null) {
                        return new BinaryOp(name, rm.getReturnType()) {
                            @Override
                            public Object evaluate(Object a, Object b) {
                                try {
                                    return rm.invoke(b, a);
                                } catch (IllegalAccessException e) {
                                    throw new IllegalArgumentException(e);
                                } catch (InvocationTargetException e) {
                                    throw new IllegalArgumentException(e.getTargetException());
                                }
                            }
                        };
                    }
                    break;
                }
                case 2: {
                    for (Class type : t) {
                        Method sm = MethodUtils.getMatchingAccessibleMethod(type, staticName, arg1, arg2);
                        if (sm != null) {
                            return new BinaryOp(name, sm.getReturnType()) {
                                @Override
                                public Object evaluate(Object a, Object b) {
                                    try {
                                        return sm.invoke(type, a, b);
                                    } catch (IllegalAccessException e) {
                                        throw new IllegalArgumentException(e);
                                    } catch (InvocationTargetException e) {
                                        throw new IllegalArgumentException(e.getTargetException());
                                    }
                                }
                            };
                        }
                    }
                    break;
                }
            }
        }


        throw new IllegalArgumentException("Not Found " + arg1.getSimpleName() + " " + name + " " + arg2.getSimpleName());
    }

}
