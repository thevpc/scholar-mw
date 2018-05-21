/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Complex;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

/**
 * @author vpc
 */
public class ExpressionStreamTokenizer {
    public static final String OPERATORS="+-*/^=<>!&|$@:";
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

    public static final int TT_LONG_OPERATOR = -7;
    private Reader reader;

    public ExpressionStreamTokenizer(Reader reader) {
        this.reader = reader;
    }

    LinkedList<Token> back = new LinkedList<>();

    public void pushBack(Token token) {
//        System.out.println("PUSH "+token);
        back.add(token);
    }

    public Token peek() throws IOException {
        Token t = nextToken0();
        back.add(t);
//        System.out.println("PEEK "+t);
        return t;
    }

    public Token[] peek(int count) throws IOException {
        Token[] all = new Token[count];
        for (int i = 0; i < count; i++) {
            all[i] = nextToken();
        }
        for (int i = 0; i < count; i++) {
            pushBack(all[i]);
        }
        return all;
    }

    public Token nextToken() throws IOException {
        Token t = nextToken0();
//        System.out.println("READ "+t);
        return t;
    }
    public Token nextToken0() throws IOException {
        if (!back.isEmpty()) {
            return back.removeFirst();
        }
        while(true) {
            Token token = new Token();
            int cc = reader.read();
            boolean number=false;
            if(cc=='-'){
//                reader.mark(1);
//                int cc1=reader.read();
//                if (cc1 >= '0' && cc1 <= '9' || cc1 == '.') {
//                    number=true;
//                }
//                reader.reset();
            }else if((cc >= '0' && cc <= '9' || cc == '.')){
                number=true;
            }
            if (number) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) cc);
                while (true) {
                    reader.mark(1);
                    cc = reader.read();
                    switch (cc) {
                        case -1: {
                            token.ttype = TT_NUMBER;
                            token.image = sb.toString();
                            token.nval = Double.valueOf(sb.toString());
                            return token;
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
                            token.ttype = TT_COMPLEX;
                            token.image = sb.toString() + ((char)cc);
                            token.cval = Complex.I(Double.valueOf(sb.toString()));
                            return token;
                        }
                        default: {
                            if (cc >= '0' && cc <= '9' || cc == '.') {
                                sb.append((char) cc);
                            } else {
                                reader.reset();
                                token.ttype = TT_NUMBER;
                                token.image = (sb.toString());
                                token.nval = Double.valueOf(sb.toString());
                                return token;
                            }
                        }
                    }
                }
            } else if (cc == 'î') {
                token.ttype = TT_COMPLEX;
                token.image = String.valueOf((char)cc);
                token.cval = Complex.I;
                return token;
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
                if (sb.toString().equals("î")) {
                    token.ttype = TT_COMPLEX;
                    token.cval = Complex.I;
                    token.image = sb.toString();
                } else {
                    token.ttype = TT_WORD;
                    token.sval = sb.toString();
                    token.image = sb.toString();
                }
                return token;
            } else if (cc == ' ') {
                //ignore
            } else if (cc == '\t') {
                //ignore
            } else if (cc == '\n') {
                //ignore
            } else if (cc == '\"') {
                StringBuilder sb = new StringBuilder();
                StringBuilder image = new StringBuilder("" + cc);
                while (true) {
                    cc = reader.read();
                    image.append(cc);
                    if (cc != '\"') {
                        sb.append((char) cc);
                    } else {
                        break;
                    }
                }
                token.ttype = TT_STRING;
                token.sval = sb.toString();
                token.image = image.toString();
                return token;
            } else if (isOpChar(cc)) {
                StringBuilder opString=new StringBuilder();
                int cc0=cc;
                opString.append((char)cc);
                while (true) {
                    reader.mark(1);
                    cc = reader.read();
                    if(!isOpChar(cc)){
                        reader.reset();
                        break;
                    }
                    opString.append((char)cc);
                }
                if(opString.length()==1){
                    token.ttype = cc0;
                    token.image = String.valueOf((char)cc0);
                }else{
                    token.ttype = TT_LONG_OPERATOR;
                    token.image = opString.toString();
                }
                return token;
            } else {
                token.ttype = cc;
                token.image = String.valueOf((char)cc);
                return token;
            }
        }
    }

    private boolean isOpChar(int cc) {
        return OPERATORS.indexOf(cc)>=0;
    }


    public static class Token {
        public int LINENO = 0;
        public int ttype = TT_NOTHING;
        public double nval;
        public Complex cval;
        public String sval;
        public String image;

        public boolean isEOF() {
            return ttype==TT_EOF;
        }

//        public long getOpPrecedence() {
//            int x = 1;
//            for (char c : image.toCharArray()) {
//                int i = OPERATORS.indexOf(c);
//                if (i >= 0) {
//                    x = x * OPERATORS.length() + i;
//                }
//            }
//            return Math.abs(x);
//        }
//
//        public boolean isOp() {
//            for (char c : OPERATORS.toCharArray()) {
//                if (image.charAt(0) == c) {
//                    return true;
//                }
//            }
//            return false;
//        }

        public Token() {
            nval = 0;
            cval = null;
            sval = null;
            image = null;
            ttype = -1;
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
                    ret = "s=" + sval;
                    break;
                case TT_LONG_OPERATOR:
                    ret = "lop=" + image;
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
}
