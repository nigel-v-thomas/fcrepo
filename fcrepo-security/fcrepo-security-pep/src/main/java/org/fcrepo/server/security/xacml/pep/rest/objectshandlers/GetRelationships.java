package org.fcrepo.server.security.xacml.pep.rest.objectshandlers;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fcrepo.common.Constants;
import org.fcrepo.server.security.xacml.pep.PEPException;
import org.fcrepo.server.security.xacml.pep.ResourceAttributes;
import org.fcrepo.server.security.xacml.pep.rest.filters.AbstractFilter;
import org.fcrepo.server.security.xacml.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.ctx.RequestCtx;


/**
 * Handles REST API method getRelationships
 *
 * @author Stephen Bayliss
 * @version $Id$
 */
public class GetRelationships
        extends AbstractFilter {

    private static final Logger logger =
        LoggerFactory.getLogger(GetRelationships.class);

    public GetRelationships()
    throws PEPException {
        super();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.fcrepo.server.security.xacml.pep.rest.filters.RESTFilter#handleRequest(javax.servlet
     * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public RequestCtx handleRequest(HttpServletRequest request,
                                    HttpServletResponse response)
            throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("{}/handleRequest!", this.getClass().getName());
        }

        RequestCtx req = null;
        Map<URI, AttributeValue> actions = new HashMap<URI, AttributeValue>();
        Map<URI, AttributeValue> resAttr;
        try {
            String[] parts = getPathParts(request);
            resAttr = ResourceAttributes.getResources(parts);

            actions.put(Constants.ACTION.ID.getURI(),
                        Constants.ACTION.GET_RELATIONSHIPS
                                .getStringAttribute());
            actions.put(Constants.ACTION.API.getURI(),
                        Constants.ACTION.APIM.getStringAttribute());

            req =
                    getContextHandler().buildRequest(getSubjects(request),
                                                     actions,
                                                     resAttr,
                                                     getEnvironment(request));

            String pid = resAttr.get(Constants.OBJECT.PID.getURI()).toString();
            LogUtil.statLog(request.getRemoteUser(),
                            Constants.ACTION.GET_RELATIONSHIPS.uri,
                            pid,
                            null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }

        return req;
    }

}
