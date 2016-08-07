package no.mesan.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;


public class FriskConfiguration extends Configuration {
    private DataSourceFactory database = new DataSourceFactory();
    private OpenIdConfiguration openIdConfiguration;

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory database) {
        this.database = database;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("openId")
    public OpenIdConfiguration getOpenIdconfiguration() {
        return openIdConfiguration;
    }

    @JsonProperty("openId")
    public void setOpenIdConfiguration(OpenIdConfiguration openIdConfiguration) {
        this.openIdConfiguration = openIdConfiguration;
    }
}
