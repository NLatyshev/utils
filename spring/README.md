# Removing DiffGram element in web service messages
1. Some time ago I needed to integrate with unusual SOAP web service. It were using Microsoft’s [diffgram](http://msdn.microsoft.com/en-us/library/ms172088.aspx) elements and attributes in their output messages.  
I found a good [article](https://pragmaticintegrator.wordpress.com/2011/03/27/removing-diffgram-element-in-web-service-messages/) where author came across the same problem.
The module contains utility class implemented spring ws ClientInterceptor that transforms ws response based on xslt file and allows to use spring WebServiceTemplate as usual.
Simple xs transformation just removes exceeded schema element and extract real data from diffgr element from response such as:
```
<?xml version="1.0" encoding="utf-8"?>
<soap12:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:xsd="http://www.w3.org/2001/XMLSchema"
  xmlns:soap12="http://www.w3.org/2003/05/soap-envelope">
  <soap12:Body>
    <web:WebServiceOneOperationResponse xmlns:web="http://www.github.com/WebServiceOne">
        <HelloResult>
            <xs:schema id="out" xmlns="" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msdata="urn:schemas-microsoft-com:xml-msdata">
                <!-- exceeded schema element -->
            </xs:schema>
            <diffgr:diffgram xmlns:msdata="urn:schemas-microsoft-com:xml-msdata" xmlns:diffgr="urn:schemas-microsoft-com:xml-diffgram-v1">
                <!-- expected service response part -->
            </diffgr:diffgram>
        </HelloResult>
    </web:WebServiceOneOperationResponse>
  </soap12:Body>
</soap12:Envelope>
```

2. The module contains one more interceptor to add session support to SOAP

