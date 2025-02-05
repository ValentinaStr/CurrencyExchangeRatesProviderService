package com.currencyexchange;

/*
@Component
public class RequestLoggingFilter implements Filter {

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        long startTime = System.currentTimeMillis();

        // Пропускаем запрос дальше по цепочке
        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        // Сохраняем лог в базе данных
        RequestLog log = new RequestLog();
        log.setMethod(httpServletRequest.getMethod());
        log.setUrl(httpServletRequest.getRequestURL().toString());
        log.setClientIp(request.getRemoteAddr());
        log.setTimestamp(LocalDateTime.now());
        log.setDuration(duration);

        requestLogRepository.save(log);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }*/
