package com.github.nlatyshev.spring.ws.client.support.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author NLatyshev
 */
@RunWith(MockitoJUnitRunner.class)
public class SoapSessionSupportInterceptorTest {

    public static final String COOKIE_HEADER = "Cookie";
    public static final String SET_COOKIE_HEADER = "Set-Cookie";
    private SoapSessionSupportInterceptor sessionSupportInterceptor;

    @Mock
    private MessageContext messageContext;
    @Mock
    private SaajSoapMessage saajMessage;
    @Mock
    private SOAPMessage soapMessage;
    @Mock
    private MimeHeaders mimeHeaders;
    @Captor
    private ArgumentCaptor<String> captor;

    public static final String[] COOKIE_VALUES = new String[]{"h1", "h2"};

    @Before
    public void setUp() throws Exception {
        sessionSupportInterceptor = new SoapSessionSupportInterceptor();
    }

    @Test
    public void testResponseIsNull() {
        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testResponseNotSaajMessage() {
        WebServiceMessage notSaajMessage = mock(WebServiceMessage.class);
        when(messageContext.getResponse()).thenReturn(notSaajMessage);

        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testResponseSoapMessageIsNull() {
        when(messageContext.getResponse()).thenReturn(saajMessage);

        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testResponseMimeHeadersIsNull() {
        when(messageContext.getResponse()).thenReturn(saajMessage);
        when(saajMessage.getSaajMessage()).thenReturn(soapMessage);

        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testSetCookieHeaderValueIsNull() {
        when(messageContext.getResponse()).thenReturn(saajMessage);
        when(saajMessage.getSaajMessage()).thenReturn(soapMessage);
        when(soapMessage.getMimeHeaders()).thenReturn(mimeHeaders);

        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testRequestIsNull() {
        stubHandleResponse(COOKIE_VALUES);

        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testRequestNotSaajMessage() {
        stubHandleResponse(COOKIE_VALUES);
        WebServiceMessage notSaajMessage = mock(WebServiceMessage.class);
        when(messageContext.getRequest()).thenReturn(notSaajMessage);

        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testRequestSoapMessageIsNull() {
        stubHandleResponse(COOKIE_VALUES);
        sessionSupportInterceptor.handleResponse(messageContext);

        when(saajMessage.getSaajMessage()).thenReturn(null);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testRequestMimeHeadersIsNull() {
        stubHandleResponse(COOKIE_VALUES);
        sessionSupportInterceptor.handleResponse(messageContext);

        when(soapMessage.getMimeHeaders()).thenReturn(null);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, never()).addHeader(eq(COOKIE_HEADER), Matchers.<String>any());
    }

    @Test
    public void testExtractAndAddToRequestCookie() {
        stubHandleResponse(COOKIE_VALUES);
        when(messageContext.getRequest()).thenReturn(saajMessage);

        sessionSupportInterceptor.handleResponse(messageContext);
        sessionSupportInterceptor.handleRequest(messageContext);

        verify(mimeHeaders, times(2)).addHeader(eq(COOKIE_HEADER), captor.capture());
        assertEquals(new HashSet<String>(Arrays.asList(COOKIE_VALUES)), new HashSet<String>(captor.getAllValues()));
    }

    @Test
    public void testOnlyTimeExtractCookie() {
        stubHandleResponse(COOKIE_VALUES);
        sessionSupportInterceptor.handleResponse(messageContext);

        when(messageContext.getRequest()).thenReturn(saajMessage);
        sessionSupportInterceptor.handleRequest(messageContext);
        verify(mimeHeaders, times(2)).addHeader(eq(COOKIE_HEADER), captor.capture());
        assertEquals(new HashSet<String>(Arrays.asList(COOKIE_VALUES)), new HashSet<String>(captor.getAllValues()));
        reset(mimeHeaders);

        stubHandleResponse(new String[]{"h3"});
        sessionSupportInterceptor.handleResponse(messageContext);

        sessionSupportInterceptor.handleRequest(messageContext);
        verify(mimeHeaders, times(2)).addHeader(eq(COOKIE_HEADER), captor.capture());
        assertEquals(new HashSet<String>(Arrays.asList(COOKIE_VALUES)), new HashSet<String>(captor.getAllValues()));
    }

    private void stubHandleResponse(String[] cookies) {
        when(messageContext.getResponse()).thenReturn(saajMessage);
        when(saajMessage.getSaajMessage()).thenReturn(soapMessage);
        when(soapMessage.getMimeHeaders()).thenReturn(mimeHeaders);
        when(mimeHeaders.getHeader(SET_COOKIE_HEADER)).thenReturn(cookies);
    }
}
