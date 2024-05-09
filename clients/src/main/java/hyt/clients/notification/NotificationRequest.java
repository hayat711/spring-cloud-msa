package hyt.clients.notification;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationRequest {
    private String message;
    private String sender;
    private String toCustomerEmail;
    private Integer toCustomerId;
    private LocalDateTime sentAt;

    public NotificationRequest(Integer customerId, String toCustomerEmail, String message) {
        this.toCustomerId = customerId;
        this.toCustomerEmail = toCustomerEmail;
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }

}
