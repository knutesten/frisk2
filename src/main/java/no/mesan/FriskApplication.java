package no.mesan;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.java8.auth.AuthDynamicFeature;
import io.dropwizard.java8.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import no.mesan.auth.AuthenticationService;
import no.mesan.auth.OpenIdUtil;
import no.mesan.auth.OpenIdAuthenticator;
import no.mesan.config.FriskConfiguration;
import no.mesan.dao.UserDao;
import no.mesan.model.User;
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

        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new OpenIdAuthenticator(jdbi.onDemand(UserDao.class)))
                        .setPrefix("Bearer")
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        environment.jersey().register(new TestResource(jdbi.onDemand(UserDao.class)));
        environment.jersey().register(new LoginResource(
                new AuthenticationService(
                        new OpenIdUtil(configuration.getOpenIdconfiguration()))));
    }
}
