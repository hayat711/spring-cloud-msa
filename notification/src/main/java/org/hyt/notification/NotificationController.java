package org.hyt.notification;

import hyt.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping()
    public void sendNotification(@RequestBody NotificationRequest request) {
        log.info("New notification... {}", request);
        notificationService.send(request);
    }
}
