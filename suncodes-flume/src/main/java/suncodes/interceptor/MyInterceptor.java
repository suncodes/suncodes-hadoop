package suncodes.interceptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

public class MyInterceptor implements Interceptor {
    private List<Event> addHeaderEvents;

    private MyInterceptor() {
    }

    @Override
    public void initialize() {
        this.addHeaderEvents = new ArrayList<>();
    }

    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        String body = new String(event.getBody());
        if (body.contains("hello")) {
            headers.put("topic", "first");
        } else {
            headers.put("topic", "second");
        }

        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        this.addHeaderEvents.clear();
        Iterator var2 = events.iterator();

        while (var2.hasNext()) {
            Event event = (Event) var2.next();
            this.addHeaderEvents.add(this.intercept(event));
        }

        return this.addHeaderEvents;
    }

    @Override
    public void close() {
    }

    public static class Builder implements org.apache.flume.interceptor.Interceptor.Builder {
        public Builder() {
        }

        @Override
        public Interceptor build() {
            return new MyInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}
