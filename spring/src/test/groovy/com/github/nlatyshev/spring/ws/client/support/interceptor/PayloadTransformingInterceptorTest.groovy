package com.github.nlatyshev.spring.ws.client.support.interceptor

import com.github.nlatyshev.spring.ws.client.support.interceptor.diffgram.schema.WebServiceOneOperation
import com.github.nlatyshev.spring.ws.client.support.interceptor.diffgram.schema.WebServiceOneOperationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.test.client.MockWebServiceServer
import org.springframework.xml.transform.StringSource
import spock.lang.Specification

import static org.springframework.ws.test.client.RequestMatchers.payload
import static org.springframework.ws.test.client.ResponseCreators.withPayload

/**
 * @author nlatyshev on 30.05.2017.
 */
@ContextConfiguration(locations = ['classpath:test-application-context.xml'])
class PayloadTransformingInterceptorTest extends Specification {

    @Autowired
    WebServiceTemplate wsTemplate

    MockWebServiceServer mockServer

    def setup() {
        mockServer = MockWebServiceServer.createServer(wsTemplate)
    }

    def 'Remove schema and diffgr elements from response'() {
        setup:
            mockServer.expect(payload(new StringSource('''
            <web:WebServiceOneOperation xmlns:web="http://www.github.com/WebServiceOne">
                <in>Hello</in>
            </web:WebServiceOneOperation>''')))
            .andRespond(withPayload(new StringSource('''
            <web:WebServiceOneOperationResponse xmlns:web="http://www.github.com/WebServiceOne">
                <HelloResult>
                    <xs:schema id="out" xmlns="" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msdata="urn:schemas-microsoft-com:xml-msdata">
                        <xs:element name="out" msdata:IsDataSet="true" type="xsd:string"/>
                    </xs:schema>
                    <diffgr:diffgram xmlns:msdata="urn:schemas-microsoft-com:xml-msdata" xmlns:diffgr="urn:schemas-microsoft-com:xml-diffgram-v1">
                        <out>Hi</out>
                    </diffgr:diffgram>
                </HelloResult>
            </web:WebServiceOneOperationResponse>''')))
        when:
            def request = new WebServiceOneOperation()
            request.setIn('Hello')
            def response = (WebServiceOneOperationResponse) wsTemplate.marshalSendAndReceive('http://www.github.com/WebServiceOne', request)
        then:
            response.helloResult.out == 'Hi'
    }
}
