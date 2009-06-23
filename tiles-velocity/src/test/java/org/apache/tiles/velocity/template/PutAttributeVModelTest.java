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

package org.apache.tiles.velocity.template;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.PutAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PutAttributeVModel}.
 */
public class PutAttributeVModelTest {

    /**
     * The attribute key that will be used to store the parameter map, to use across Velocity tool calls.
     */
    private static final String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK";

    /**
     * The model to test.
     */
    private PutAttributeVModel model;

    /**
     * The template model.
     */
    private PutAttributeModel tModel;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(PutAttributeModel.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.PutAttributeVModel
     * #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testExecute() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> params = createParams();
        Stack<Object> composeStack = new Stack<Object>();

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.execute(container, composeStack, "myName", "myValue",
                "myExpression", null, "myRole", "myType",
                false, velocityContext, request, response);

        replay(tModel, container, servletContext, request, response, velocityContext);
        initializeModel();
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.execute(request, response, velocityContext, params));
        verify(tModel, container, servletContext, request, response, velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.PutAttributeVModel
     * #start(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Map<String, Object> params = createParams();
        Stack<Object> composeStack = new Stack<Object>();
        Stack<Map<String, Object>> parameterMapStack = new Stack<Map<String, Object>>();

        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(parameterMapStack);
        tModel.start(composeStack);

        replay(tModel, servletContext, request, response, velocityContext);
        initializeModel();
        model.start(request, response, velocityContext, params);
        assertEquals(1, parameterMapStack.size());
        assertEquals(params, parameterMapStack.peek());
        verify(tModel, servletContext, request, response, velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.PutAttributeVModel
     * #end(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.apache.velocity.context.Context)}.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        Map<String, Object> params = createParams();
        Stack<Object> composeStack = new Stack<Object>();
        Stack<Map<String, Object>> parameterMapStack = new Stack<Map<String, Object>>();
        parameterMapStack.push(params);

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(parameterMapStack);
        tModel.end(container, composeStack, "myName", "myValue",
                "myExpression", null, "myRole", "myType",
                false, velocityContext, request, response);

        replay(tModel, container, servletContext, request, response, velocityContext);
        initializeModel();
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.end(request, response, velocityContext));
        assertTrue(parameterMapStack.isEmpty());
        verify(tModel, container, servletContext, request, response, velocityContext);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "myName");
        params.put("value", "myValue");
        params.put("expression", "myExpression");
        params.put("role", "myRole");
        params.put("type", "myType");
        params.put("cascade", false);
        return params;
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new PutAttributeVModel(tModel, servletContext);
    }
}
