package platform.business;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Data
public class WrappedSnippet {

    private String code;

    private String date;

    private long time;

    private int views;

    public WrappedSnippet() {

    }

    public WrappedSnippet(CodeSnippet code) {
        this.code = code.getCode();
        this.date = code.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.time = (null != code.getExpirationDate()) ?
                ChronoUnit.SECONDS.between(LocalDateTime.now(), code.getExpirationDate())
                : 0L;
        this.views = (null != code.getViewsAllowed()) ? code.getViewsAllowed() : 0;
    }

    public WrappedSnippet(String code, long time, int views) {
        this.code = code;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.time = time;
        this.views = views;
    }

    public CodeSnippet unwrap() {
        LocalDateTime date = (null != this.date) ? LocalDateTime.parse(this.date) : LocalDateTime.now();
        LocalDateTime expirationDate = (time > 0) ? date.plusSeconds(time) : null;
        Integer viewsAllowed = (views > 0) ? views : null;
        return new CodeSnippet(this.code, date, expirationDate, viewsAllowed);
    }
}
