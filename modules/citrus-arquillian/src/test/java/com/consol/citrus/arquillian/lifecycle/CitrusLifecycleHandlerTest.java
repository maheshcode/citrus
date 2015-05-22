/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.arquillian.lifecycle;

import com.consol.citrus.Citrus;
import com.consol.citrus.arquillian.configuration.CitrusConfiguration;
import com.consol.citrus.arquillian.helper.InjectionHelper;
import com.consol.citrus.config.CitrusBaseConfig;
import org.easymock.EasyMock;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.easymock.EasyMock.*;

public class CitrusLifecycleHandlerTest {

    private CitrusLifecycleHandler lifecycleHandler = new CitrusLifecycleHandler();

    private CitrusConfiguration configuration = CitrusConfiguration.from(new Properties());

    private Citrus citrusFramework = Citrus.newInstance(CitrusBaseConfig.class);
    private Instance<Citrus> citrusInstance = EasyMock.createMock(Instance.class);
    private Instance<CitrusConfiguration> configurationInstance = EasyMock.createMock(Instance.class);

    @Test
    public void testLifecycle() throws Exception {
        reset(citrusInstance, configurationInstance);

        expect(citrusInstance.get()).andReturn(citrusFramework).anyTimes();
        expect(configurationInstance.get()).andReturn(configuration).anyTimes();

        replay(citrusInstance, configurationInstance);

        InjectionHelper.inject(lifecycleHandler, "citrusInstance", citrusInstance);
        InjectionHelper.inject(lifecycleHandler, "configurationInstance", configurationInstance);

        lifecycleHandler.beforeSuite(new BeforeSuite());
        lifecycleHandler.afterSuite(new AfterSuite());

        verify(citrusInstance, configurationInstance);
    }
}