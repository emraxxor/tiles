package org.apache.tiles.velocity.context;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class VelocityTilesRequestContextTest {

    /**
     * The request context to test.
     */
    private VelocityTilesRequestContext context;
    
    /**
     * The Velocity context.
     */
    private Context velocityContext;
    
    /**
     * A string writer.
     */
    private StringWriter writer;
    
    /**
     * @throws java.lang.Exception If something goes wrong.
     */
    @Before
    public void setUp() throws Exception {
        velocityContext = createMock(Context.class);
        writer = new StringWriter();
    }

    /**
     * Tests {@link VelocityTilesRequestContext#dispatch(String)}.
     * 
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException, ServletException {
        String path = "this way";
        TilesRequestContext enclosedRequest = createMock(TilesRequestContext.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        RequestDispatcher dispatcher = createMock(RequestDispatcher.class);
        
        servletRequest.setAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME, true);
        expect(servletRequest.getRequestDispatcher("this way")).andReturn(dispatcher);
        dispatcher.include(eq(servletRequest), isA(ExternalWriterHttpServletResponse.class));
        replay(servletRequest, response, dispatcher);
        Object[] requestItems = new Object[] {servletRequest, response};
        
        expect(enclosedRequest.getRequestObjects()).andReturn(requestItems);
        
        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        context.dispatch(path);
        verify(velocityContext, enclosedRequest, servletRequest, response, dispatcher);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#include(String)}.
     * 
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testInclude() throws IOException, ServletException {
        String path = "this way";
        TilesRequestContext enclosedRequest = createMock(TilesRequestContext.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        RequestDispatcher dispatcher = createMock(RequestDispatcher.class);
        
        servletRequest.setAttribute(ServletUtil.FORCE_INCLUDE_ATTRIBUTE_NAME, true);
        expect(servletRequest.getRequestDispatcher("this way")).andReturn(dispatcher);
        dispatcher.include(eq(servletRequest), isA(ExternalWriterHttpServletResponse.class));
        replay(servletRequest, response, dispatcher);
        Object[] requestItems = new Object[] {servletRequest, response};
        
        expect(enclosedRequest.getRequestObjects()).andReturn(requestItems);
        
        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        context.include(path);
        verify(velocityContext, enclosedRequest, servletRequest, response, dispatcher);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#getPrintWriter()}.
     * 
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetPrintWriter() throws IOException {
        TilesRequestContext enclosedRequest = createMock(TilesRequestContext.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        assertNotNull(context.getPrintWriter());
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#getWriter()}.
     * 
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetWriter() throws IOException {
        TilesRequestContext enclosedRequest = createMock(TilesRequestContext.class);

        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        assertEquals(writer, context.getWriter());
        verify(velocityContext, enclosedRequest);
    }

    /**
     * Tests {@link VelocityTilesRequestContext#getRequestObjects()}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testGetRequestObjects() throws ServletException, IOException {
        TilesRequestContext enclosedRequest = createMock(TilesRequestContext.class);
        HttpServletRequest servletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        
        replay(servletRequest, response);
        Object[] requestItems = new Object[] {servletRequest, response};
        
        expect(enclosedRequest.getRequestObjects()).andReturn(requestItems);
        
        replay(velocityContext, enclosedRequest);
        context = new VelocityTilesRequestContext(enclosedRequest, velocityContext, writer);
        assertArrayEquals(new Object[] {velocityContext, servletRequest, response, writer}, context.getRequestObjects());
        verify(velocityContext, enclosedRequest, servletRequest, response);
    }
}