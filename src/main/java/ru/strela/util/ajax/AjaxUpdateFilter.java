package ru.strela.util.ajax;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AjaxUpdateFilter implements Filter {
    
	private static class ByteResponseWrapper extends HttpServletResponseWrapper {
        private PrintWriter writer;
        private ByteOutputStream output;
        
        public byte[] getBytes() {
            writer.flush();
            return output.getBytes();
        }
          
        public ByteResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new ByteOutputStream();
            writer = new PrintWriter(output);
        }

        @Override
        public PrintWriter getWriter() {
            return writer;
        }
        
        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return output;
        }
    }
	    
	private static class ByteOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos = new ByteArrayOutputStream();

        @Override
        public void write(int b) throws IOException {
            bos.write(b);            
        }
        
        public byte[] getBytes() {
            return bos.toByteArray();
        }

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
		}
    }
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        boolean isAjax = "true".equals(httpRequest.getHeader("X-Ajax-Update"));
        if(isAjax) {
        	ByteResponseWrapper brw = new ByteResponseWrapper((HttpServletResponse) response);
            chain.doFilter(request, brw);
        	
	        @SuppressWarnings("unchecked")
			List<String> updates = (List<String>)request.getAttribute("ajaxUpdate");
	        if(updates != null && !updates.isEmpty()) {
	        	Source source = new Source(new ByteArrayInputStream(brw.getBytes()));
        		JsonNodeFactory factory = JsonNodeFactory.instance;
        		ArrayNode fragmentsNode = factory.arrayNode();
        		for(String update : updates) {
        			Element element = source.getElementById(update);
        			if(element != null) {
        				ObjectNode fragmentNode = fragmentsNode.addObject();
        				fragmentNode.put("type", "c");
        				fragmentNode.put("id", update);
        				fragmentNode.put("html", element.getContent().toString());
        			}
     	        }
        		response.setContentType("application/json");
        		response.getWriter().print(fragmentsNode.toString());
	        }
        } else {
        	chain.doFilter(request, response);
        }
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
	
}