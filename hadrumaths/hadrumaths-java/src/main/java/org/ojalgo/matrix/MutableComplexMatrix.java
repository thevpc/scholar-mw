package org.ojalgo.matrix;

import org.ojalgo.ProgrammingError;
import org.ojalgo.matrix.decomposition.*;
import org.ojalgo.matrix.store.ComplexDenseStore;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.scalar.ComplexNumber;
import org.ojalgo.type.context.NumberContext;

import java.math.BigDecimal;

/**
 * do not move, need to bein this package
 */
public class MutableComplexMatrix extends org.ojalgo.matrix.AbstractMatrix<ComplexNumber> {

    public static final MatrixFactory<ComplexNumber> FACTORY = new MatrixFactory<ComplexNumber>(MutableComplexMatrix.class, ComplexDenseStore.FACTORY);

    public static MatrixBuilder<ComplexNumber> getBuilder(final int aRowDim, final int aColDim) {
        return FACTORY.getBuilder(aRowDim, aColDim);
    }

    public MutableComplexMatrix(final MatrixStore<ComplexNumber> aStore) {
        super(aStore);
    }

    public BasicMatrix enforce(final NumberContext aContext) {

        final ComplexDenseStore retVal = (ComplexDenseStore) ComplexDenseStore.FACTORY.copy(this.getStore());

        retVal.modifyAll(aContext.getComplexEnforceFunction());

        return this.getFactory().instantiate(retVal);
    }

    public BasicMatrix round(final NumberContext aContext) {

        final ComplexDenseStore retVal = (ComplexDenseStore) ComplexDenseStore.FACTORY.copy(this.getStore());

        retVal.modifyAll(aContext.getComplexRoundFunction());

        return this.getFactory().instantiate(retVal);
    }

    public BigDecimal toBigDecimal(final int aRow, final int aCol) {
        return new BigDecimal(this.getStore().doubleValue(aRow, aCol));
    }

    public ComplexNumber toComplexNumber(final int aRow, final int aCol) {
        return this.getStore().get(aRow, aCol);
    }

    @Override
    public PhysicalStore<ComplexNumber> toComplexStore() {
        return this.getStore().copy();
    }

    public String toString(final int aRow, final int aCol) {
        return this.toComplexNumber(aRow, aCol).toString();
    }

    @Override
    PhysicalStore<ComplexNumber> copyOf(final BasicMatrix aMtrx) {
        return aMtrx.toComplexStore();
    }

    @Override
    MatrixFactory<ComplexNumber> getFactory() {
        return FACTORY;
    }

    @Override
    MatrixStore<ComplexNumber> getStoreFrom(final BasicMatrix aMtrx) {
        if (aMtrx instanceof ComplexMatrix) {
            return ((ComplexMatrix) aMtrx).getStore();
        } else {
            return aMtrx.toComplexStore();
        }
    }

    @Override
    Eigenvalue<ComplexNumber> makeEigenvalueDelegate() {
        ProgrammingError.throwForIllegalInvocation();
        return null;
    }

    @Override
    LU<ComplexNumber> makeLUDelegate() {
        return LUDecomposition.makeComplex();
    }

    @Override
    QR<ComplexNumber> makeQRDelegate() {
        return QRDecomposition.makeComplex();
    }

    @Override
    SingularValue<ComplexNumber> makeSingularValueDelegate() {
        return SingularValueDecomposition.makeComplex();
    }

    public void put(int r,int c,ComplexNumber value){
        ((ComplexDenseStore)getStore()).set(r,c,value);
    }

}
