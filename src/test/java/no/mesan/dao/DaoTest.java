package no.mesan.dao;

import io.dropwizard.java8.jdbi.OptionalContainerFactory;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.flywaydb.core.Flyway;
import org.hsqldb.jdbc.JDBCDataSource;
import org.skife.jdbi.v2.DBI;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.ParameterizedType;

class DaoTest<T> {
    private static final DBI jdbi;
    private static final DataSource dataSource;

    static {
        dataSource = new JDBCDataSource();
        ((JDBCDataSource)dataSource).setUrl("mem:test;sql.syntax_pgs=true;shutdown=true;");
        ((JDBCDataSource)dataSource).setUser("frisk");
        ((JDBCDataSource)dataSource).setPassword("");

        jdbi = new DBI(dataSource);
        jdbi.registerContainerFactory(new OptionalContainerFactory());
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

//    void initDataSet(String datasetPath) {
//        final IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File(dataSetXml));
//        final IDatabaseTester databaseTester = new JdbcDatabaseTester(dataSource.getConnection());
//        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
//        databaseTester.setDataSet(dataSet);
//        databaseTester.onSetup();
//    }

    @SuppressWarnings("unchecked")
    T dao = jdbi.onDemand((Class<T>)
            ((ParameterizedType)getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0]);
}
