package net.vpc.scholar.hadrumaths.util.config;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: TAHA
 * Date: 7 fevr. 2004
 * Time: 09:11:50
 * 
 */
public class Properties2 extends LinkedHashMap {
    public static LoadMetaDataHandler DEFAULT_LOAD_META_DATA_HANDLER = new LoadMetaDataHandler();
    private Vector metaDataHandlers;
    private Vector metadata;

    public Properties2() {
        super();
        addMetaDataHandler(DEFAULT_LOAD_META_DATA_HANDLER);
    }

    public Properties2(Map map) {
        super(map);
        addMetaDataHandler(DEFAULT_LOAD_META_DATA_HANDLER);
    }

    /**
     * Calls the <tt>Hashtable</tt> method <code>put</code>. Provided for
     * parallelism with the <tt>getProperty</tt> method. Enforces use of
     * strings for property keys and values. The value returned is the
     * result of the <tt>Hashtable</tt> call to <code>put</code>.
     *
     * @param key   the key to be placed into this property list.
     * @param value the value corresponding to <tt>key</tt>.
     * @return the previous value of the specified key in this property
     * list, or <code>null</code> if it did not have one.
     * @see #getProperty
     * @since 1.2
     */
    public synchronized Object setProperty(String key, String value) {
        return put(key, value);
    }

    private static final String keyValueSeparators = "=: \t\r\n\f";

    private static final String strictKeyValueSeparators = "=:";

    private static final String specialSaveChars = "=: \t\r\n\f#!";

    private static final String whiteSpaceChars = " \t\r\n\f";

    private File loadingFile;

