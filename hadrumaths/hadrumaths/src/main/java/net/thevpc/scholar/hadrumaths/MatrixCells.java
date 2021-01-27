package net.thevpc.scholar.hadrumaths;

public class MatrixCells {
    public static <T> VectorCell<T> vectorForArray(T[] elements) {
        return new VectorCell<T>() {
            @Override
            public T get(int index) {
                return elements[index];
            }
        };
    }

    public static <T> MatrixCell<T> matrixForConstant(T element) {
        return new MatrixCell<T>() {
            @Override
            public T get(int row, int col) {
                return element;
            }
        };
    }

    public static <T> MatrixCell<T> columnForArray(T[] elements) {
        return new MatrixCell<T>() {
            @Override
            public T get(int row, int col) {
                return elements[row];
            }
        };
    }

    public static <T> MatrixCell<T> rowForArray(T[] elements) {
        return new MatrixCell<T>() {
            @Override
            public T get(int row, int col) {
                return elements[col];
            }
        };
    }

    public static <T> MatrixCell<T> columnForVector(VectorCell<T> elements) {
        return new VectorCellToMatrixColumn<>(elements);
    }

    public static <T> MatrixCell<T> rowForVector(VectorCell<T> elements) {
        return new VectorCellToMatrixRow<>(elements);
    }

    private static class VectorCellToMatrixColumn<T> implements MatrixCell<T> {
        private final VectorCell<T> value;

        public VectorCellToMatrixColumn(VectorCell<T> value) {
            this.value = value;
        }

        @Override
        public T get(int row, int column) {
            return value.get(row);
        }
    }

    private static class VectorCellToMatrixRow<T> implements MatrixCell<T> {
        private final VectorCell<T> value;

        public VectorCellToMatrixRow(VectorCell<T> value) {
            this.value = value;
        }

        @Override
        public T get(int row, int column) {
            return value.get(column);
        }
    }

}
