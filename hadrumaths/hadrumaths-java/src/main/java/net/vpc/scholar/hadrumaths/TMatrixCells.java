package net.vpc.scholar.hadrumaths;

public class TMatrixCells {
    public static <T> TVectorCell<T> vectorForArray(T[] elements) {
        return new TVectorCell<T>() {
            @Override
            public T get(int index) {
                return elements[index];
            }
        };
    }

    public static <T> TMatrixCell<T> matrixForConstant(T element) {
        return new TMatrixCell<T>() {
            @Override
            public T get(int row, int col) {
                return element;
            }
        };
    }

    public static <T> TMatrixCell<T> columnForArray(T[] elements) {
        return new TMatrixCell<T>() {
            @Override
            public T get(int row, int col) {
                return elements[row];
            }
        };
    }

    public static <T> TMatrixCell<T> rowForArray(T[] elements) {
        return new TMatrixCell<T>() {
            @Override
            public T get(int row, int col) {
                return elements[col];
            }
        };
    }

    public static <T> TMatrixCell<T> columnForVector(TVectorCell<T> elements) {
        return new TVectorCellToMatrixColumn<>(elements);
    }

    public static <T> TMatrixCell<T> rowForVector(TVectorCell<T> elements) {
        return new TVectorCellToMatrixRow<>(elements);
    }

    private static class TVectorCellToMatrixColumn<T> implements TMatrixCell<T>{
        private TVectorCell<T> value;

        public TVectorCellToMatrixColumn(TVectorCell<T> value) {
            this.value = value;
        }

        @Override
        public T get(int row, int column) {
            return value.get(row);
        }
    }

    private static class TVectorCellToMatrixRow<T> implements TMatrixCell<T>{
        private TVectorCell<T> value;

        public TVectorCellToMatrixRow(TVectorCell<T> value) {
            this.value = value;
        }

        @Override
        public T get(int row, int column) {
            return value.get(column);
        }
    }

}