    public synchronized void load(File file) throws IOException {
        FileInputStream fis = null;
        loadingFile = file;
        try {
            fis = new FileInputStream(file);
            load(fis);
        } finally {
            loadingFile = null;
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * Reads a property list (key and element pairs) from the input
     * stream.  The stream is assumed to be using the ISO 8859-1
     * character encoding; that is each byte is one Latin1 character.
     * Characters not in Latin1, and certain special characters, can
     * be represented in keys and elements using escape sequences
     * similar to those used for character and string literals (see <a
     * href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#100850">&sect;3.3</a>
     * and <a
     * href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089">&sect;3.10.6</a>
     * of the <i>Java Language Specification</i>).
     * <p/>
     * The differences from the character escape sequences used for
     * characters and strings are:
     * <p/>
     * <ul>
     * <li> Octal escapes are not recognized.
     * <p/>
     * <li> The character sequence <code>\b</code> does <i>not</i>
     * represent a backspace character.
     * <p/>
     * <li> The method does not treat a backslash character,
     * <code>\</code>, before a non-valid escape character as an
     * error; the backslash is silently dropped.  For example, in a
     * Java string the sequence <code>"\z"</code> would cause a
     * compile time error.  In contrast, this method silently drops
     * the backslash.  Therefore, this method treats the two character
     * sequence <code>"\b"</code> as equivalent to the single
     * character <code>'b'</code>.
     * <p/>
     * <li> Escapes are not necessary for single and double quotes;
     * however, by the rule above, single and double quote characters
     * preceded by a backslash still yield single and double quote
     * characters, respectively.
     * <p/>
     * </ul>
     * <p/>
     * An <code>IllegalArgumentException</code> is thrown if a
     * malformed Unicode escape appears in the input.
     * <p/>
     * <p/>
     * This method processes input in terms of lines.  A natural line
     * of input is terminated either by a set of line terminator
     * characters (<code>\n</code> or <code>\r</code> or
     * <code>\r\n</code>) or by the end of the file.  A natural line
     * may be either a blank line, a comment line, or hold some part
     * of a key-element pair.  The logical line holding all the data
     * for a key-element pair may be spread out across several adjacent
     * natural lines by escaping the line terminator sequence with a
     * backslash character, <code>\</code>.  Note that a comment line
     * cannot be extended in this manner; every natural line that is a
     * comment must have its own comment indicator, as described
     * below.  If a logical line is continued over several natural
     * lines, the continuation lines receive further processing, also
     * described below.  Lines are read from the input stream until
     * end of file is reached.
     * <p/>
     * <p/>
     * A natural line that contains only white space characters is
     * considered blank and is ignored.  A comment line has an ASCII
     * <code>'#'</code> or <code>'!'</code> as its first non-white
     * space character; comment lines are also ignored and do not
     * encode key-element information.  In addition to line
     * terminators, this method considers the characters space
     * (<code>' '</code>, <code>'&#92;u0020'</code>), tab
     * (<code>'\t'</code>, <code>'&#92;u0009'</code>), and form feed
     * (<code>'\f'</code>, <code>'&#92;u000C'</code>) to be white
     * space.
     * <p/>
     * <p/>
     * If a logical line is spread across several natural lines, the
     * backslash escaping the line terminator sequence, the line
     * terminator sequence, and any white space at the start the
     * following line have no affect on the key or element values.
     * The remainder of the discussion of key and element parsing will
     * assume all the characters constituting the key and element
     * appear on a single natural line after line continuation
     * characters have been removed.  Note that it is <i>not</i>
     * sufficient to only examine the character preceding a line
     * terminator sequence to to see if the line terminator is
     * escaped; there must be an odd number of contiguous backslashes
     * for the line terminator to be escaped.  Since the input is
     * processed from left to right, a non-zero even number of
     * 2<i>n</i> contiguous backslashes before a line terminator (or
     * elsewhere) encodes <i>n</i> backslashes after escape
     * processing.
     * <p/>
     * <p/>
     * The key contains all of the characters in the line starting
     * with the first non-white space character and up to, but not
     * including, the first unescaped <code>'='</code>,
     * <code>':'</code>, or white space character other than a line
     * terminator. All of these key termination characters may be
     * included in the key by escaping them with a preceding backslash
     * character; for example,<p>
     * <p/>
     * <code>\:\=</code><p>
     * <p/>
     * would be the two-character key <code>":="</code>.  Line
     * terminator characters can be included using <code>\r</code> and
     * <code>\n</code> escape sequences.  Any white space after the
     * key is skipped; if the first non-white space character after
     * the key is <code>'='</code> or <code>':'</code>, then it is
     * ignored and any white space characters after it are also
     * skipped.  All remaining characters on the line become part of
     * the associated element string; if there are no remaining
     * characters, the element is the empty string
     * <code>&quot;&quot;</code>.  Once the raw character sequences
     * constituting the key and element are identified, escape
     * processing is performed as described above.
     * <p/>
     * <p/>
     * As an example, each of the following three lines specifies the key
     * <code>"Truth"</code> and the associated element value
     * <code>"Beauty"</code>:
     * <p/>
     * <pre>
     * Truth = Beauty
     * 	Truth:Beauty
     * Truth			:Beauty
     * </pre>
     * As another example, the following three lines specify a single
     * property:
     * <p/>
     * <pre>
     * fruits                           apple, banana, pear, \
     *                                  cantaloupe, watermelon, \
     *                                  kiwi, mango
     * </pre>
     * The key is <code>"fruits"</code> and the associated element is:
     * <p/>
     * <pre>"apple, banana, pear, cantaloupe, watermelon, kiwi, mango"</pre>
     * Note that a componentVectorSpace appears before each <code>\</code> so that a componentVectorSpace
     * will appear after each comma in the final result; the <code>\</code>,
     * line terminator, and leading white space on the continuation line are
     * merely discarded and are <i>not</i> replaced by one or more other
     * characters.
     * <p/>
     * As a third example, the line:
     * <p/>
     * <pre>cheeses
     * </pre>
     * specifies that the key is <code>"cheeses"</code> and the associated
     * element is the empty string <code>""</code>.<p>
     *
     * @param inStream the input stream.
     * @throws IOException              if an error occurred when reading from the
     *                                  input stream.
     * @throws IllegalArgumentException if the input stream contains a
     *                                  malformed Unicode escape sequence.
     */
    public synchronized void load(InputStream inStream) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "8859_1"));
        while (true) {
// Get next line
            String line = in.readLine();
            if (line == null)
                return;

            if (line.length() > 0) {

// Find start of key
                int len = line.length();
                int keyStart;
                for (keyStart = 0; keyStart < len; keyStart++)
                    if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1)
                        break;

// Blank lines are ignored
                if (keyStart == len)
                    continue;

// Continue lines that end in slashes if they are not comments
                char firstChar = line.charAt(keyStart);
                if ((firstChar != '#') && (firstChar != '!')) {
                    while (continueLine(line)) {
                        String nextLine = in.readLine();
                        if (nextLine == null)
                            nextLine = "";
                        String loppedLine = line.substring(0, len - 1);
// Advance beyond whitespace on new line
                        int startIndex;
                        for (startIndex = 0; startIndex < nextLine.length(); startIndex++)
                            if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1)
                                break;
                        nextLine = nextLine.substring(startIndex, nextLine.length());
                        line = new String(loppedLine + nextLine);
                        len = line.length();
                    }

// Find separation between key and value
                    int separatorIndex;
                    for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
                        char currentChar = line.charAt(separatorIndex);
                        if (currentChar == '\\')
                            separatorIndex++;
                        else if (keyValueSeparators.indexOf(currentChar) != -1)
                            break;
                    }

// Skip over whitespace after key if any
                    int valueIndex;
                    for (valueIndex = separatorIndex; valueIndex < len; valueIndex++)
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;

// Skip over one non whitespace key value separators if any
                    if (valueIndex < len)
                        if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1)
                            valueIndex++;

