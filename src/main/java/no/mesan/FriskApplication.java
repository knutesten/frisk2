package no.mesan;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import no.mesan.config.FriskConfiguration;
import no.mesan.resource.TestResource;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;

/**
 * Created by knutn on 7/19/2016.
 */
public class FriskApplication extends Application<FriskConfiguration> {
    public static void main(String[] args) throws Exception {
        new FriskApplication().run(args);
    }

    @Override
    public void run(FriskConfiguration configuration, Environment environment) throws Exception {
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword());
        flyway.migrate();


        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, dataSourceFactory, "postgresql");
        environment.jersey().register(new TestResource());
    }
}
