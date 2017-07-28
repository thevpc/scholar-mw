package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.util.config.*;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class Configuration
        implements Serializable, Cloneable {

    public static final String FONT_TYPE = "font";
    public static final String COLOR_TYPE = "color";
    public static final String LONG_TYPE = "long";
    public static final String DOUBLE_TYPE = "double";
    public static final String STRING_TYPE = "string";
    public static final String INT_TYPE = "int";
    public static final String SHORT_TYPE = "short";
    public static final String FLOAT_TYPE = "float";
    public static final String TIME_TYPE = "time";
    public static final String DATE_TYPE = "date";
    public static final String CONFIDENTIAL_TYPE = "confidential";
    public static final String OBFUSCATED_TYPE = "obfuscated";
    public static final String BOOLEAN_TYPE = "boolean";
    public static final String LOCALE_TYPE = "locale";
    public static final String TYPE_SUFFIX = "$type";
    public static final String COMMENTS_SUFFIX = "$comments";
    public static final String CONFIGURATION_CHANGED = "CONFIGURATION_CHANGED";
    public static final String USER_CONFIGURATION_CHANGED = "USER_CONFIGURATION_CHANGED";
    private static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static PropertyChangeSupport configChangeSupport = new PropertyChangeSupport("Configuration");
    private static boolean static_persistenceEnabled = true;
    private static Configuration globalConfig;
    private static Configuration userConfig;
    private Map<String, Object> props = new HashMap<String, Object>();
    private Map<String, String> descs = new HashMap<String, String>();
    private Map<String, String> types = new HashMap<String, String>();
    private Map<String, ConfigConverter> typesToConverters = new HashMap<String, ConfigConverter>();
    private Map<Class, ConfigConverter> classesToConverters = new HashMap<Class, ConfigConverter>();
    private ConfigOptions options = new ConfigOptions();

    public Configuration() {

        options.setHelpAsEntry(false);
        options.setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss+SSS"));
        options.setAutoSave(true);
        initConverters();
    }

    public Configuration(Map m) {
        this();
        java.util.Map.Entry e;
        for (Iterator i = m.entrySet().iterator(); i.hasNext(); setProperty((String) e.getKey(), e.getValue())) {
            e = (java.util.Map.Entry) i.next();
        }
        options.setAutoSave(true);
    }

    public Configuration(File file)
            throws IOException, ParseException {
        this();
        options.setFile(file);
        load();
        options.setAutoSave(true);
    }

    public static boolean isLoaded() {
        return globalConfig != null;
    }

    public static void addConfigurationChangeListener(PropertyChangeListener listener) {
        configChangeSupport.addPropertyChangeListener(CONFIGURATION_CHANGED, listener);
    }

    public static void removeConfigurationChangeListener(PropertyChangeListener listener) {
        configChangeSupport.removePropertyChangeListener(CONFIGURATION_CHANGED, listener);
    }

    public static void addUserConfigurationChangeListener(PropertyChangeListener listener) {
        configChangeSupport.addPropertyChangeListener(USER_CONFIGURATION_CHANGED, listener);
    }

    public static void removeUserConfigurationChangeListener(PropertyChangeListener listener) {
        configChangeSupport.removePropertyChangeListener(USER_CONFIGURATION_CHANGED, listener);
    }

    public static boolean isPersistenceEnabled() {
        return static_persistenceEnabled;
    }

    public static void setPersistenceEnabled(boolean e) {
        static_persistenceEnabled = e;
    }

    public static Configuration getConfiguration() {
        if (globalConfig == null) {
            //System.out.println("No Configuration loaded, use defaults");
            globalConfig = new Configuration();
            globalConfig.getOptions().setAutoSave(false);
        }
        return globalConfig;
    }

    public static void setConfiguration(Configuration c) {
        //System.out.println("Setting  Configuration...");
        Configuration old = globalConfig;
        globalConfig = c;
        Locale l = globalConfig.getLocale("java.util.Locale");
        if (l != null) {
            Locale.setDefault(l);
//            Log.trace("Locale set to " + l);
            System.out.println("Locale set to " + l);
        }

        configChangeSupport.firePropertyChange(CONFIGURATION_CHANGED, old, globalConfig);
    }

    public static Configuration getUserConfiguration() {
        if (userConfig == null) {
            return getConfiguration();
//            //System.out.println("No Configuration loaded, use defaults");
//            userConfig = new Configuration();
//            userConfig.getOptions().setAutoSave(true);
        }
        return userConfig;
    }

    public static void setUserConfiguration(Configuration c) {
        //System.out.println("Setting  Configuration...");
        Configuration old = userConfig;
        userConfig = c;
        Locale l = userConfig.getLocale("java.util.Locale");
        if (l != null) {
            Locale.setDefault(l);
//            Log.trace("Locale set to " + l);
            System.out.println("Locale set to " + l);
        }

        configChangeSupport.firePropertyChange(USER_CONFIGURATION_CHANGED, old, userConfig);
    }

    public static void setConfiguration(File f) throws IOException, ParseException {
        setConfiguration(new Configuration(f));
    }

    public static void setUserConfiguration(File f) throws IOException, ParseException {
        setUserConfiguration(new Configuration(f));
    }

    private static boolean isArrayType(String type) {
        return (type.length() > 3 && type.charAt(type.length() - 3) == '[' && type.charAt(type.length() - 1) == ']');
    }

    private static char getArraySeparator(String type) {
        return (type.charAt(type.length() - 2));
    }

    private static String getBaseType(String type) {
        if ((type.length() > 3 && type.charAt(type.length() - 3) == '[' && type.charAt(type.length() - 1) == ']')) {
            type = type.substring(0, type.length() - 3);
        }
        if (type.length() > 1 && type.charAt(type.length() - 1) == '!') {
            type = type.substring(0, type.length() - 1);
        }
        return type;
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (!o1.getClass().isArray()) {
            return o1.equals(o2);
        }
        Class clz = o1.getClass().getComponentType();
        if (clz.isPrimitive()) {
            System.err.println("could not compare primitive arrays");
            return false;
        }
        Object[] o1arr = (Object[]) o1;
        Object[] o2arr = (Object[]) o2;
        if (o1arr.length != o2arr.length) {
            return false;
        } else {
            Collection o1coll = Arrays.asList(o1arr);
            Collection o2coll = Arrays.asList(o2arr);
            return o1coll.equals(o2coll);
        }
    }

    public static String getStringFromFont(Font font) {
        return font.getName() + "," + font.getStyle() + "," + font.getSize();
    }

    public static Font getFontFromString(String font) {
        StringTokenizer st = new StringTokenizer(font, ",");
        st.hasMoreTokens();
        String sn = st.nextToken();
        st.hasMoreTokens();
        String sst = st.nextToken();
        st.hasMoreTokens();
        String ssz = st.nextToken();
        return new Font(sn, Integer.parseInt(sst), Integer.parseInt(ssz));
    }

    public static String getStringFromColor(Color color) {
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    public static Color getColorFromString(String color) {
        StringTokenizer st = new StringTokenizer(color, ", ");
        st.hasMoreTokens();
        String r = st.nextToken();
        st.hasMoreTokens();
        String g = st.nextToken();
        st.hasMoreTokens();
        String b = st.nextToken();
        return new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
    }

    public static String toSpecialString(String theString, boolean escapeSpace) {
        return toSpecialString(theString, "=: \t\r\n\f#!", escapeSpace);
    }

    public static String toSpecialString(String theString, String specialSaveChars, boolean escapeSpace) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len * 2);
        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
                case 32: // ' '
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(' ');
                    break;

                case 92: // '\\'
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;

                case 9: // '\t'
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;

                case 10: // '\n'
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;

                case 13: // '\r'
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;

                case 12: // '\f'
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;

                default:
                    if (aChar < ' ' || aChar > '~') {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex(aChar >> 12 & 0xf));
                        outBuffer.append(toHex(aChar >> 8 & 0xf));
                        outBuffer.append(toHex(aChar >> 4 & 0xf));
                        outBuffer.append(toHex(aChar & 0xf));
                        break;
                    }
                    if (specialSaveChars.indexOf(aChar) != -1) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(aChar);
                    break;
            }
        }

        return outBuffer.toString();
    }

    public static String fromSpecialString(String theString) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            char aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case 48: // '0'
                            case 49: // '1'
                            case 50: // '2'
                            case 51: // '3'
                            case 52: // '4'
                            case 53: // '5'
                            case 54: // '6'
                            case 55: // '7'
                            case 56: // '8'
                            case 57: // '9'
                                value = ((value << 4) + aChar) - 48;
                                break;

                            case 97: // 'a'
                            case 98: // 'b'
                            case 99: // 'c'
                            case 100: // 'd'
                            case 101: // 'e'
                            case 102: // 'f'
                                value = ((value << 4) + 10 + aChar) - 97;
                                break;

                            case 65: // 'A'
                            case 66: // 'B'
                            case 67: // 'C'
                            case 68: // 'D'
                            case 69: // 'E'
                            case 70: // 'F'
                                value = ((value << 4) + 10 + aChar) - 65;
                                break;

                            case 58: // ':'
                            case 59: // ';'
                            case 60: // '<'
                            case 61: // '='
                            case 62: // '>'
                            case 63: // '?'
                            case 64: // '@'
                            case 71: // 'G'
                            case 72: // 'H'
                            case 73: // 'I'
                            case 74: // 'J'
                            case 75: // 'K'
                            case 76: // 'L'
                            case 77: // 'M'
                            case 78: // 'N'
                            case 79: // 'O'
                            case 80: // 'P'
                            case 81: // 'Q'
                            case 82: // 'R'
                            case 83: // 'S'
                            case 84: // 'T'
                            case 85: // 'U'
                            case 86: // 'V'
                            case 87: // 'W'
                            case 88: // 'X'
                            case 89: // 'Y'
                            case 90: // 'Z'
                            case 91: // '['
                            case 92: // '\\'
                            case 93: // ']'
                            case 94: // '^'
                            case 95: // '_'
                            case 96: // '`'
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }

                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }

        return outBuffer.toString();
    }

    public static char toHex(int nibble) {
        return hexDigit[nibble & 0xf];
    }

    public static Locale getLocaleFromString(String locale) {
        StringTokenizer st = new StringTokenizer(locale, "_ ,;:/");
        int tcount = st.countTokens();
        String a1;
        String a2 = "";
        String a3 = "";
        if (tcount < 1 || tcount > 3) {
            throw new RuntimeException("bad locale : " + locale);
        }
        if (st.hasMoreTokens()) {
            a1 = st.nextToken();
            if (st.hasMoreTokens()) {
                a2 = st.nextToken();
                if (st.hasMoreTokens()) {
                    a2 = st.nextToken();
                    if (st.hasMoreTokens()) {
                        throw new RuntimeException("bad locale : " + locale);
                    }
                }
            }
        } else {
            throw new RuntimeException("bad locale : " + locale);
        }
        return new Locale(a1, a2, a3);
    }

    public ConfigOptions getOptions() {
        return options;
    }

    public void clear() {
        props.clear();
        descs.clear();
        types.clear();
    }

    public void registerConverter(ConfigConverter c) {
        typesToConverters.put(c.getAcceptedType(), c);
        classesToConverters.put(c.getAcceptedClass(), c);
    }

    private void initConverters() {
        registerConverter(new IntConverter());
        registerConverter(new LongConverter());
        registerConverter(new ShortConverter());
        registerConverter(new DoubleConverter());
        registerConverter(new FloatConverter());
        registerConverter(new ByteConverter());
        registerConverter(new CharConverter());
        registerConverter(new DateConverter(this));
        registerConverter(new TimeConverter());
        registerConverter(new ObfuscatedValueConverter());
        registerConverter(new LocaleConverter());
        registerConverter(new ColorConverter());
        registerConverter(new FontConverter());
        registerConverter(new BooleanConverter());
        registerConverter(new StringConverter());
        registerConverter(new PrimitiveBooleanConverter());
        registerConverter(new PrimitiveIntArrayConverter());
    }

    public void load()
            throws IOException, ParseException {
        if (options.getFile() == null) {
            throw new IOException("No config file to load");
        } else if (!options.getFile().exists()) {
            boolean b = false;
            try {
                File p = options.getFile().getParentFile();
                if (p != null) {
                    p.mkdirs();
                }
                b = options.getFile().createNewFile();
            } catch (IOException e) {
                //
            }
            if (!b) {
                throw new IOException(options.getFile().getCanonicalPath() + " does not exist and could not be created as a configuration file");
            }
        }
        load(options.getFile());
        System.out.println("Configuration loaded");
    }

    public void store()
            throws IOException {
        if (isPersistenceEnabled() && options.getFile() != null) {
            store(options.getFile(), null);
        }
    }

    public boolean containsKey(String key) {
        return props.containsKey(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean s = (Boolean) props.get(key);
        return s != null ? s : defaultValue;
    }

    public char getChar(String key, char defaultValue) {
        Character s = (Character) props.get(key);
        return s != null ? s : defaultValue;
    }

    public void setDate(String key, Date date) {
        setProperty(key, date);
    }

    public void setTime(String key, Time date) {
        setProperty(key, date);
//        props.put(key, date ==null ? null : date);
    }

    public Date getDate(String key) {
        return (Date) props.get(key);
    }

    public Date getDate(String key, Date defaultValue) {
        Date r = (Date) props.get(key);
        return r == null ? defaultValue : r;
    }

    public Time getTime(String key) {
        return (Time) props.get(key);
    }

    public Time getTime(String key, Time defaultValue) {
        Time r = (Time) props.get(key);
        return r == null ? defaultValue : r;
    }

    public double getDouble(String key, double defaultValue) {
        Number s = (Number) props.get(key);
        return s != null ? s.doubleValue() : defaultValue;
    }

    public float getFloat(String key, float defaultValue) {
        Number s = (Number) props.get(key);
        return s != null ? s.floatValue() : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        Number s = (Number) props.get(key);
        return s != null ? s.intValue() : defaultValue;
    }

    public Font getFont(String key, Font defaultValue) {
        Font s = (Font) props.get(key);
        return s != null ? s : defaultValue;
    }

    public Color getColor(String key, Color defaultValue) {
        Color s = (Color) props.get(key);
        return s != null ? s : defaultValue;
    }

    public Locale getLocale(String key) {
        return (Locale) props.get(key);
    }

    public void setLocale(String key, Locale locale) {
        setProperty(key, locale);
    }

    public Locale getLocale(String key, Locale defaultValue) {
        Locale r = (Locale) props.get(key);
        return r == null ? defaultValue : r;
    }

    public long getLong(String key, long defaultValue) {
        Number s = (Number) props.get(key);
        return s != null ? s.longValue() : defaultValue;
    }

    public synchronized void deleteTree(String key, boolean deleteRoot) {
        for (Iterator i = props.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String entryKey = (String) entry.getKey();
            if (key.equals(entryKey)) {
                if (deleteRoot) {
                    i.remove();
                }
            } else if (entryKey.startsWith(key + ".")) {
                i.remove();
            }
        }
    }

    public String[] getChildrenKeys(String key, boolean recurse) {
        ArrayList<String> al = new ArrayList<String>();
        for (Iterator i = props.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String entryKey = (String) entry.getKey();
            if (entryKey.startsWith(key + ".")) {
                if (recurse || entryKey.substring(key.length() + 2).indexOf('.') < 0) {
                    al.add(entryKey);
                }
            }
        }
        return al.toArray(new String[al.size()]);
    }

    public Object getProperty(String key) {
        return props.get(key);
    }

    public String getPropertyComments(String key) {
        if (!descs.containsKey(key)) {
            String s = null;//getHelper().get(key, false);
            if (s != null) {
                descs.put(key, s);
            } else {
                descs.put(key, key);
            }
            return s;
        } else {
            return descs.get(key);
        }
    }

    public String getPropertyType(String key) {
        if (!containsKey(key)) {
            return null;
        } else {
            String v = types.get(key);
            return v != null ? v : "string";
        }
    }

    public short getShort(String key, short defaultValue) {
        Number s = (Number) props.get(key);
        return s != null ? s.shortValue() : defaultValue;
    }

    public String getString(String key) {
        return (String) props.get(key);
    }

    public int[] getIntArray(String key) {
        return (int[]) props.get(key);
    }

    public Object getObject(String key, Object defaultValue) {
        Object r = props.get(key);
        return r == null ? defaultValue : r;
    }

    public String getString(String key, String defaultValue) {
        String r = (String) props.get(key);
        return r == null ? defaultValue : r;
    }

    public String[] getStringArray(String key, String[] defaultValue) {
        String[] r = (String[]) props.get(key);
        return r == null ? defaultValue : r;
    }

    public Color[] getColorArray(String key, Color[] defaultValue) {
        Color[] r = (Color[]) props.get(key);
        return r == null ? defaultValue : r;
    }

    public Color[] getColorArray(String key) {
        return (Color[]) props.get(key);
    }

    public String[] getStringArray(String key) {
        return (String[]) props.get(key);
    }

    public Set keySet() {
        TreeSet<String> ts = new TreeSet<String>();
        for (String k : props.keySet()) {
            if (!k.endsWith("$type") && !k.endsWith("$comments")) {
                ts.add(k);
            }
        }

        return ts;
    }

    public void load(File inFile)
            throws IOException, ParseException {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(inFile);
            p.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        postLoad(p);
    }

    private void postLoad(Properties p) {
        for (Iterator i = p.entrySet().iterator(); i.hasNext(); ) {
            java.util.Map.Entry e = (java.util.Map.Entry) i.next();
            String keyType = (String) e.getKey();
            String key;
            String type;
            int x = keyType.indexOf(36);
            if (x > 0) {
                key = keyType.substring(0, x);
                type = keyType.substring(x + 1);
            } else {
                key = keyType;
                type = "string";
            }
            String stringValue = (String) e.getValue();
            Object value = getLoadedValue(stringValue, type);
            props.put(key, value);
            if (type != null) {
                types.put(key, type);
            } else {
                types.remove(key);
            }
        }
    }

    public void load(InputStream in)
            throws IOException, ParseException {
        Properties p = new Properties();
        p.load(in);
        postLoad(p);
    }

    public ConfigConverter getConverter(String type) {
        ConfigConverter conv = typesToConverters.get(type);
        if (conv == null) {
            throw new RuntimeException("Unhandled type " + type);
        }
        return conv;
    }

    public ConfigConverter getConverter(Class someClass) {
        ConfigConverter conv = classesToConverters.get(someClass);
        if (conv == null) {
            throw new RuntimeException("Unhandled Class " + someClass);
        }
        return conv;
    }

    public Object getLoadedValue(String stringValue, String type) {
        try {
            if (stringValue == null) {
                return null;
            }

            // test if some specific converter is registred
            if (typesToConverters.containsKey(type)) {
                return getConverter(type).stringToObject(stringValue);
            }

//            boolean isArray = false;
            if (isArrayType(type)) {

                String elemtype = type.substring(0, type.length() - 3);
                String sep = "" + getArraySeparator(type);
                ArrayList<Object> v = new ArrayList<Object>(3);
                boolean lastIsSep = true;
                StringTokenizer t = new StringTokenizer(stringValue, sep, true);
                while (t.hasMoreTokens()) {
                    String x = t.nextToken();
                    if (x.equals(sep)) {
                        if (lastIsSep) {
                            v.add(null);
                        }
                        lastIsSep = true;
                    } else {
                        lastIsSep = false;
                        v.add(getConverter(elemtype).stringToObject(x));
                    }
                }
                Class c = getConverter(elemtype).getAcceptedClass();
                Object[] arr = (Object[]) Array.newInstance(c, v.size());
                return v.toArray(arr);

            } else {
                return getConverter(type).stringToObject(stringValue);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void load(String inFile)
            throws IOException, ParseException {
        File fileInstance = new File(inFile);
        if (!fileInstance.exists()) {
            fileInstance.createNewFile();
        }
        load(fileInstance);
    }

    public void load(URL inURL)
            throws IOException, ParseException {
        load(inURL.openStream());
    }

    public synchronized void loadFromClass(Object classOrInstance) {
        boolean oldIsAutoSave = options.isAutoSave();
        options.setAutoSave(false);
        Object instance;
        Class instanceClass;
        if (classOrInstance instanceof Class) {
            instance = null;
            instanceClass = (Class) classOrInstance;
        } else {
            instance = classOrInstance;
            instanceClass = classOrInstance.getClass();
        }
        Field[] fields = instanceClass.getFields();
        for (Field field : fields) {
            try {
                if (Modifier.isPublic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
//                    Class fc = fields[i].getType();
                    String fn = field.getName();
                    Object fv = field.get(instance);
                    Field hf = null;
                    Field tf = null;
                    try {
                        hf = instanceClass.getField(fn + "__HELP");
                    } catch (Throwable e) {
                        //
                    }
                    try {
                        tf = instanceClass.getField(fn + "__TYPE");
                    } catch (Throwable e) {
                        //
                    }
                    String helpString = hf != null ? (String) hf.get(instance) : "NO_HELP_PROVIDED";
                    String typeField = tf != null ? (String) tf.get(instance) : null;
                    setProperty(fn, fv, typeField);
                    setPropertyComments(fn, helpString);
                }
            } catch (Throwable ee) {
                ee.printStackTrace();
            }
        }

        options.setAutoSave(oldIsAutoSave);
        autoSaveIfNecessary();
    }

    public void remove(String key) {
        props.remove(key);
        props.remove(key + "$type");
        props.remove(key + "$comments");
    }

    public void setBoolean(String key, boolean value) {
        setProperty(key, value ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setChar(String key, char value) {
        setProperty(key, value);
    }

    public void setConfidential(String key, String confidential) {
        setProperty(key, confidential, "confidential");
    }

    public void setDouble(String key, double value) {
        setProperty(key, value);
    }

    public void setFloat(String key, float value) {
        setProperty(key, value);
    }

    public void setArray(String key, Object[] arr, char sep) {
        setProperty(key, arr, null, sep);
    }

    public void setIntArray(String key, int[] intArray) {
        setProperty(key, intArray);
    }

    public void setInt(String key, int value) {
        setProperty(key, value);
    }

    public void setLong(String key, long value) {
        setProperty(key, value);
    }

    public void setProperty(String key, Object value) {
        setProperty(key, value, null);
    }

    public void setString(String key, String value) {
        setProperty(key, value, null);
    }

    public void setFont(String key, Font value) {
        setProperty(key, value, FONT_TYPE);
    }

    public void setColor(String key, Color value) {
        setProperty(key, value, COLOR_TYPE);
    }

    public void setStringArray(String key, String[] value, char sep) {
        setProperty(key, value, null, sep);
    }

    public void setColorArray(String key, Color[] value) {
        setProperty(key, value, null, ';');
    }

    private void setProperty(String key, Object value, String type) {
        setProperty(key, value, type, ';');
    }

    public boolean isSupportedClass(Class aClass) {
        if (classesToConverters.containsKey(aClass)) {
            return true;
        } else if (aClass.isArray()) {
            Class elementClass = aClass.getComponentType();
            if (classesToConverters.containsKey(elementClass)) {
                return true;
            }
        }
        return false;
    }

    private void setProperty(String key, Object value, String type, char sep) {
        if (value == null) {
            if (!props.containsKey(key)) {
                return;
            }
            remove(key);
        } else {
            if (type == null) {
                if (classesToConverters.containsKey(value.getClass())) {
                    type = getConverter(value.getClass()).getAcceptedType();
                } else if (value.getClass().isArray()) {
                    Class elementClass = value.getClass().getComponentType();
                    if (elementClass == null) {
                        throw new RuntimeException("exprected an array");
                    }
                    type = getConverter(elementClass).getAcceptedType();
                    type = type + "[" + sep + "]";
                    if (equals(value, props.get(key)) && equals(type, types.get(key))) {
                        return;
                    }
                } else {
                    type = getConverter(value.getClass()).getAcceptedType();
                }
            }
            if (equals(value, props.get(key)) && equals(type, types.get(key))) {
                return;
            }
            if ("string".equals(type)) {
                types.remove(key);
            } else {
                types.put(key, type);
            }
            props.put(key, value);
        }
        autoSaveIfNecessary();
    }

    private void autoSaveIfNecessary() {
        try {
            if (options.isAutoSave() && options.getFile() != null) {
                store();
            }
        } catch (Exception e) {
            System.err.println("Could not autosave Configuration to " + options.getFile().getAbsolutePath());
        }
    }

    public void setPropertyComments(String key, String comments) {
        if (equals(descs.get(key), comments)) {
            return;
        }
        if (comments == null || "".equals(comments)) {
            descs.remove(key);
        } else {
            descs.put(key, comments);
        }
        autoSaveIfNecessary();
    }

    public void setShort(String key, short value) {
        setProperty(key, value);
    }

    public void store(File out, String header) throws IOException {
        FileOutputStream fos;
        fos = new FileOutputStream(out);
        try {
            store(fos, header);
        } finally {
            fos.close();
        }
    }

    public void store(OutputStream out, String header)
            throws IOException {
        BufferedWriter awriter = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
        if (header != null) {
            awriter.write("# " + header);
            awriter.newLine();
        }
        awriter.write("# Last Modified " + (new Date()).toString());
        awriter.newLine();
        awriter.newLine();
        for (Iterator i = keySet().iterator(); i.hasNext(); awriter.flush()) {
            String key = (String) i.next();
            Object val = props.get(key);
            String comments = getOptions().isStoreComments() ? getPropertyComments(key) : null;
            String type = getPropertyType(key);
            //String stringVal = val != null ? "date".equals(type) ? getDateFormat().format((Date) val) : "confidential".equals(type) ? SecurityTools.lightWeightEncrypt((String) val) : "obfuscated".equals(type) ? (String) val : !"string".equals(type) && !"long".equals(type) && !"int".equals(type) && !"short".equals(type) && !"float".equals(type) && !"double".equals(type) && !"boolean".equals(type) && !"locale".equals(type) ? "cssa".equals(type) ? Utils.toString((String[]) val, ';') : (String) val : String.valueOf(val) : (String) null;
            String stringVal = getStoredValue(val, type);
            if (comments != null) {
                for (StringTokenizer st = new StringTokenizer(comments, "\n\r"); st.hasMoreTokens(); awriter.newLine()) {
                    awriter.write("# " + st.nextToken());
                }

                if (options.isHelpAsEntry()) {
                    awriter.write(toSpecialString(key + "$comments", true) + "=" + toSpecialString(comments, false));
                    awriter.newLine();
                }
            }
            awriter.write(toSpecialString(key, true) + (type != null ? "$" + toSpecialString(type, false) : "") + "=" + toSpecialString(stringVal, false));
            awriter.newLine();
            awriter.newLine();
        }

    }

    public String getStoredValue(Object val, String type) {
        if (val == null) {
            return null;
        }
        if (isArrayType(type)) {
            Object[] valArr = (Object[]) val;
            char sep = getArraySeparator(type);
            String baseType = getBaseType(type);
            StringBuilder sb = new StringBuilder();
            ConfigConverter conv = getConverter(baseType);
            for (int i = 0; i < valArr.length; i++) {
                if (i > 0) {
                    sb.append(sep);
                }
                sb.append(conv.objectToString(valArr[i]));
            }
            return sb.toString();
        } else {
            ConfigConverter conv = getConverter(type);
            return conv.objectToString(val);
        }
    }

    public void store(String outFile, String header)
            throws IOException {
        File fileInstance = new File(outFile);
        if (!fileInstance.exists()) {
            fileInstance.createNewFile();
        }
        FileOutputStream out = null;
        try {
            store(out = new FileOutputStream(outFile), header);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void store(URL out, String header)
            throws IOException {
        store(out.openConnection().getOutputStream(), header);
    }

    public void storeToClass(Object classOrInstance) {
        Object instance;
        Class instanceClass;
        if (classOrInstance instanceof Class) {
            instance = null;
            instanceClass = (Class) classOrInstance;
        } else {
            instance = classOrInstance;
            instanceClass = classOrInstance.getClass();
        }
        Field[] fields = instanceClass.getFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
//                Class fc = fields[i].getType();
                String fn = field.getName();
                Object value = getProperty(fn);
                if (value != null) {
                    try {
                        field.set(instance, getProperty(fn));
                    } catch (Exception e) {
                        throw new RuntimeException(e + " (" + fn + ")");
                    }
                }
            }
        }

    }

    public Map toMap() {
        return props;
    }

    public Iterator iterator() {
        return props.entrySet().iterator();
    }

    @Override
    public Configuration clone() {
        try {
            Configuration o = (Configuration) super.clone();
            o.props = new HashMap<String, Object>(props);
            o.descs = new HashMap<String, String>(descs);
            o.types = new HashMap<String, String>(types);
            o.typesToConverters = new HashMap<String, ConfigConverter>(typesToConverters);
            o.classesToConverters = new HashMap<Class, ConfigConverter>(classesToConverters);
            o.options = options.clone();
            return o;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }


}
