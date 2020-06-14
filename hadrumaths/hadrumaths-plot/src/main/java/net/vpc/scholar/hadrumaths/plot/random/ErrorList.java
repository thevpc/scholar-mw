package net.vpc.scholar.hadrumaths.plot.random;

import net.vpc.common.io.IOUtils;
import net.vpc.common.tson.*;
import net.vpc.scholar.hadrumaths.Expr;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ErrorList {
    private long opErrorCount;
    private long opTotalCount;
    private LinkedHashMap<Class, List<ErrorGroup>> messages = new LinkedHashMap<>();

    public void writeSummary() {
        ErrWarnCount c = count();
        System.err.println("--------------------------------------------");
        System.err.printf("%10d erroneous items, %10d total items count%n", opErrorCount, opTotalCount);
        System.err.printf("%10d warnings, %10d errors%n", c.w, c.e);
    }

    public ErrWarnCount count() {
        int w = 0;
        int e = 0;
        for (List<ErrorGroup> objWithErrorsList : messages.values()) {
            for (ErrorGroup objWithErrors : objWithErrorsList) {
                for (ErrorMessage message : objWithErrors.messages) {
                    if (message.isWarning()) {
                        w++;
                    } else {
                        e++;
                    }
                }
            }
        }
        return new ErrWarnCount(w, e);
    }

    public void addError(Class cls, String message) {
        addError(new ErrorMessage(null, false, cls, null, new RuntimeException(message)));
    }

    public void addError(ErrorMessage e) {
        System.err.println(e);
        if (e.obj != null) {
            System.err.println("\t" + e.obj);
        }
        List<ErrorGroup> p = messages.get(e.getClazz());
        if (p == null) {
            p = new ArrayList<>();
            messages.put(e.getClazz(), p);
        }
        ErrorGroup objWithErrorsOk = null;
        for (ErrorGroup objWithErrors : p) {
            if (objWithErrors.obj == e.obj) {
                objWithErrorsOk = objWithErrors;
                break;
            }
        }
        if (objWithErrorsOk == null) {
            objWithErrorsOk = new ErrorGroup(null, e.clazz, e.obj);
            p.add(objWithErrorsOk);
        }
        objWithErrorsOk.messages.add(e);
    }

    public void addError(Class cls, Throwable message) {
        addError(new ErrorMessage(null, false, cls, null, message));
    }

    public void addWarning(Class cls, String message) {
        addError(new ErrorMessage(null, true, cls, null, new RuntimeException(message)));
    }

    public void addInstanceError(Object instance, String th) {
        addInstanceError(instance, new RuntimeException(th));
    }

    public void addInstanceError(Object instance, Throwable th) {
        addError(new ErrorMessage(null, false, instance.getClass(), instance, th));
    }

    public void reset() {
        messages.clear();
    }

    public boolean isValid() {
        return count().e == 0;
    }

    public boolean isPerfect() {
        ErrWarnCount c = count();
        return c.e == 0 && c.w == 0;
    }

    public void throwErrors() {
        ErrWarnCount c = count();
        System.err.println("--------------------------------------------");
        System.err.printf("%10d warnings, %10d errors%n", c.w, c.e);
        if (c.e > 0) {
            for (List<ErrorGroup> objWithErrorsList : messages.values()) {
                for (ErrorGroup objWithErrors : objWithErrorsList) {
                    for (ErrorMessage message : objWithErrors.messages) {
                        if (!message.isWarning()) {
                            throw new RuntimeException(message.toString());
                        }
                    }
                }
            }
        }
    }

    public void save() {
        File testConfigFolder = getTestConfigFolder();
        testConfigFolder.mkdirs();
        for (List<ErrorGroup> objWithErrorsList : messages.values()) {
            for (ErrorGroup objWithErrors : objWithErrorsList) {
                Class<?> cls = objWithErrors.getClazz();
                String id = nextErrorName(cls.getSimpleName());
                File errorStr = new File(testConfigFolder, id + ".error-string");
                File errorObj = new File(testConfigFolder, id + ".error-obj");
                TsonObjectBuilder tsonObj = Tson.obj()
                        .add("id", Tson.elem(id))
                        .add("type", Tson.elem(cls.getName()))
                        .add("value", Tson.elem(String.valueOf(objWithErrors.obj)));
                TsonArrayBuilder array = Tson.array();
                for (ErrorMessage message : objWithErrors.messages) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    message.message.printStackTrace(pw);
                    array.add(
                            Tson.obj()
                                    .add("id", Tson.elem(message.id))
                                    .add("warning", Tson.elem(message.warning))
                                    .add("message", Tson.elem(message.message.toString()))
                                    .add("stacktrace", Tson.elem(sw.toString()))
                    );
                }
                tsonObj.add("messages", array);
                try {
                    Tson.writer().write(errorStr, tsonObj);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                IOUtils.saveObject2(errorObj.getPath(), objWithErrors.obj);
            }
        }
    }

    static protected File getTestConfigFolder() {
        return new File(System.getProperty("user.home"), ".config" + File.separator + "hadrumaths" + File.separator + "test");
    }

    private static String nextErrorName(String name) {
        int i = 1;
        while (i <= 1000) {
            File errorStr = new File(getTestConfigFolder(), name + "-" + i + ".error-string");
            File errorObj = new File(getTestConfigFolder(), name + "-" + i + ".error-obj");
            if (!errorStr.exists() && !errorObj.exists()) {
                return name + "-" + i;
            }
            i++;
        }
        throw new RuntimeException("Too Many Errors for " + name);
    }

    public Iterable<ErrorGroup> load() {
        return new Iterable<ErrorGroup>() {
            @Override
            public Iterator<ErrorGroup> iterator() {
                File testConfigFolder = getTestConfigFolder();
                File[] p = testConfigFolder.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".error-obj");
                    }
                });
                if (p == null) {
                    return Collections.emptyIterator();
                }
                Iterator<File> filesIterator = Arrays.asList(p).iterator();
                return new Iterator<ErrorGroup>() {
                    ErrorGroup e;

                    @Override
                    public boolean hasNext() {
                        e = null;
                        while (filesIterator.hasNext()) {
                            e = null;
                            File errorObj = filesIterator.next();
                            String name = errorObj.getName().substring(0, errorObj.getName().length() - ".error-obj".length());
                            File errorStr = new File(errorObj.getParentFile(), name + ".error-string");
                            try {
                                Expr expr = (Expr) IOUtils.loadObject2(errorObj.getPath());
                                String str = errorStr.exists() ? new String(Files.readAllBytes(errorStr.toPath())) : "";
                                TsonObject tson = Tson.reader().readElement(str).toObject();
                                Class<?> type = Class.forName(tson.get("type").getString());
                                e = new ErrorGroup(
                                        name,
                                        type,
                                        expr);
                                for (TsonElement me : tson.get("messages").toArray()) {
                                    TsonObject m = me.toObject();
                                    e.messages.add(
                                            new ErrorMessage(
                                                    name,
                                                    m.get("warning").getBoolean(),
                                                    type,
                                                    expr,
                                                    new RuntimeException(m.get("message").toStr().getString())
                                            )
                                    );
                                }
                                break;
                            } catch (Exception ex) {
                                System.err.println("Error loading " + errorObj.getName() + ". Discarded!");
                                ex.printStackTrace();
                                errorObj.delete();
                                errorStr.delete();
                            }
                        }
                        return e != null;
                    }

                    @Override
                    public ErrorGroup next() {
                        return e;
                    }
                };
            }
        };
    }

    public long getOpErrorCount() {
        return opErrorCount;
    }

    public ErrorList setOpErrorCount(long opErrorCount) {
        this.opErrorCount = opErrorCount;
        return this;
    }

    public long getOpTotalCount() {
        return opTotalCount;
    }

    public ErrorList setOpTotalCount(long opTotalCount) {
        this.opTotalCount = opTotalCount;
        return this;
    }

    public ErrorList addOpErrorCount(long opErrorCount) {
        this.opErrorCount += opErrorCount;
        return this;
    }

    public ErrorList addOpTotalCount(long opTotalCount) {
        this.opTotalCount += opTotalCount;
        return this;
    }

    public static class ErrorMessage {
        public String id;
        private boolean warning;
        private Class clazz;
        private Object obj;
        private Throwable message;

        public ErrorMessage(String id, boolean warning, Class clazz, Object obj, Throwable message) {
            this.id = id;
            this.clazz = clazz;
            this.obj = obj;
            this.warning = warning;
            this.message = message;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (warning) {
                sb.append("[WARNING] ");
            } else {
                sb.append("[ERROR  ] ");
            }
            sb.append(String.format("%-30s", clazz.getSimpleName()));
            sb.append(" ");
            switch (message.getClass().getName()) {
                case "java.lang.StringIndexOutOfBoundsException":
                case "java.lang.IndexOutOfBoundsException":
                case "java.lang.ArrayIndexOutOfBoundsException":
                case "java.lang.NullPointerException": {
                    sb.append(message.toString());
                    break;
                }
                case "java.lang.RuntimeException":
                case "net.vpc.scholar.hadrumaths.random.RandomObjectGenerator$GenerateException": {
                    sb.append(message.getMessage());
                    break;
                }
                default: {
                    sb.append(message.getMessage());
                }
            }
            if (false) {
                try {
                    sb.append("\n");
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    message.printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                    // ignore
                }
            }
            return sb.toString();
        }

        public boolean isWarning() {
            return warning;
        }

        public Class getClazz() {
            return clazz;
        }

        public Throwable getMessage() {
            return message;
        }
    }

    static class ErrorGroup {
        private String id;
        private Class clazz;
        private Object obj;
        private List<ErrorMessage> messages = new ArrayList<>();

        public ErrorGroup(String id, Class clazz, Object obj) {
            this.id = id;
            this.clazz = clazz;
            this.obj = obj;
        }

        public Class getClazz() {
            return clazz;
        }

        public Object getObj() {
            return obj;
        }

        public List<ErrorMessage> getMessages() {
            return messages;
        }

        public void delete() {
            File errorStr = new File(getTestConfigFolder(), id + ".error-string");
            File errorObj = new File(getTestConfigFolder(), id + ".error-obj");
            if (errorStr.exists()) {
                errorStr.delete();
            }
            if (errorStr.exists()) {
                errorObj.delete();
            }
        }
    }

    private class ErrWarnCount {
        private int w;
        private int e;

        public ErrWarnCount(int w, int e) {
            this.w = w;
            this.e = e;
        }

        public int getW() {
            return w;
        }

        public int getE() {
            return e;
        }
    }
}
