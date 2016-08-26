package no.mesan;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.java8.auth.AuthDynamicFeature;
import io.dropwizard.java8.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.java8.jdbi.DBIFactory;
import io.dropwizard.java8.jdbi.OptionalContainerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;
import no.mesan.auth.AuthenticationService;
import no.mesan.auth.DiscoveryDocumentCache;
import no.mesan.auth.OpenIdAuthenticator;
import no.mesan.auth.OpenIdUtil;
import no.mesan.config.FriskConfiguration;
import no.mesan.dao.LeaderboardDao;
import no.mesan.dao.LogDao;
import no.mesan.dao.TypeDao;
import no.mesan.dao.UserDao;
import no.mesan.model.User;
import no.mesan.resource.LeaderboardResource;
import no.mesan.resource.LogResource;
import no.mesan.resource.LoginResource;
import no.mesan.resource.TypeResource;
import no.mesan.service.LeaderboardService;
import no.mesan.service.LogService;
import no.mesan.service.TypeService;
import no.mesan.websocket.LogUpdate;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;

public class FriskApplication extends Application<FriskConfiguration> {
    public static void main(String[] args) throws Exception {
        new FriskApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<FriskConfiguration> bootstrap) {
        bootstrap.addBundle(new WebsocketBundle(LogUpdate.class));
    }

    @Override
    public void run(FriskConfiguration configuration, Environment environment) throws Exception {
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword());
        flyway.migrate();

        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, dataSourceFactory, "postgresql");
        jdbi.registerContainerFactory(new OptionalContainerFactory());

        // TODO: This line can be removed when upgrading to dropwizard 1.0.0
        environment.getObjectMapper().registerModule(new JavaTimeModule());
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new OpenIdAuthenticator(
                                jdbi.onDemand(UserDao.class),
                                configuration.getOpenIdconfiguration().getJwtSecret()))
                        .setPrefix("Bearer")
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        environment.jersey().register(new LoginResource(
                new AuthenticationService(
                        new OpenIdUtil(
                                configuration.getOpenIdconfiguration(),
                                new DiscoveryDocumentCache(configuration.getOpenIdconfiguration().getDiscoveryDocumentUrl())),
                        jdbi.onDemand(UserDao.class))));
        environment.jersey().register(new LogResource(
                new LogService(jdbi.onDemand(LogDao.class))
        ));
        environment.jersey().register(new LeaderboardResource(
                new LeaderboardService(jdbi.onDemand(LeaderboardDao.class))
        ));
        environment.jersey().register(new TypeResource(
                new TypeService(jdbi.onDemand(TypeDao.class))
        ));
    }
}
