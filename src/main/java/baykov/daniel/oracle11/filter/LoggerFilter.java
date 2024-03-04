//package baykov.daniel.oracle11.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletRequestWrapper;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StreamUtils;
//
//import java.io.*;
//import java.util.*;
//
//@Component
//@Order(value = Ordered.HIGHEST_PRECEDENCE)
//public class LoggerFilter implements Filter {
//
//    private static final List<String> HEADERS_TO_SKIP = Arrays.asList("authorization", "token", "security", "oauth", "auth");
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        CachedRequestHttpServletRequest cachedRequestHttpServletRequest =
//                new CachedRequestHttpServletRequest((HttpServletRequest) servletRequest);
//
//        String log = String.format("URL: %s | Request: %s | HTTP Method: %s | Headers: %s | QueryStringParams: %s | RequestBody: %s",
//                cachedRequestHttpServletRequest.getRequestURL(), cachedRequestHttpServletRequest.getRemoteAddr(),
//                cachedRequestHttpServletRequest.getMethod(), getRequestHeaders(cachedRequestHttpServletRequest),
//                cachedRequestHttpServletRequest.getQueryString(), getBody(cachedRequestHttpServletRequest));
//
//        System.out.println(log);
//
//        filterChain.doFilter(cachedRequestHttpServletRequest, servletResponse);
//    }
//
//    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
//        Map<String, String> headersMap = new HashMap<>();
//        Enumeration<String> headerEnumeration = request.getHeaderNames();
//        while (headerEnumeration.hasMoreElements()) {
//            String header = headerEnumeration.nextElement();
//
//            if (HEADERS_TO_SKIP.stream().noneMatch(h -> h.toLowerCase().contains(header.toLowerCase())
//                    || header.toLowerCase().contains(h.toLowerCase()))) {
//                headersMap.put(header, request.getHeader(header));
//            }
//        }
//
//        return headersMap;
//    }
//
//    private String getBody(CachedRequestHttpServletRequest request) throws IOException {
//        StringBuilder body = new StringBuilder();
//        String line;
//        BufferedReader reader = request.getReader();
//        while ((line = reader.readLine()) != null) {
//            body.append(line);
//        }
//
//        return body.toString();
//    }
//
//    private static class CachedRequestHttpServletRequest extends HttpServletRequestWrapper {
//
//        private byte[] cachedBody;
//
//        public CachedRequestHttpServletRequest(HttpServletRequest request) throws IOException {
//            super(request);
//            this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
//        }
//
//        @Override
//        public ServletInputStream getInputStream() {
//            return new CachedRequestServletInputStream(this.cachedBody);
//        }
//
//        @Override
//        public BufferedReader getReader() {
//            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.cachedBody)));
//        }
//    }
//
//    private static class CachedRequestServletInputStream extends ServletInputStream {
//
//        private InputStream cachedBodyInputStream;
//
//        public CachedRequestServletInputStream(byte[] cachedBody) {
//            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
//        }
//
//        @Override
//        public boolean isFinished() {
//            try {
//                return cachedBodyInputStream.available() == 0;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return false;
//        }
//
//        @Override
//        public boolean isReady() {
//            return true;
//        }
//
//        @Override
//        public void setReadListener(ReadListener listener) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public int read() throws IOException {
//            return cachedBodyInputStream.read();
//        }
//    }
//}
