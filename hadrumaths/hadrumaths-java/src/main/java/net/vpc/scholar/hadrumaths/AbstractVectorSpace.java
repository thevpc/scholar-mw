package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.Converter;
import net.vpc.common.util.TypeName;

import java.util.List;

public abstract class AbstractVectorSpace<T> implements VectorSpace<T> {
    private Ops<T> ops = new OpsImpl<>(this);

    @Override
    public <R> Converter<R, T> getConverterFrom(Class<R> t) {
        return Maths.Config.getConverter(t, getItemType().getTypeClass());
    }

    @Override
    public <R> Converter<T, R> getConverterTo(Class<R> t) {
        return Maths.Config.getConverter(getItemType().getTypeClass(), t);
    }

    @Override
    public <R> Converter<R, T> getConverterFrom(TypeName<R> t) {
        return Maths.Config.getConverter(t, getItemType());
    }

    @Override
    public <R> Converter<T, R> getConverterTo(TypeName<R> t) {
        return Maths.Config.getConverter(getItemType(), t);
    }

//    @Override
//    public <R> Converter<R, T> getConverterFrom(TypeReference<R> t) {
//        return Maths.Config.getConverter(t,getItemType());
//    }
//
//    @Override
//    public <R> Converter<T, R> getConverterTo(TypeReference<R> t) {
//        return Maths.Config.getConverter(getItemType(),t);
//    }

    @Override
    public <R> R convertTo(T value, Class<R> t) {
        return getConverterTo(t).convert(value);
    }

    @Override
    public <R> T convertFrom(R value, Class<R> t) {
        return getConverterFrom(t).convert(value);
    }

    @Override
    public T addAll(List<T> b) {
        RepeatableOp<T> o = addRepeatableOp();
        for (T t : b) {
            o.append(t);
        }
        return o.eval();
    }

    @Override
    public T mulAll(List<T> b) {
        RepeatableOp<T> o = mulRepeatableOp();
        for (T t : b) {
            o.append(t);
        }
        return o.eval();
    }

    public Ops<T> ops() {
        return ops;
    }

    public ElementOp<T> tanhOp() {
        return new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) tanh(e);
            }
        };
    }

    public static class OpsImpl<T> implements Ops<T> {
        private VectorSpace<T> v;
        private ElementOp<T> tanh = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.tanh(e);
            }
        };
        private ElementOp<T> tan = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.tan(e);
            }
        };
        private ElementOp<T> sin = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.sin(e);
            }
        };
        private ElementOp<T> cos = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.cos(e);
            }
        };
        private ElementOp<T> real = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.real(e);
            }
        };
        private ElementOp<T> imag = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.imag(e);
            }
        };
        private ElementOp<T> abs = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.abs(e);
            }
        };
        private ElementOp<T> abssqr = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.abssqr(e);
            }
        };
        private ElementOp<T> neg = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.neg(e);
            }
        };
        private ElementOp<T> conj = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.conj(e);
            }
        };
        private ElementOp<T> inv = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.inv(e);
            }
        };
        private ElementOp<T> cotan = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.cotan(e);
            }
        };
        private ElementOp<T> sinh = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.sinh(e);
            }
        };
        private ElementOp<T> cosh = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.cosh(e);
            }
        };
        private ElementOp<T> sincard = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.sincard(e);
            }
        };
        private ElementOp<T> cotanh = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.cotanh(e);
            }
        };
        private ElementOp<T> asinh = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.asinh(e);
            }
        };
        private ElementOp<T> acosh = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.acosh(e);
            }
        };
        private ElementOp<T> asin = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.asin(e);
            }
        };
        private ElementOp<T> acos = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.acos(e);
            }
        };
        private ElementOp<T> atan = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.atan(e);
            }
        };
        private ElementOp<T> arg = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.arg(e);
            }
        };
        private ElementOp<T> acotan = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.acotan(e);
            }
        };
        private ElementOp<T> exp = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.exp(e);
            }
        };
        private ElementOp<T> log = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.log(e);
            }
        };
        private ElementOp<T> log10 = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.log10(e);
            }
        };
        private ElementOp<T> db = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.db(e);
            }
        };
        private ElementOp<T> db2 = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.db2(e);
            }
        };
        private ElementOp<T> sqr = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.sqr(e);
            }
        };
        private ElementOp<T> sqrt = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.sqrt(e);
            }
        };
        private ElementOp<T> not = new ElementOp<T>() {
            @Override
            public T eval(int index, T e) {
                return (T) v.not(e);
            }
        };

        public OpsImpl(VectorSpace<T> v) {
            this.v = v;
        }

        public ElementOp<T> tanh() {
            return tanh;
        }

        public ElementOp<T> tan() {
            return tan;
        }

        public ElementOp<T> sin() {
            return sin;
        }

        public ElementOp<T> cos() {
            return cos;
        }

        public ElementOp<T> real() {
            return real;
        }

        public ElementOp<T> imag() {
            return imag;
        }

        public ElementOp<T> abs() {
            return abs;
        }

        public ElementOp<T> abssqr() {
            return abssqr;
        }

        public ElementOp<T> neg() {
            return neg;
        }

        public ElementOp<T> conj() {
            return conj;
        }

        public ElementOp<T> inv() {
            return inv;
        }

        public ElementOp<T> cotan() {
            return cotan;
        }

        public ElementOp<T> sinh() {
            return sinh;
        }

        public ElementOp<T> cosh() {
            return cosh;
        }

        public ElementOp<T> sincard() {
            return sincard;
        }

        public ElementOp<T> cotanh() {
            return cotanh;
        }

        public ElementOp<T> asinh() {
            return asinh;
        }

        public ElementOp<T> acosh() {
            return acosh;
        }

        public ElementOp<T> asin() {
            return asin;
        }

        public ElementOp<T> acos() {
            return acos;
        }

        public ElementOp<T> atan() {
            return atan;
        }

        public ElementOp<T> arg() {
            return arg;
        }

        public ElementOp<T> acotan() {
            return acotan;
        }

        public ElementOp<T> exp() {
            return exp;
        }

        public ElementOp<T> log() {
            return log;
        }

        public ElementOp<T> log10() {
            return log10;
        }

        public ElementOp<T> db() {
            return db;
        }

        public ElementOp<T> db2() {
            return db2;
        }

        public ElementOp<T> sqr() {
            return sqr;
        }

        public ElementOp<T> sqrt() {
            return sqrt;
        }

        public ElementOp<T> not() {
            return not;
        }
    }
}
