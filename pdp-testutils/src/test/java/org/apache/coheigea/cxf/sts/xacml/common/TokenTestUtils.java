/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.coheigea.cxf.sts.xacml.common;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.trust.STSClient;

public final class TokenTestUtils {
    
    private TokenTestUtils() {
        // complete
    }
    
    public static void updateSTSPort(BindingProvider p, String port) {
        STSClient stsClient = (STSClient)p.getRequestContext().get(SecurityConstants.STS_CLIENT);
        if (stsClient != null) {
            String location = stsClient.getWsdlLocation();
            if (location != null && location.contains("8080")) {
                stsClient.setWsdlLocation(location.replace("8080", port));
            } else if (location != null && location.contains("8443")) {
                stsClient.setWsdlLocation(location.replace("8443", port));
            }
        }
        stsClient = (STSClient)p.getRequestContext().get(SecurityConstants.STS_CLIENT + ".sct");
        if (stsClient != null) {
            String location = stsClient.getWsdlLocation();
            if (location.contains("8080")) {
                stsClient.setWsdlLocation(location.replace("8080", port));
            } else if (location.contains("8443")) {
                stsClient.setWsdlLocation(location.replace("8443", port));
            }
        }
    }

}
