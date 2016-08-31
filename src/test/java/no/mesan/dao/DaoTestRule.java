package no.mesan.dao;

import io.dropwizard.java8.jdbi.OptionalContainerFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.flywaydb.core.Flyway;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.skife.jdbi.v2.DBI;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.Arrays;

class DaoTestRule<T> implements MethodRule {
    private static final DBI jdbi;
    private static final DataSource dataSource;
    private static final String DATA_SET_PATH = "db/dataset/";
    private T dao;

    static {
        dataSource = new JDBCDataSource();
        ((JDBCDataSource)dataSource).setUrl("mem;sql.syntax_pgs=true;shutdown=true;");
        ((JDBCDataSource)dataSource).setUser("frisk");
        ((JDBCDataSource)dataSource).setPassword("");

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.clean();
        flyway.migrate();

        jdbi = new DBI(dataSource);
        jdbi.registerContainerFactory(new OptionalContainerFactory());
    }

    DaoTestRule(Class<T> clazz) {
        dao = jdbi.onDemand(clazz);
    }

    T getDao() {
        return dao;
    }

    private void initDataSet(String dataSetPath) {
        try {
            IDatabaseConnection databaseConnection = new DatabaseConnection(dataSource.getConnection());
            databaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "\"");
            IDataSet dataSet = new YamlDataSet(
                    new File(ClassLoader.getSystemResource(DATA_SET_PATH + dataSetPath).getFile()));
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        } catch (DatabaseUnitException | SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface DataSet {
        String[] value();
    }

    @Override
    public Statement apply(Statement statement, FrameworkMethod frameworkMethod, Object o) {
        DataSet dataSet = frameworkMethod.getAnnotation(DataSet.class);
        if (dataSet != null) {
            Arrays.stream(dataSet.value()).forEach(this::initDataSet);
        }
        return statement;
    }
}
