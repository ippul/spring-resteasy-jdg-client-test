Feature: As a consultant I want to test that the application deployed on openshift interact with datagrid as expected

    Scenario: As a consultant I want to add an entry on Data Grid and I want to be able to retreive the inserted entry
        Given an instance of JBoss EAP 7.3 is ready to serve request at URL 'https://eap-app-my-first-project.apps-crc.testing/'
        And the test application is deployed with the context 'spring-resteasy-jdg-1.0.0'
        When the endpoint '/jdg' is invoked using the http method 'PUT' for the key 'test-entry-key' and 'test entry value' as value
        Then the endpoint '/jdg' is invoked using the http method 'GET' for the key 'test-entry-key'
        And the response value is 'test entry value' 
        And the http response code is 200

    Scenario: As a consultant I want to test that the application return the correct http code if the service is invoked for a non existing key
        Given an instance of JBoss EAP 7.3 is ready to serve request at URL 'https://eap-app-my-first-project.apps-crc.testing/'
        And the test application is deployed with the context 'spring-resteasy-jdg-1.0.0'
        When the endpoint '/jdg' is invoked using the http method 'GET' for the key 'test-entry-not-existing-key'
        Then the http response code is 204