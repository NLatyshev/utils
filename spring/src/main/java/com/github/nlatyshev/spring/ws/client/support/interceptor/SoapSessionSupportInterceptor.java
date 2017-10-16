package com.github.nlatyshev.spring.ws.client.support.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import java.util.Arrays;

/**
 * Client-side thread-safe interceptor that adds session support to SOAP interaction.
 * <p/>
 * When response contains 'Set-Cookie' header, this interceptor saves header values and holds it between requests.
 * The interceptor intercepts requests and adds 'Cookie' header with held values.
 *
 * @author NLatyshev
 */
public class SoapSessionSupportInterceptor implements ClientInterceptor {

    private static final String COOKIE_HEADER = "Cookie";
    private static final String SET_COOKIE_HEADER = "Set-Cookie";
    private static final Log LOGGER = LogFactory.getLog(SoapSessionSupportInterceptor.class);
    private ThreadLocal<String[]> sessionHolder = new ThreadLocal<String[]>();

    /**
     * Add 'Cookie' header with held 'Set-Cookie' header values.
     *
     * @param messageContext the message context
     * @return always <code>true</code>
     * @throws WebServiceClientException
     */
    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        if (sessionHolder.get() != null) {
            MimeHeaders headers = getMimeHeaders(messageContext.getRequest());
            if (headers != null) {
                String[] cookieHeaderValues = sessionHolder.get();
                LOGGER.debug(String.format("Set cookies %s to request", Arrays.toString(cookieHeaderValues)));
                for (String value : cookieHeaderValues) {
                    headers.addHeader(COOKIE_HEADER, value);
                }
            }
        }
        return true;
    }

    /**
     * Extract from response 'Set-Cookie' header values and save it to ThreadLocal.
     *
     * @param messageContext the message context
     * @return always <code>true</code>
     * @throws WebServiceClientException
     */
    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        if (sessionHolder.get() == null) {
            MimeHeaders headers = getMimeHeaders(messageContext.getResponse());
            if (headers != null) {
                String[] setCookieHeaderValues = headers.getHeader(SET_COOKIE_HEADER);
                LOGGER.debug(String.format("Get cookies %s from response", Arrays.toString(setCookieHeaderValues)));
                sessionHolder.set(setCookieHeaderValues);
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {}

    private MimeHeaders getMimeHeaders(WebServiceMessage message) {
        if (message instanceof SaajSoapMessage) {
            SOAPMessage soapMessage = ((SaajSoapMessage) message).getSaajMessage();
            if (soapMessage != null) {
                return soapMessage.getMimeHeaders();
            }
        }
        return null;
    }
}
