package com.restapi;

import lombok.var;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

public class RestApiHandlerImpl implements RestApiHandler {
    @Override
    public <TReturnEntity> TReturnEntity get(Class<TReturnEntity> clazz, String baseUrl, String resource,
                                             Map<String, Object> queryParams,
                                             String username, String password) {
        Client client = ClientBuilder.newClient();
        if (username!= null && password != null) {
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
            client.register(feature);
        }
        WebTarget webtarget = client.target(baseUrl);
        if (resource != null)
            webtarget = webtarget.path(resource);
        if (queryParams != null && queryParams.size()>0) {
            for (var param : queryParams.entrySet()) {
                webtarget = webtarget.queryParam(param.getKey(), param.getValue());
            }
        }
        Invocation.Builder invokationBuilder = webtarget.request(MediaType.APPLICATION_JSON);
        TReturnEntity resultObject = invokationBuilder.get(clazz);
        return  resultObject;
    }

    @Override
    public <TInputEntity, TReturnEntity> TReturnEntity post(Class<TReturnEntity> clazz, String url, TInputEntity payload) {
        Client client = ClientBuilder.newClient();
        WebTarget webtarget = client.target(url);
        Invocation.Builder invokationBuilder = webtarget.request(MediaType.APPLICATION_JSON);
        TReturnEntity resultObject = invokationBuilder.post(Entity.entity(payload, MediaType.APPLICATION_JSON), clazz);
        return  resultObject;
    }

    @Override
    public <TInputEntity, TReturnEntity> TReturnEntity post(Class<TReturnEntity> clazz, String baseUrl, String resource,
                                                            TInputEntity payload,
                                                            Map<String, Object> queryParams,
                                                            String username, String password) {
        Client client = ClientBuilder.newClient();
        if (username!= null && password != null) {
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
            client.register(feature);
        }
        WebTarget webtarget = client.target(baseUrl);
        if (resource != null)
            webtarget = webtarget.path(resource);
        if (queryParams != null && queryParams.size()>0) {
            for (var param : queryParams.entrySet()) {
                webtarget = webtarget.queryParam(param.getKey(), param.getValue());
            }
        }

        Invocation.Builder invokationBuilder = webtarget.request(MediaType.APPLICATION_JSON);
        TReturnEntity resultObject = invokationBuilder.post(Entity.entity(payload, MediaType.APPLICATION_JSON), clazz);
        return  resultObject;
    }

    @Override
    public <TReturnEntity> TReturnEntity delete(Class<TReturnEntity> clazz, String url, String resource) {
        Client client = ClientBuilder.newClient();
        WebTarget webtarget = client.target(url);
        if (resource != null)
            webtarget = webtarget.path(resource);
        Invocation.Builder invokationBuilder = webtarget.request(MediaType.APPLICATION_JSON);
        TReturnEntity resultObject = invokationBuilder.delete(clazz);
        return  resultObject;

    }

    @Override
    public <TInputEntity, TReturnEntity> TReturnEntity put(Class<TReturnEntity> clazz, String url, String resource, TInputEntity payload) {
        Client client = ClientBuilder.newClient();
        WebTarget webtarget = client.target(url);
        if (resource != null)
            webtarget = webtarget.path(resource);
        Invocation.Builder invokationBuilder = webtarget.request(MediaType.APPLICATION_JSON);
        TReturnEntity resultObject = invokationBuilder.put(Entity.entity(payload, MediaType.APPLICATION_JSON), clazz);
        return  resultObject;
    }
}
