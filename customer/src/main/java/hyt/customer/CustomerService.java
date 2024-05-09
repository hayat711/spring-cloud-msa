package hyt.customer;

import hyt.clients.fraud.FraudCheckResponse;
import hyt.clients.fraud.FraudClient;
import hyt.clients.notification.NotificationClient;
import hyt.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.hyt.amqp.RabbitMQMessageProducer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer producer;


    public void registerCustomer(CustomerRegistrationRecord request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        // todo: check if fraudster
//        FraudCheckResponse fraudCheckResponse =  restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }


        NotificationRequest notificationRequest =
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, Welcome to Hyt...", customer.getFirstName())
                );

        // publish to queue
        producer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );



    }
}
