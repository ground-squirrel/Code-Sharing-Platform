package platform.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="snippets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeSnippet {

    @Id
    @Column
    @JsonIgnore
    private String uuid;

    @Lob
    @Column
    private String code;

    @Column
    private LocalDateTime date;

    @Column
    private LocalDateTime expirationDate;

    @Column
    private Integer viewsAllowed;

    public CodeSnippet(String code, LocalDateTime date, LocalDateTime expirationDate, Integer viewsAllowed) {
        this.code = code;
        this.date = date;
        this.expirationDate = expirationDate;
        this.viewsAllowed = viewsAllowed;
    }

    public void decreaseViewsAllowed() {
        if (null != viewsAllowed) {
            this.viewsAllowed--;
        }
    }

    public boolean hasExpired() {
        if (null == expirationDate && null == viewsAllowed) {
            return false;
        } else return (null != viewsAllowed && viewsAllowed <= 0)
                || (null != expirationDate && expirationDate.isBefore(LocalDateTime.now()));
    }

}
