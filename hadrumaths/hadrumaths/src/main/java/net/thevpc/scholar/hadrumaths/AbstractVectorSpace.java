package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractVectorSpace<T> implements VectorSpace<T> {
    private final Ops<T> ops = new OpsImpl<>(this);

    @Override
    public <R> Function<R, T> getConverterFrom(TypeName<R> t) {
        return Maths.Config.getConverter(t, getItemType());
    }

    @Override
    public <R> Function<T, R> getConverterTo(TypeName<R> t) {
        return Maths.Config.getConverter(getItemType(), t);
    }

    @Override
    public <R> Function<R, T> getConverterFrom(Class<R> t) {
        return Maths.Config.getConverter(t, getItemType().getTypeClass());
    }

    @Override
    public <R> Function<T, R> getConverterTo(Class<R> t) {
        return Maths.Config.getConverter(getItemType().getTypeClass(), t);
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
        return getConverterTo(t).apply(value);
    }

    @Override
    public <R> T convertFrom(R value, Class<R> t) {
        return getConverterFrom(t).apply(value);
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

    public VectorOp<T> tanhOp() {
        return new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return tanh(e);
            }
        };
    }

    public static class OpsImpl<T> implements Ops<T> {
        private final VectorSpace<T> v;
        private final VectorOp<T> tanh = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.tanh(e);
            }
        };
        private final VectorOp<T> tan = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.tan(e);
            }
        };
        private final VectorOp<T> sin = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.sin(e);
            }
        };
        private final VectorOp<T> cos = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.cos(e);
            }
        };
        private final VectorOp<T> real = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.real(e);
            }
        };
        private final VectorOp<T> imag = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.imag(e);
            }
        };
        private final VectorOp<T> abs = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.abs(e);
            }
        };
        private final VectorOp<T> abssqr = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.abssqr(e);
            }
        };
        private final VectorOp<T> neg = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.neg(e);
            }
        };
        private final VectorOp<T> conj = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.conj(e);
            }
        };
        private final VectorOp<T> inv = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.inv(e);
            }
        };
        private final VectorOp<T> cotan = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.cotan(e);
            }
        };
        private final VectorOp<T> sinh = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.sinh(e);
            }
        };
        private final VectorOp<T> cosh = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.cosh(e);
            }
        };
        private final VectorOp<T> sincard = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.sincard(e);
            }
        };
        private final VectorOp<T> cotanh = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.cotanh(e);
            }
        };
        private final VectorOp<T> asinh = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.asinh(e);
            }
        };
        private final VectorOp<T> acosh = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.acosh(e);
            }
        };
        private final VectorOp<T> asin = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.asin(e);
            }
        };
        private final VectorOp<T> acos = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.acos(e);
            }
        };
        private final VectorOp<T> atan = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.atan(e);
            }
        };
        private final VectorOp<T> arg = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.arg(e);
            }
        };
        private final VectorOp<T> acotan = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.acotan(e);
            }
        };
        private final VectorOp<T> exp = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.exp(e);
            }
        };
        private final VectorOp<T> log = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.log(e);
            }
        };
        private final VectorOp<T> log10 = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.log10(e);
            }
        };
        private final VectorOp<T> db = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.db(e);
            }
        };
        private final VectorOp<T> db2 = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.db2(e);
            }
        };
        private final VectorOp<T> sqr = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.sqr(e);
            }
        };
        private final VectorOp<T> sqrt = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.sqrt(e);
            }
        };
        private final VectorOp<T> not = new VectorOp<T>() {
            @Override
            public T eval(int index, T e) {
                return v.not(e);
            }
        };

        public OpsImpl(VectorSpace<T> v) {
            this.v = v;
        }

        public VectorOp<T> real() {
            return real;
        }

        public VectorOp<T> imag() {
            return imag;
        }

        public VectorOp<T> abs() {
            return abs;
        }

        public VectorOp<T> abssqr() {
            return abssqr;
        }

        public VectorOp<T> neg() {
            return neg;
        }

        public VectorOp<T> conj() {
            return conj;
        }

        public VectorOp<T> inv() {
            return inv;
        }

        public VectorOp<T> sin() {
            return sin;
        }

        public VectorOp<T> cos() {
            return cos;
        }

        public VectorOp<T> tan() {
            return tan;
        }

        public VectorOp<T> cotan() {
            return cotan;
        }

        public VectorOp<T> sinh() {
            return sinh;
        }

        public VectorOp<T> sincard() {
            return sincard;
        }

        public VectorOp<T> cosh() {
            return cosh;
        }

        public VectorOp<T> tanh() {
            return tanh;
        }

        public VectorOp<T> cotanh() {
            return cotanh;
        }

        public VectorOp<T> asinh() {
            return asinh;
        }

        public VectorOp<T> acosh() {
            return acosh;
        }

        public VectorOp<T> asin() {
            return asin;
        }

        public VectorOp<T> acos() {
            return acos;
        }

        public VectorOp<T> atan() {
            return atan;
        }

        public VectorOp<T> arg() {
            return arg;
        }

        public VectorOp<T> acotan() {
            return acotan;
        }

        public VectorOp<T> exp() {
            return exp;
        }

        public VectorOp<T> log() {
            return log;
        }

        public VectorOp<T> log10() {
            return log10;
        }

        public VectorOp<T> db() {
            return db;
        }

        public VectorOp<T> db2() {
            return db2;
        }

        public VectorOp<T> sqr() {
            return sqr;
        }

        public VectorOp<T> sqrt() {
            return sqrt;
        }

        public VectorOp<T> not() {
            return not;
        }
    }
}
