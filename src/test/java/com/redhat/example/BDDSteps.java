package com.redhat.example;

import static org.junit.Assert.assertEquals;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import io.cucumber.java8.En;

public class BDDSteps implements En {

    private String url;
    
    private String applicationContext;

    private String actualValue;

    private Integer httpResponseCode;
    
    public BDDSteps(){
        Given("an instance of JBoss EAP 7.3 is ready to serve request at URL {string}", (final String url) -> {
            this.url = url;
        });
        
        And("the test application is deployed with the context {string}", (final String applicationContext) -> {
            this.applicationContext = applicationContext;
        });
        
        When("the endpoint {string} is invoked using the http method {string} for the key {string}", (final String endpoint, final String httpMethod, final String key) -> {
            get(endpoint, key);
        });
        
        When("the endpoint {string} is invoked using the http method {string} for the key {string} and {string} as value", (final String endpoint, final String httpMethod, final String key, final String value) -> {
            put(endpoint, key, value);
        });
    
        And("the response value is {string}", (final String expectedValue) -> {
            assertEquals(expectedValue, this.actualValue);
        });

        And("the http response code is {int}", (final Integer expectedHttpResponseCode) -> {
            assertEquals(expectedHttpResponseCode, this.httpResponseCode);
        });
    }

    private ResteasyWebTarget createClient(String endpoint, final String key) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final SSLContextBuilder sshbuilder = new SSLContextBuilder();
        sshbuilder.loadTrustMaterial(null, new TrustAllStrategy()); // Accept self signed certs :(
        final ResteasyClient client = new ResteasyClientBuilder().sslContext(sshbuilder.build()).build();
        final ResteasyWebTarget target = client.target(this.url + this.applicationContext + endpoint + "/" + key);
        target.request(MediaType.APPLICATION_JSON);
        return target;     
    }

    private void get(String endpoint, final String key) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final ResteasyWebTarget target = createClient(endpoint, key);
        final Response response = target.request().get();
        this.actualValue = response.readEntity(String.class);
        this.httpResponseCode = response.getStatus();
        response.close();
    }

    private void put(String endpoint, final String key, final String value) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        final ResteasyWebTarget target = createClient(endpoint, key);
        final Response response = target.request().put(Entity.entity(value, MediaType.APPLICATION_JSON));
        this.httpResponseCode = response.getStatus();
        response.close();
    }
}
