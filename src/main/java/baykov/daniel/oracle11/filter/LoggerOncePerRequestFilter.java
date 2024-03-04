package baykov.daniel.oracle11.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class LoggerOncePerRequestFilter extends OncePerRequestFilter {

    private static final List<String> HEADERS_TO_SKIP = Arrays.asList("authorization", "token", "security", "oauth", "auth");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Instant startTime = Instant.now();

        CachedRequestHttpServletRequest cachedRequest = new CachedRequestHttpServletRequest(request);

        logRequest(cachedRequest);

        CachedResponseHttpServletResponse cachedResponse = new CachedResponseHttpServletResponse(response);

        filterChain.doFilter(cachedRequest, cachedResponse);

        logResponse(cachedResponse);

        Instant endTime = Instant.now();

        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Time taken: " + duration.toMillis() + " milliseconds");
    }

    private void logRequest(CachedRequestHttpServletRequest request) throws IOException {
        String log = String.format("Request - URL: %s | Request: %s | HTTP Method: %s | Headers: %s | QueryStringParams: %s | RequestBody: %s",
                request.getRequestURL(), request.getRemoteAddr(), request.getMethod(),
                getRequestHeaders(request), request.getQueryString(), getBody(request));
        System.out.println(log);
    }

    private void logResponse(CachedResponseHttpServletResponse response) throws IOException {
        String log = String.format("Response - Status: %s | Headers: %s | ResponseBody: %s",
                response.getStatus(), getResponseHeaders(response), response.getContentAsString());
        System.out.println(log);
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerEnumeration = request.getHeaderNames();
        while (headerEnumeration.hasMoreElements()) {
            String header = headerEnumeration.nextElement();

            if (!shouldSkipHeader(header)) {
                headersMap.put(header, request.getHeader(header));
            }
        }

        return headersMap;
    }

    private boolean shouldSkipHeader(String header) {
        return HEADERS_TO_SKIP.stream().anyMatch(h -> header.toLowerCase().contains(h.toLowerCase()));
    }

    private String getBody(CachedRequestHttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        return body.toString();
    }

    private Map<String, String> getResponseHeaders(CachedResponseHttpServletResponse response) {
        Map<String, String> headersMap = new HashMap<>();
        for (String header : response.getHeaderNames()) {
            headersMap.put(header, response.getHeader(header));
        }
        return headersMap;
    }

    private static class CachedRequestHttpServletRequest extends HttpServletRequestWrapper {

        private byte[] cachedBody;

        public CachedRequestHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
        }

        @Override
        public ServletInputStream getInputStream() {
            return new CachedRequestServletInputStream(this.cachedBody);
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.cachedBody)));
        }
    }

    private static class CachedRequestServletInputStream extends ServletInputStream {

        private InputStream cachedBodyInputStream;

        public CachedRequestServletInputStream(byte[] cachedBody) {
            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            try {
                return cachedBodyInputStream.available() == 0;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return cachedBodyInputStream.read();
        }
    }

    private static class CachedResponseHttpServletResponse extends HttpServletResponseWrapper {
        private ByteArrayOutputStream cachedResponseBody = new ByteArrayOutputStream();

        public CachedResponseHttpServletResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void write(int b) throws IOException {
                    cachedResponseBody.write(b);
                    getResponse().getOutputStream().write(b);
                }
            };
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new OutputStreamWriter(cachedResponseBody, getResponse().getCharacterEncoding()), true);
        }

        public String getContentAsString() {
            return cachedResponseBody.toString();
        }
    }
}
