package org.apache.tiles.velocity;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertAttributeModel;
import org.apache.velocity.context.Context;

public class InsertAttributeVModel implements Executable, BodyExecutable {

    private InsertAttributeModel model;
    
    public InsertAttributeVModel(InsertAttributeModel model,
            ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    private ServletContext servletContext;
    
    public void end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(velocityContext).pop();
        try {
            model.end(ServletUtil.getComposeStack(request), ServletUtil.getCurrentContainer(request, servletContext),
                    VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"), false),
                    velocityContext, request, response);
        } catch (IOException e) {
            throw new TilesVelocityException("Cannot end insertAttribute", e);
        }
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        VelocityUtil.getParameterStack(velocityContext).push(params);
        model.start(ServletUtil.getComposeStack(request), ServletUtil.getCurrentContainer(request, servletContext),
                VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"), false),
                (String) params.get("preparer"), (String) params.get("role"),
                params.get("defaultValue"), (String) params.get("defaultValueRole"),
                (String) params.get("defaultValueType"), (String) params.get("name"),
                (Attribute) params.get("value"), velocityContext, request, response);
        
    }

    public void execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        try {
            model.execute(ServletUtil.getCurrentContainer(request, servletContext),
                    VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"), false),
                    (String) params.get("preparer"), (String) params.get("role"),
                    params.get("defaultValue"), (String) params.get("defaultValueRole"),
                    (String) params.get("defaultValueType"), (String) params.get("name"),
                    (Attribute) params.get("value"), velocityContext, request, response);
        } catch (IOException e) {
            throw new TilesVelocityException("Cannot execute insertAttribute", e);
        }
    }

}
