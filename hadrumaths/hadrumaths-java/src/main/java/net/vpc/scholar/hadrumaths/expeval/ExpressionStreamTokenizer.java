/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Complex;

import java.io.IOException;
import java.io.Reader;

/**
 * @author vpc
 */
public class ExpressionStreamTokenizer {

    public int ttype = TT_NOTHING;
    /**
     * A constant indicating that the end of the stream has been read.
     */
    public static final int TT_EOF = -1;
    /**
     * A constant indicating that the end of the line has been read.
     */
    public static final int TT_EOL = '\n';
    /**
     * A constant indicating that a number token has been read.
     */
    public static final int TT_NUMBER = -2;
    /**
     * A constant indicating that a word token has been read.
     */
    public static final int TT_WORD = -3;

    /* A constant indicating that no token has been read, used for
     * initializing ttype.  FIXME This could be made public and
     * made available as the part of the API in a future release.
     */
    private static final int TT_NOTHING = -4;
    public static final int TT_COMPLEX = -5;
    /**
     * A constant indicating that a word token has been read.
     */
    public static final int TT_STRING = -6;
    private Reader reader;
    public double nval;
    public Complex cval;
    public String sval;
    int LINENO = 0;

    public ExpressionStreamTokenizer(Reader reader) {
        this.reader = reader;
    }

    public int nextToken() throws IOException {
        nval = 0;
        cval = null;
        sval = null;
        ttype = -1;
        int cc = reader.read();
        if (cc >= '0' && cc <= '9' || cc == '.') {
            StringBuilder sb = new StringBuilder();
            sb.append((char) cc);
            while (true) {
                reader.mark(1);
                cc = reader.read();
                switch (cc) {
                    case -1: {
                        ttype = TT_NUMBER;
                        nval = Double.valueOf(sb.toString());
                        return ttype;
                    }
                    case 'E': {
                        sb.append((char) cc);
                        cc = reader.read();
                        switch (cc) {
                            case -1: {
                                break;
                            }
                            default: {
                                sb.append((char) cc);
                                break;
                            }
                        }
                        break;
                    }
                    case 'î': {
                        ttype = TT_COMPLEX;
                        cval = Complex.I(Double.valueOf(sb.toString()));
                        return ttype;
                    }
                    default: {
                        if (cc >= '0' && cc <= '9' || cc == '.') {
                            sb.append((char) cc);
                        } else {
                            reader.reset();
                            ttype = TT_NUMBER;
                            nval = Double.valueOf(sb.toString());
                            return ttype;
                        }
                    }
                }
            }
        } else if (cc == 'î') {
            ttype = TT_COMPLEX;
            cval = Complex.I;
            return ttype;
        } else if ((cc >= 'a' && cc <= 'z') || (cc >= 'A' & cc <= 'Z') || cc == '_') {
            StringBuilder sb = new StringBuilder();
            sb.append((char) cc);
            while (true) {
                reader.mark(1);
                cc = reader.read();
                if ((cc >= 'a' && cc <= 'z') || (cc >= 'A' && cc <= 'Z') || (cc >= '0' && cc <= '9') || cc == '_') {
                    sb.append((char) cc);
                } else {
                    reader.reset();
                    break;
                }
            }
            if (sb.toString().equals("i")) {
                ttype = TT_COMPLEX;
                cval = Complex.I;
            } else {
                ttype = TT_WORD;
                sval = sb.toString();
            }
            return ttype;
        } else if (cc == '\"') {
            StringBuilder sb = new StringBuilder();
            while (true) {
                cc = reader.read();
                if (cc != '\"') {
                    sb.append((char) cc);
                } else {
                    break;
                }
            }
            ttype = TT_STRING;
            sval = sb.toString();
            return ttype;
        } else {
            ttype = cc;
            return cc;
        }
    }

    @Override
    public String toString() {
        String ret;
        switch (ttype) {
            case TT_EOF:
                ret = "EOF";
                break;
//	  case TT_EOL:
//	    ret = "EOL";
//	    break;
            case TT_WORD:
                ret = sval;
                break;
            case TT_COMPLEX:
                ret = "c=" + cval.toString();
                break;
            case TT_NUMBER:
                ret = "n=" + nval;
                break;
            case TT_STRING:
                ret = "s=" + nval;
                break;
//   	  case TT_NOTHING:
//	    ret = "NOTHING";
//	    break;
            default: {
                char s[] = new char[3];
                s[0] = s[2] = '\'';
                s[1] = (char) ttype;
                ret = new String(s);
                break;
            }
        }
        return "Token[" + ret + "], line " + LINENO;
    }
}
