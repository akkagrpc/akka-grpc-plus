#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.projection;

import akka.Done;
import akka.kafka.javadsl.SendProducer;
import akka.persistence.query.typed.EventEnvelope;
import akka.projection.r2dbc.javadsl.R2dbcHandler;
import akka.projection.r2dbc.javadsl.R2dbcSession;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import lombok.SneakyThrows;
import ${package}.server.event.${first_word_of_artifactId}Registered;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ${package}.server.event.Event;

import java.util.concurrent.CompletionStage;

import static akka.Done.done;

public class EventsProjectionHandler extends R2dbcHandler<EventEnvelope<Event>> {
    private final Logger logger = LoggerFactory.getLogger(EventsProjectionHandler.class);
    private final String topic;
    private final SendProducer<String, byte[]> sendProducer;

    public EventsProjectionHandler(String topic, SendProducer<String, byte[]> sendProducer) {
        this.topic = topic;
        this.sendProducer = sendProducer;
    }

    @SneakyThrows
    @Override
    public CompletionStage<Done> process(R2dbcSession session, EventEnvelope<Event> envelope) {
        Event event = envelope.event();
        // using the cartId as the key and `DefaultPartitioner` will select partition based on the key
        // so that events for same cart always ends up in same partition
        String key = "";
        if (event instanceof ${first_word_of_artifactId}Registered) {
            ${first_word_of_artifactId}Registered templateRegistered = (${first_word_of_artifactId}Registered) event;
            key = templateRegistered.${package}Id;
        }
        ProducerRecord<String, byte[]> producerRecord =
                new ProducerRecord<>(topic, key, serialize(event));
        return sendProducer
                .send(producerRecord)
                .thenApply(
                        recordMetadata -> {
                            logger.info(
                                    "Published event [{}] to topic/partition {}/{}",
                                    event,
                                    topic,
                                    recordMetadata.partition());
                            return done();
                        });
    }

    private static byte[] serialize(Event event) {
        final ByteString protoMessage;
        final String fullName;
        if (event instanceof ${first_word_of_artifactId}Registered) {
            ${first_word_of_artifactId}Registered templateRegistered = (${first_word_of_artifactId}Registered) event;
            protoMessage =
                    com.akkagrpc.grpc.${first_word_of_artifactId}Added.newBuilder()
                            .build()
                            .toByteString();
            fullName = com.akkagrpc.grpc.${first_word_of_artifactId}Added.getDescriptor().getFullName();

        } else {
            throw new IllegalArgumentException("Unknown event type: " + event.getClass());
        }
        // pack in Any so that type information is included for deserialization
        return Any.newBuilder()
                .setValue(protoMessage)
                .setTypeUrl("{artifactId}/" + fullName)
                .build()
                .toByteArray();
    }
}

