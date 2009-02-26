package org.apache.tiles.velocity;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertDefinitionModel;
import org.apache.velocity.context.Context;

public class InsertDefinitionVModel implements Executable, BodyExecutable {

    private InsertDefinitionModel model;

    private ServletContext servletContext;

    public InsertDefinitionVModel(InsertDefinitionModel model,
            ServletContext servletContext) {
        this.model = model;
        this.servletContext = servletContext;
    }

    public void execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params) {
        model.execute(ServletUtil.getCurrentContainer(request, servletContext),
                (String) params.get("name"), (String) params.get("template"),
                (String) params.get("role"), (String) params.get("preparer"),
                velocityContext, request, response);
    }

    public void end(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext) {
        Map<String, Object> params = VelocityUtil.getParameterStack(velocityContext).pop();
        model.execute(ServletUtil.getCurrentContainer(request, servletContext),
                (String) params.get("name"), (String) params.get("template"),
                (String) params.get("role"), (String) params.get("preparer"),
                velocityContext, request, response);
    }

    public void start(HttpServletRequest request, HttpServletResponse response,
            Context velocityContext, Map<String, Object> params) {
        // TODO Auto-generated method stub

    }

}
