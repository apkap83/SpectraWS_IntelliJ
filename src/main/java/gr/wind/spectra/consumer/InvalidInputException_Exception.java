
package gr.wind.spectra.consumer;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InvalidInputException", targetNamespace = "http://web.spectra.wind.gr/")
public class InvalidInputException_Exception
    extends java.lang.Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InvalidInputException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public InvalidInputException_Exception(String message, InvalidInputException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public InvalidInputException_Exception(String message, InvalidInputException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: gr.wind.spectra.consumer.InvalidInputException
     */
    public InvalidInputException getFaultInfo() {
        return faultInfo;
    }

}
