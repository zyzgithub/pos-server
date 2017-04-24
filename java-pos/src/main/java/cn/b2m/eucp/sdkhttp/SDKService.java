/**
 * SDKService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.b2m.eucp.sdkhttp;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface SDKService extends Service {
    public String getSDKServiceAddress();

    public SDKClient getSDKService() throws ServiceException;

    public SDKClient getSDKService(URL portAddress) throws ServiceException;
}
