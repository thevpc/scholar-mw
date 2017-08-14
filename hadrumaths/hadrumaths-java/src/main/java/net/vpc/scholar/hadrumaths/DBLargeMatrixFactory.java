package net.vpc.scholar.hadrumaths;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vpc on 2/5/15.
 */
public class DBLargeMatrixFactory extends LargeMatrixFactory {
    private static Logger log = Logger.getLogger(DBLargeMatrixFactory.class.getName());
    private Connection connection;
    private Map<String, PreparedStatement> cachedPreparedStatements;
    int hits = 0;

//    public static DBLargeMatrixFactory createStorage(String id, Connection connexion) {
//        DBLargeMatrixFactory factory = new DBLargeMatrixFactory(id, connexion, false, Complex.ZERO);
//        factory.prepare();
//        return factory;
//    }
//
//    public static DBLargeMatrixFactory createSparseStorage(String id, Connection connexion, Complex defaultValue) {
//        DBLargeMatrixFactory factory = new DBLargeMatrixFactory(id, connexion, true, defaultValue);
//        factory.prepare();
//        return factory;
//    }
//
//    public static DBLargeMatrixFactory createLocalStorage(String id, File storeFolder) {
//        if (StringUtils.isEmpty(id)) {
//            id = storeFolder.getPath();
//        }
//        return createStorage(id, createConnection(storeFolder));
//    }
//
//    public static DBLargeMatrixFactory createLocalSparseStorage(String id, File storeFolder) {
//        if (StringUtils.isEmpty(id)) {
//            id = storeFolder.getPath();
//        }
//        return createSparseStorage(id, createConnection(storeFolder), Complex.ZERO);
//    }

//    public static DBLargeMatrixFactory createLocalSparseStorage(String id, File storeFolder, Complex defaultValue) {
//        if (StringUtils.isEmpty(id)) {
//            id = storeFolder.getPath();
//        }
//        return createSparseStorage(id, createConnection(storeFolder), defaultValue);
//    }
//
//    public static DBLargeMatrixFactory createRemoteStorage(String id, String server, String name, String login, String password) {
//        if (StringUtils.isEmpty(id)) {
//            id = login + "@" + (StringUtils.isEmpty(server) ? "localhost" : server) + "/" + name;
//        }
//        return createStorage(id, createConnection(server, name, login, password));
//    }
//
//
//    public static DBLargeMatrixFactory createRemoteSparseStorage(String id, String server, String name, String login, String password) {
//        if (StringUtils.isEmpty(id)) {
//            id = login + "@" + (StringUtils.isEmpty(server) ? "localhost" : server) + "/" + name;
//        }
//        return createSparseStorage(id, createConnection(server, name, login, password), Complex.ZERO);
//    }
//
//
//    public static DBLargeMatrixFactory createRemoteSparseStorage(String id, String server, String name, String login, String password, Complex defaultValue) {
//        if (StringUtils.isEmpty(id)) {
//            id = login + "@" + (StringUtils.isEmpty(server) ? "localhost" : server) + "/" + name;
//        }
//        return createSparseStorage(id, createConnection(server, name, login, password), defaultValue);
//    }
//
//    public DBLargeMatrixFactory(String id, Connection cnx, boolean sparse, Complex defaultValue) {
//        super(id, sparse, defaultValue);
//        this.connection = cnx;
//        log.log(Level.FINE, "Create " + (sparse ? " Sparse (" + defaultValue + ") " : "") + " Large Matrix Factory to " + cnx);
//    }


