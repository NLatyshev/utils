package com.github.nlatyshev.spring.ws.client.support.interceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.xml.transform.TransformerObjectSupport;

import javax.xml.transform.TransformerFactory;

/**
 * Interceptor that transforms the payload of <code>WebServiceMessage</code> using XSLT stylesheet.
 *
 * @author NLatyshev
 * @see org.springframework.ws.server.endpoint.interceptor.PayloadTransformingInterceptor
 */
public class PayloadTransformingInterceptor extends TransformerObjectSupport implements ClientInterceptor, InitializingBean {

    private org.springframework.ws.server.endpoint.interceptor.PayloadTransformingInterceptor transformingInterceptor =
            new org.springframework.ws.server.endpoint.interceptor.PayloadTransformingInterceptor();

    /**
     * Sets the XSLT stylesheet to use for transforming incoming request.
     */
    public void setRequestXslt(Resource requestXslt) {
        transformingInterceptor.setRequestXslt(requestXslt);
    }

    /**
     * Sets the XSLT stylesheet to use for transforming outgoing responses.
     */
    public void setResponseXslt(Resource responseXslt) {
        transformingInterceptor.setResponseXslt(responseXslt);
    }

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        try {
            return transformingInterceptor.handleRequest(messageContext, null);
        } catch (Exception e) {
            throw new WebServiceClientException(e.getMessage(), e) {
            };
        }
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        try {
            return transformingInterceptor.handleResponse(messageContext, null);
        } catch (Exception e) {
            throw new WebServiceClientException(e.getMessage(), e) {
            };
        }
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {}

    @Override
    public void setTransformerFactoryClass(Class<? extends TransformerFactory> transformerFactoryClass) {
        transformingInterceptor.setTransformerFactoryClass(transformerFactoryClass);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        transformingInterceptor.afterPropertiesSet();
    }
}
