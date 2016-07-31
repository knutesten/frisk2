package no.mesan;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import no.mesan.config.FriskConfiguration;
import no.mesan.resource.LoginResource;
import no.mesan.resource.TestResource;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;

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

        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, dataSourceFactory, "postgresql");
        environment.jersey().register(new TestResource());
        environment.jersey().register(new LoginResource());
    }
}