    public DBLargeMatrixFactory(String id) {
        super();
        DBLargeMatrixId d = DBLargeMatrixId.parse(id);
        if(d==null){
            throw new IllegalArgumentException("Invalid Id");
        }
        init(id,d.isSparse(),d.getDefaultValue());
        this.connection = createConnection(d);
        prepare();
        log.log(Level.FINE, "Create " + (d.isSparse() ? " Sparse (" + d.getDefaultValue() + ") " : "") + " Large Matrix Factory to " + d);
    }


//    private static Connection createConnection(File storeFolder) {
//        try {
//            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
//            Connection connection = DriverManager.getConnection("jdbc:derby:" + storeFolder.getPath() + ";create=true");
//            connection.setAutoCommit(false);
//            // force garbage collection to unload the EmbeddedDriver
//            //  so Derby can be restarted
//            System.gc();
//            return connection;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static Connection createConnection(String server, String name, String login, String password) {
//        try {
//            Class.forName("org.apache.derby.jdbc.ClientDriver");
//            return DriverManager.getConnection("jdbc:derby://" + server + "/" + name, login, password);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String createId(String id){
        DBLargeMatrixId id2 = DBLargeMatrixId.parse(id);
        if(id2==null){
            return null;
        }
        return id2.toString();
    }

    public static String createLocalId(String storeFolder, boolean sparse, Complex defaultValue){
        return createLocalId(storeFolder,"hadrumaths","hadrumaths",sparse,defaultValue);
    }

    public static String createLocalId(String storeFolder, String login, String password, boolean sparse, Complex defaultValue){
        return new DBLargeMatrixId(
                "derby",
                login,password,
                "org.apache.derby.jdbc.ClientDriver",
                "jdbc:derby:" + storeFolder + ";create=true;user="+login+";password="+password,
                sparse,defaultValue
        ).toString();
    }
    public static String createRemoteId(String server, String name, String login, String password,boolean sparse,Complex defaultValue){
        return new DBLargeMatrixId(
                "derbynet",
                login,password,"org.apache.derby.jdbc.ClientDriver",
                "jdbc:derby://" + server + "/" + name,
                sparse,defaultValue
        ).toString();
    }

    private static Connection createConnection(DBLargeMatrixId id){
        try {
            if(id.getDriver().length()>0) {
                Class.forName(id.getDriver());
            }else if(id.getType().equals("derbynet")){
                Class.forName("org.apache.derby.jdbc.ClientDriver");
            }else if(id.getType().equals("derby")){
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            }
            String url = Maths.Config.replaceVars(id.getUrl());
            return DriverManager.getConnection(url, id.getLogin(), id.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void prepare() {
        try {
            String sql = "create table matrix(id integer NOT NULL GENERATED ALWAYS AS IDENTITY,r int, c int, creation_time timestamp)";
            log.log(Level.FINEST, getId() + " : sql = " + sql);
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            //ignore
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void dispose() {
        if (connection != null) {
            log.log(Level.FINE, "disposing Large Matrix Factory " + connection);
            try {
                if (!connection.isClosed()) {
                    connection.commit();
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

    @Override
    public Matrix newMatrix(int rows, int columns) {
        int id = 0;
        String tabname = null;
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String sql = "insert into matrix(r,c,creation_time) values(?,?,?)";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(rows, columns, time));
            PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, rows);
            ps.setInt(2, columns);
            ps.setTimestamp(3, time);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            } else {
                throw new IllegalArgumentException("Unable to create Matrix");
            }
            rs.close();
            ps.close();
            tabname = "m" + id;
//            ps.close();

            sql = "create table " + tabname + "(r integer, c integer,rval double,ival double, primary key(r,c))";
            log.log(Level.FINEST, getId() + " : sql = " + sql);
            ps = getConnection().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();

            sql = "create INDEX " + tabname + "_idx" + " on " + tabname + "(r , c )";
            log.log(Level.FINEST, getId() + " : sql = " + sql);
            ps = getConnection().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            dataUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return new DBLargeMatrix(id, rows, columns, this);
    }

    @Override
    public void close() {
        dispose();
    }

    public Matrix findMatrix(long id) {
        try {
            String sql = "select r,c from matrix where id=?";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(id));
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DBLargeMatrix(id, rs.getInt(1), rs.getInt(2), this);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void reset() {
        clear();
    }

    public void clear() {
        try {
            String sql = "select id,r,c from matrix";
            log.log(Level.FINEST, getId() + " : sql = " + sql);
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                disposeMatrix(id);
            }
            rs.close();
            ps.close();
            if(cachedPreparedStatements!=null) {
                cachedPreparedStatements.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Long getId(Matrix m) {
        if (m != null && m instanceof LargeMatrix) {
            return ((LargeMatrix) m).getLargeMatrixId();
        }
        return null;
    }

    public Complex get(long id, int row, int col) {
//        System.out.println("get:" + row + " ," + col);
        Complex c = Complex.ZERO;
        try {
            String sql = "select rval,ival from m" + id + " where r=? and c=?";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(row, col));
            PreparedStatement ps = preparedStatement(sql);
            ps.setInt(1, row);
            ps.setInt(2, col);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = Complex.valueOf(rs.getDouble(1), rs.getDouble(2));
            }
            rs.close();
            //ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return c;
    }

    public Complex[] getRow(long id, int row, int fromCol, int toCol) {
//        System.out.println("get:" + row + " ," + col);
        Complex[] c = new Complex[toCol - fromCol];
        try {
            String sql = "select c,rval,ival from m" + id + " where r=? and c>=? and c<? order by c";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(row, fromCol, toCol));
            PreparedStatement ps = preparedStatement(sql);
            ps.setInt(1, row);
            ps.setInt(2, fromCol);
            ps.setInt(3, toCol);
            ResultSet rs = ps.executeQuery();
            int lastCol = fromCol;
            Complex defaultValue = getDefaultValue();
            while (rs.next()) {
                int cc = rs.getInt(1);
                while (lastCol < cc) {
                    c[lastCol - fromCol] = defaultValue;
                    lastCol++;
                }
                c[cc - fromCol] = Complex.valueOf(rs.getDouble(2), rs.getDouble(3));
                lastCol++;
            }
            while (lastCol < toCol) {
                c[lastCol - fromCol] = defaultValue;
                lastCol++;
            }
            rs.close();
            //ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public Complex[] getColumn(long id, int column, int fromRow, int toRow) {
//        System.out.println("get:" + row + " ," + col);
        Complex[] c = new Complex[toRow - fromRow];
        try {
            String sql = "select r,rval,ival from m" + id + " where c=? and r>=? and r<? order by r";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(column, fromRow, toRow));
            PreparedStatement ps = preparedStatement(sql);
            ps.setInt(1, column);
            ps.setInt(2, fromRow);
            ps.setInt(3, toRow);
            ResultSet rs = ps.executeQuery();
            int lastRow = fromRow;
            while (rs.next()) {
                int cc = rs.getInt(1);
                while (lastRow < cc) {
                    c[lastRow - fromRow] = getDefaultValue();
                    lastRow++;
                }
                c[cc - fromRow] = Complex.valueOf(rs.getDouble(2), rs.getDouble(3));
                lastRow++;
            }
            while (lastRow < toRow) {
                c[lastRow - fromRow] = getDefaultValue();
                lastRow++;
            }
            rs.close();
            //ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public void set(long id, Complex val, int row, int col) {
//        System.out.println("set:" + row + " ," + col);
        if (isSparse() && getDefaultValue().equals(val)) {
            try {
                String sql = "delete from m" + id + " where r=? and c=?";
                log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(row, col));
                PreparedStatement ps = preparedStatement(sql);
                ps.setInt(1, row);
                ps.setInt(2, col);
                ps.executeUpdate();
                //ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String sql = "update m" + id + " set rval=?, ival=? where r=? and c=?";
                log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(val.getReal(), val.getImag(), row, col));
                PreparedStatement ps = preparedStatement(sql);
                ps.setDouble(1, val.getReal());
                ps.setDouble(2, val.getImag());
                ps.setInt(3, row);
                ps.setInt(4, col);
                int updates = ps.executeUpdate();
                //ps.close();
                if (updates == 0) {
                    sql = "insert into m" + id + " (rval, ival,r,c) values(?,?,?,?)";
                    log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(val.getReal(), val.getImag(), row, col));
                    ps = preparedStatement(sql);
                    ps.setDouble(1, val.getReal());
                    ps.setDouble(2, val.getImag());
                    ps.setInt(3, row);
                    ps.setInt(4, col);
                    ps.executeUpdate();
                    //ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        try {
            dataUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void resizeMatrix(long id, int rows, int columns) {
        try {
            String sql = "update matrix set r=?,c=? where id=?";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(rows, columns, id));
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, rows);
            ps.setInt(2, columns);
            ps.setLong(3, id);
            ps.executeUpdate();
            ps.close();
            sql = "delete from m" + id + " where r>=? or c>=?";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(rows, columns, id));
            ps = getConnection().prepareStatement(sql);
            ps.setInt(1, rows);
            ps.setInt(2, columns);
            ps.executeUpdate();
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void disposeMatrix(long id) {
        try {
            String sql = "drop table m" + id;
            log.log(Level.FINEST, getId() + " : sql = " + sql);
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            sql = "delete from matrix where id=?";
            log.log(Level.FINEST, getId() + " : sql = " + sql + " ; " + Arrays.asList(id));
            ps = getConnection().prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement preparedStatement(String sql) throws SQLException {
        if(cachedPreparedStatements==null){
            cachedPreparedStatements=new HashMap<String, PreparedStatement>();
        }
        PreparedStatement preparedStatement = cachedPreparedStatements.get(sql);
        if (preparedStatement == null) {
            preparedStatement = getConnection().prepareStatement(sql);
            cachedPreparedStatements.put(sql, preparedStatement);
        }
        return preparedStatement;
    }

    private void dataUpdated() throws SQLException {
        hits = (hits + 1) % 1000;
        if (hits == 0) {
            connection.commit();
        }
    }

    @Override
    public String toString() {
        return "LargeMatrixFactory{" +
                "connection=" + connection +
                ", resetOnClose=" + isResetOnClose() +
                ", sparse=" + isSparse() +
                ", defaultValue=" + getDefaultValue() +
                '}';
    }

}
