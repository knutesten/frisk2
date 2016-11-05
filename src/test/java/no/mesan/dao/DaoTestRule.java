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
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.Arrays;

class DaoTestRule<T> implements MethodRule {
    private static final DBI JDBI;
    private static final DataSource DATA_SOURCE;
    private static final Flyway FLYWAY;

    private T dao;

    static {
        DATA_SOURCE = new JDBCDataSource();
        ((JDBCDataSource) DATA_SOURCE).setUrl("mem;sql.syntax_pgs=true;shutdown=true;");
        ((JDBCDataSource) DATA_SOURCE).setUser("frisk");
        ((JDBCDataSource) DATA_SOURCE).setPassword("");

        FLYWAY = new Flyway();
        FLYWAY.setDataSource(DATA_SOURCE);

        JDBI = new DBI(DATA_SOURCE);
        JDBI.registerContainerFactory(new OptionalContainerFactory());
    }

    DaoTestRule(Class<T> clazz) {
        dao = JDBI.onDemand(clazz);
    }

    T getDao() {
        return dao;
    }

    private void initDataSet(String filePath) {
        try {
            IDatabaseConnection databaseConnection = new DatabaseConnection(DATA_SOURCE.getConnection());
            databaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "\"");
            IDataSet dataSet = new YamlDataSet(new File(filePath));
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        } catch (DatabaseUnitException | SQLException | FileNotFoundException | UnsupportedEncodingException e) {
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
        FLYWAY.clean();
        FLYWAY.migrate();

        DataSet dataSet = frameworkMethod.getAnnotation(DataSet.class);
        if (dataSet != null) {
            Arrays.stream(dataSet.value())
                    .forEach(it -> initDataSet(frameworkMethod.getDeclaringClass().getResource(it).getFile()));
        }

        return statement;
    }
}
