package no.mesan;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import no.mesan.config.FriskConfiguration;
import no.mesan.resource.LoginResource;
import no.mesan.resource.TestResource;
import org.flywaydb.core.Flyway;

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
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(configuration.getDataSourceFactory().getUrl());
        System.out.println(configuration.getDataSourceFactory().getPassword());
        System.out.println(configuration.getDataSourceFactory().getUser());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword());
        //flyway.migrate();


        //final DBIFactory factory = new DBIFactory();
        //final DBI jdbi = factory.build(environment, dataSourceFactory, "postgresql");
        environment.jersey().register(new TestResource());
        environment.jersey().register(new LoginResource());
    }
}
