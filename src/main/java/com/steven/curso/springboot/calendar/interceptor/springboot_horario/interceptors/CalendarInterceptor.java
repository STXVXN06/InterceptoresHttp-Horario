package com.steven.curso.springboot.calendar.interceptor.springboot_horario.interceptors;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("calendarInterceptor")
public class CalendarInterceptor implements HandlerInterceptor {

    @Value("${config.calendar.open}")
    private Integer open;
    @Value("${config.calendar.close}")
    private Integer close;

    private Logger logger = LoggerFactory.getLogger(CalendarInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        
        if (hour >= open && hour < close) {
            StringBuilder message = new StringBuilder("Bienvenidos al horario de atencion a clientes");
            message.append(", atendemos desde las ");
            message.append(open);
            message.append("hrs. ");
            message.append("hasta las ");
            message.append(close);
            message.append("hrs. ");
            message.append("Gracias por su visita!");
            request.setAttribute("message", message.toString());
            logger.info("entrando preHandle hora correcta");
            return true;
        }
        logger.info("entrando preHandle hora incorrecta");
        Map<String,String> json = new HashMap<>();
        json.put("error", "No tienes acceso a esta pagina!");
        json.put("date", new Date().toString());
        json.put("message", "Horario de atencion desde las " + open + "hrs. hasta las " + close + "hrs.");
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(json);

        response.setContentType("application/json");
        response.setStatus(401);

        response.getWriter().write(jsonString);

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

}
