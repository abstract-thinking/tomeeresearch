/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.superbiz.calculator.ws;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


class CalculatorTest {

    //Random port to avoid test conflicts
    private static final int PORT = Integer.parseInt(System.getProperty("httpejbd.port", "" + org.apache.openejb.util.NetworkUtil.getNextAvailablePort()));
    // private static final int PORT = 8080;

    @BeforeAll
    public static void setUp() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("openejb.embedded.remotable", "true");

        //Just for this test we change the default port from 4204 to avoid conflicts
        properties.setProperty("httpejbd.port", "" + PORT);

        properties.setProperty("httpejbd.print", "true");
        properties.setProperty("httpejbd.indent.xml", "true");
        properties.setProperty("logging.level.OpenEJB.server.http", "FINE");

        EJBContainer.createEJBContainer(properties);
    }

    @Test
    public void test() throws Exception {
        // TODO: Where does the main come from?
        // INFO: Webservice(wsdl=http://127.0.0.1:41631/main/Calculator, qname={http://superbiz.org/wsdl}CalculatorService) --> Ejb(id=Calculator)

        Service calculatorService = Service.create(
                new URL("http://localhost:" + PORT + "/main/Calculator?wsdl"),
                new QName("http://superbiz.org/wsdl", "CalculatorService"));

        assertThat(calculatorService).isNotNull();

        CalculatorWs calculator = calculatorService.getPort(CalculatorWs.class);
        assertThat(calculator.sum(4, 6)).isEqualTo(10);
        assertThat(calculator.multiply(3, 4)).isEqualTo(12);
    }
}
