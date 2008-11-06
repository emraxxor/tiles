/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tiles.factory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer;
import org.apache.tiles.mock.RepeaterTilesApplicationContextFactory;
import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;
import org.easymock.EasyMock;


/**
 * @version $Rev$ $Date$
 */
public class KeyedDefinitionsFactoryTilesContainerFactoryTest extends TestCase {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(KeyedDefinitionsFactoryTilesContainerFactoryTest.class);

    /**
     * The servlet context.
     */
    private ServletContext context;

    /**
     * Default configuration parameters.
     */
    private Map<String, String> defaults;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        context = EasyMock.createMock(ServletContext.class);
        EasyMock.expect(context.getInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM)).andReturn(
                KeyedDefinitionsFactoryTilesContainerFactory.class.getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        defaults = new HashMap<String, String>();
    }

    /**
     * Tests getting a container factory.
     */
    public void testGetFactory() {
        Vector<String> v = new Vector<String>();
        v.add(AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM);
        v.add(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES);
        v.add(ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES);

        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements());
        EasyMock.replay(context);
        TilesContainerFactory factory = (TilesContainerFactory) AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        factory.setDefaultConfiguration(defaults);
        assertNotNull(factory);
        assertEquals(KeyedDefinitionsFactoryTilesContainerFactory.class,
                factory.getClass());
    }

    /**
     * Tests creating a container.
     *
     * @throws MalformedURLException If the resources have an invalid form (that
     * should not happen).
     */
    public void testCreateContainer() throws MalformedURLException {
        Vector<String> enumeration = new Vector<String>();
        enumeration.add(AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM);
        enumeration.add(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES);
        enumeration.add(ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .APPLICATION_CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .REQUEST_CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainerFactory.CONTAINER_KEYS_INIT_PARAM))
                .andReturn("one,two").anyTimes();
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "one"))
                .andReturn("/WEB-INF/tiles-one.xml").anyTimes();
        EasyMock.expect(context.getInitParameter(
                KeyedDefinitionsFactoryTilesContainer.DEFINITIONS_CONFIG_PREFIX
                + "@two")).andReturn("/WEB-INF/tiles-two.xml").anyTimes();
        EasyMock.expect(context.getInitParameter(EasyMock.isA(String.class))).andReturn(null).anyTimes();
        EasyMock.expect(context.getInitParameterNames()).andReturn(enumeration.elements()).anyTimes();
        URL url = getClass().getResource("test-defs.xml");
        EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        url = getClass().getResource("test-defs-key-one.xml");
        EasyMock.expect(context.getResource("/WEB-INF/tiles-one.xml")).andReturn(url);
        url = getClass().getResource("test-defs-key-two.xml");
        EasyMock.expect(context.getResource("/WEB-INF/tiles-two.xml")).andReturn(url);
        EasyMock.replay(context);

        TilesContainerFactory factory = (TilesContainerFactory) AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        factory.setDefaultConfiguration(defaults);
        TilesContainer container = factory.createContainer(context);

        assertNotNull(container);
        assertTrue("The container is not an instance of KeyedDefinitionsFactoryTilesContainer",
                container instanceof KeyedDefinitionsFactoryTilesContainer);
        KeyedDefinitionsFactoryTilesContainer keyedContainer =
            (KeyedDefinitionsFactoryTilesContainer) container;
        assertNotNull(keyedContainer.getDefinitionsFactory());
        assertNotNull(keyedContainer.getDefinitionsFactory("one"));
        assertNotNull(keyedContainer.getDefinitionsFactory("two"));
        //now make sure it's initialized
        try {
            container.init(new HashMap<String, String>());
            fail("Container should have already been initialized");
        } catch (IllegalStateException te) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("The container has been initialized, the exception is ok", te);
            }
        }

    }
}