// Skip over white space after other separators if any
                    while (valueIndex < len) {
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;
                        valueIndex++;
                    }
                    String key = line.substring(keyStart, separatorIndex);
                    String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

// Convert then store key and value
                    key = loadConvert(key);
                    value = loadConvert(value);
                    put(key, value);
                } else {
// #meta
                    line = line.substring(1);
                    if (line.startsWith("meta ")) {
                        String data = line.substring(5).trim();
                        addMetadata(data);
                        if (metaDataHandlers != null) {
                            StringBuilder sb = new StringBuilder(data.length());
                            for (int i = 0; i < data.length(); i++) {
                                char c = data.charAt(i);
                                if (sb.length() == 0) {
                                    if (Character.isJavaIdentifierStart(c)) {
                                        sb.append(c);
                                    } else {
                                        break;
                                    }
                                } else {
                                    if (Character.isJavaIdentifierPart(c)) {
                                        sb.append(c);
                                    } else {
                                        break;
                                    }
                                }
                            }
                            String sbs = sb.toString();
                            for (int i = 0; i < metaDataHandlers.size(); i++) {
                                MetaDataHandler handler = (MetaDataHandler) metaDataHandlers.get(i);
                                handler.handleMetaData(this, sbs, data);
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * Returns true if the given line is a line that must
     * be appended to the next line
     */
    private boolean continueLine(String line) {
        int slashCount = 0;
        int index = line.length() - 1;
        while ((index >= 0) && (line.charAt(index--) == '\\'))
            slashCount++;
        return (slashCount % 2 == 1);
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars
     * and changes special saved chars to their original forms
     */
    private String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);

        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /*
     * Converts unicodes to encoded &#92;uxxxx
     * and writes out any of the characters in specialSaveChars
     * with a preceding slash
     */
    private String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len * 2);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');

                    outBuffer.append(' ');
                    break;
                case '\\':
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                default:
                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        if (specialSaveChars.indexOf(aChar) != -1)
                            outBuffer.append('\\');
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    /**
     * Calls the <code>store(OutputStream out, String header)</code> method
     * and suppresses IOExceptions that were thrown.
     *
     * @param out    an output stream.
     * @param header a description of the property list.
     * @throws ClassCastException if this <code>Properties</code> object
     *                            contains any keys or values that are not <code>Strings</code>.
     * @deprecated This method does not throw an IOException if an I/O error
     * occurs while saving the property list.  As of the Java 2 platform v1.2, the preferred
     * way to save a properties list is via the <code>store(OutputStream out,
     * String header)</code> method.
     */
    public synchronized void save(OutputStream out, String header) {
        try {
            store(out, header);
        } catch (IOException e) {
        }
    }

    /**
     * Writes this property list (key and element pairs) in this
     * <code>Properties</code> table to the output stream in a format suitable
     * for loading into a <code>Properties</code> table using the
     * {@link #load(InputStream) load} method.
     * The stream is written using the ISO 8859-1 character encoding.
     * <p/>
     * Properties from the defaults table of this <code>Properties</code>
     * table (if any) are <i>not</i> written out by this method.
     * <p/>
     * If the header argument is not null, then an ASCII <code>#</code>
     * character, the header string, and a line separator are first written
     * to the output stream. Thus, the <code>header</code> can serve as an
     * identifying comment.
     * <p/>
     * Next, a comment line is always written, consisting of an ASCII
     * <code>#</code> character, the current date and time (as if produced
     * by the <code>toString</code> method of <code>Date</code> for the
     * current time), and a line separator as generated by the Writer.
     * <p/>
     * Then every entry in this <code>Properties</code> table is
     * written out, one per line. For each entry the key string is
     * written, then an ASCII <code>=</code>, then the associated
     * element string. Each character of the key and element strings
     * is examined to see whether it should be rendered as an escape
     * sequence. The ASCII characters <code>\</code>, tab, form feed,
     * newline, and carriage return are written as <code>\\</code>,
     * <code>\t</code>, <code>\f</code> <code>\n</code>, and
     * <code>\r</code>, respectively. Characters less than
     * <code>&#92;u0020</code> and characters greater than
     * <code>&#92;u007E</code> are written as
     * <code>&#92;u</code><i>xxxx</i> for the appropriate hexadecimal
     * value <i>xxxx</i>.  For the key, all space characters are
     * written with a preceding <code>\</code> character.  For the
     * element, leading space characters, but not embedded or trailing
     * space characters, are written with a preceding <code>\</code>
     * character. The key and element characters <code>#</code>,
     * <code>!</code>, <code>=</code>, and <code>:</code> are written
     * with a preceding backslash to ensure that they are properly loaded.
     * <p/>
     * After the entries have been written, the output stream is flushed.  The
     * output stream remains open after this method returns.
     *
     * @param out    an output stream.
     * @param header a description of the property list.
     * @throws IOException          if writing this property list to the specified
     *                              output stream throws an <tt>IOException</tt>.
     * @throws ClassCastException   if this <code>Properties</code> object
     *                              contains any keys or values that are not <code>Strings</code>.
     * @throws NullPointerException if <code>out</code> is null.
     * @since 1.2
     */
    public synchronized void store(OutputStream out, String header)
            throws IOException {
        BufferedWriter awriter;
        awriter = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
        if (header != null)
            writeln(awriter, "#" + header);
        writeln(awriter, "#" + new Date().toString());
        if (metadata != null) {
            for (int i = 0; i < metadata.size(); i++) {
                writeln(awriter, "#meta " + metadata.get(i));
            }
        }
        for (Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Map.Entry entry = (Map.Entry) e.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            key = saveConvert(key, true);

            /* No need to escape embedded and trailing spaces for value, hence
             * pass false to flag.
             */
            val = saveConvert(val, false);
            writeln(awriter, key + "=" + val);
        }
        awriter.flush();
    }

    private static void writeln(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * <code>null</code> if the property is not found.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     * @see #setProperty
     */
    public String getProperty(String key) {
        Object oval = super.get(key);
        return (oval instanceof String) ? (String) oval : null;
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param key          the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     * @see #setProperty
     */
    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }


    /**
     * Prints this property list out to the specified output stream.
     * This method is useful for debugging.
     *
     * @param out an output stream.
     */
    public void list(PrintStream out) {
        out.println("-- listing properties --");
        Map<String, String> h = new HashMap<String, String>();
        enumerate(h);
        for (Map.Entry<String, String> entry : h.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (val.length() > 40) {
                val = val.substring(0, 37) + "...";
            }
            out.println(key + "=" + val);
        }
    }

    private synchronized void enumerate(Map h) {
        for (Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Map.Entry entry = (Map.Entry) e.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            h.put(key, val);
        }
    }

    /**
     * Prints this property list out to the specified output stream.
     * This method is useful for debugging.
     *
     * @param out an output stream.
     * @since JDK1.1
     */
    /*
     * Rather than use an anonymous inner class to share common code, this
     * method is duplicated in order to ensure that a non-1.1 compiler can
     * compile this file.
     */
    public void list(PrintWriter out) {
        list(out, 40);
    }

    public void list(PrintWriter out, int maxLineLenght) {
        out.println("-- listing properties --");
        Map<String, String> h = new HashMap();
        enumerate(h);
        for (Map.Entry<String, String> entry : h.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (val.length() > maxLineLenght) {
                if (maxLineLenght > 3) {
                    val = val.substring(0, maxLineLenght - 3) + "...";
                } else {
                    val = "...";
                }
            }
            out.println(key + "=" + val);
        }
    }

    public String[] getMetaData() {
        if (metadata == null) {
            return new String[0];
        } else {
            return (String[]) metadata.toArray(new String[0]);
        }
    }

    public void addMetadata(String meta) {
        if (metadata == null) {
            metadata = new Vector();
        }
        metadata.add(meta);
    }

    public void clearMetaData() {
        metadata = null;
    }

    public int getMetaDataSize() {
        return metadata == null ? 0 : metadata.size();
    }

    public String getMetaDataAt(int i) {
        return metadata == null ? null : (String) metadata.get(i);
    }

    public void removeMetaDataAt(int i) {
        if (metadata != null) {
            metadata.remove(i);
        }
    }

    public File getLoadingFile() {
        return loadingFile;
    }

    public void addMetaDataHandler(MetaDataHandler handler) {
        if (metaDataHandlers == null) {
            metaDataHandlers = new Vector();
        }
        metaDataHandlers.add(handler);
    }

    public void removeMetaDataHandler(MetaDataHandler handler) {
        if (metaDataHandlers != null) {
            metaDataHandlers.remove(handler);
        }
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
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');
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
                    if (specialSaveChars.indexOf(aChar) != -1)
                        outBuffer.append('\\');
                    outBuffer.append(aChar);
                    break;
            }
        }

        return outBuffer.toString();
    }

    private static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static char toHex(int nibble) {
        return hexDigit[nibble & 0xf];
    }

}
