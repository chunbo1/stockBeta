package com.restapi;

import java.util.Map;

public interface RestApiHandler {
    <TReturnEntity> TReturnEntity get(Class<TReturnEntity> clazz, String url, String resource, Map<String, Object> queryParams, String username, String password);
    <TInputEntity, TReturnEntity> TReturnEntity post(Class<TReturnEntity> clazz, String url, TInputEntity payload);
    <TInputEntity, TReturnEntity> TReturnEntity post(Class<TReturnEntity> clazz, String url, String resource, TInputEntity payload, Map<String, Object> queryParams, String username, String password);

    <TReturnEntity> TReturnEntity delete(Class<TReturnEntity> clazz, String url, String resource);
    <TInputEntity, TReturnEntity> TReturnEntity put(Class<TReturnEntity> clazz, String url, String resource, TInputEntity payload);
}
