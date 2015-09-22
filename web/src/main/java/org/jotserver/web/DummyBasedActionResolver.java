package org.jotserver.web;

import java.net.MalformedURLException;
import java.net.URL;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.NameBasedActionResolver;

public class DummyBasedActionResolver extends NameBasedActionResolver {

    @Override
    protected Resolution findView(String urlBinding) {
        Resolution res = new ForwardResolution("/WEB-INF/view/NotFound.jsp");
        if(urlBinding.endsWith(DEFAULT_BINDING_SUFFIX)) {
            String resource = "/WEB-INF/view" + urlBinding.substring(0, urlBinding.length()-DEFAULT_BINDING_SUFFIX.length()) + ".jsp";
            try {
                URL url = getConfiguration().getServletContext().getResource(resource);
                if(url != null) {
                    res = new ForwardResolution(resource);
                }
            } catch(MalformedURLException e) { }
        }
        return res;
    }

}
