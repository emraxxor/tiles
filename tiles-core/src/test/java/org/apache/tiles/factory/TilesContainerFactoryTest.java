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

import junit.framework.TestCase;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.ChainedTilesApplicationContextFactory;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.mock.RepeaterTilesApplicationContextFactory;
import org.apache.tiles.mock.RepeaterTilesRequestContextFactory;

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;


/**
 * @version $Rev$ $Date$
 */
public class TilesContainerFactoryTest extends TestCase {

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory
            .getLog(TilesContainerFactoryTest.class);

    /**
     * The servlet context.
     */
    private ServletContext context;

    /** {@inheritDoc} */
    @Override
    public void setUp() {
        context = EasyMock.createMock(ServletContext.class);
    }

    /**
     * Tests getting the factory.
     */
    @SuppressWarnings("deprecation")
    public void testGetFactory() {
        Vector<String> v = new Vector<String>();
        Vector<String> emptyVector = new Vector<String>();
        v.add(AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM);
        v.add(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES);
        v.add(ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES);

        EasyMock.expect(context.getInitParameterNames()).andReturn(
                emptyVector.elements());
        EasyMock.expect(context.getInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM))
                .andReturn(null);
        EasyMock.expect(context.getInitParameter(
                TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM))
                .andReturn(null);
        EasyMock.replay(context);
        AbstractTilesContainerFactory factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        assertNotNull(factory);
        assertEquals(TilesContainerFactory.class, factory.getClass());

        EasyMock.reset(context);
        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements());
        EasyMock.expect(context.getInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM))
                .andReturn(TestFactory.class.getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.replay(context);
        factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        assertNotNull(factory);
        assertEquals(TestFactory.class, factory.getClass());

        Map<String, String> defaults = new HashMap<String, String>();
        EasyMock.reset(context);
        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements());
        EasyMock.expect(context.getInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM))
                .andReturn(TestFactory.class.getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.replay(context);
        factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        ((TilesContainerFactory) factory).setDefaultConfiguration(defaults);
        assertNotNull(factory);
        assertEquals(TestFactory.class, factory.getClass());

        EasyMock.reset(context);
        EasyMock.expect(context.getInitParameterNames()).andReturn(v.elements());
        EasyMock.expect(context.getInitParameter(AbstractTilesContainerFactory
                .CONTAINER_FACTORY_INIT_PARAM)).andReturn("org.missing.Class");
        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.replay(context);
        try {
            AbstractTilesContainerFactory.getTilesContainerFactory(context);
            fail("Invalid classname.  Exception should have been thrown.");
        } catch (TilesException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("The classname is invalid, it is ok", e);
            }
        }
    }


    /**
     * Tests the creation of a container.
     *
     * @throws MalformedURLException If something goes wrong when obtaining URL
     * resources.
     */
    public void testCreateContainer() throws MalformedURLException {
        URL url = getClass().getResource("test-defs.xml");
        Vector<String> enumeration = new Vector<String>();
        enumeration.add(ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES);
        enumeration.add(ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES);
        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .APPLICATION_CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory
                .REQUEST_CONTEXT_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(TilesContainerFactory.DEFINITIONS_FACTORY_INIT_PARAM)).andReturn(null);
        EasyMock.expect(context.getInitParameter(EasyMock.isA(String.class))).andReturn(null).anyTimes();
        EasyMock.expect(context.getInitParameterNames()).andReturn(enumeration.elements()).anyTimes();
        EasyMock.expect(context.getResource("/WEB-INF/tiles.xml")).andReturn(url);
        EasyMock.replay(context);

        AbstractTilesContainerFactory factory = AbstractTilesContainerFactory
                .getTilesContainerFactory(context);
        TilesContainer container = factory.createContainer(context);

        assertNotNull(container);
        //now make sure it's initialized
        try {
            container.init(new HashMap<String, String>());
            fail("Container should have already been initialized");
        } catch (IllegalStateException te) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Intercepted an exception, it is OK", te);
            }
        }

    }


    /**
     * Tests getting init parameter map.
     */
    public void testGetInitParameterMap() {
        Vector<String> keys = new Vector<String>();
        keys.add("one");
        keys.add("two");

        EasyMock.expect(context.getInitParameter(
                ChainedTilesApplicationContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesApplicationContextFactory.class
                        .getName());
        EasyMock.expect(context.getInitParameter(
                ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES))
                .andReturn(RepeaterTilesRequestContextFactory.class.getName());
        EasyMock.expect(context.getInitParameterNames()).andReturn(keys.elements());
        EasyMock.expect(context.getInitParameterNames()).andReturn(keys.elements());
        EasyMock.expect(context.getInitParameter("one")).andReturn("oneValue").anyTimes();
        EasyMock.expect(context.getInitParameter("two")).andReturn("twoValue").anyTimes();
        EasyMock.replay(context);

        Map<String, String> map = TilesContainerFactory.getInitParameterMap(context);

        assertEquals(2, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.containsKey("two"));
        assertEquals("oneValue", map.get("one"));
        assertEquals("twoValue", map.get("two"));
    }

    /**
     * A test factory extending directly from TilesContainerFactory.
     */
    public static class TestFactory extends TilesContainerFactory {

    }
}
