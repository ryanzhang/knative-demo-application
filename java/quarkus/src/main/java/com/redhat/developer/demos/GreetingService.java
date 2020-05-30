package com.redhat.developer.demos;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.text.SimpleDateFormat;
import java.util.Date;

@ApplicationScoped
public class GreetingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GreetingService.class);

  private final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");

  private static final String RESPONSE_STRING_FORMAT = "%s %s  \n\n\nprint by => '%s' : %d\n";
  // private static final String RESPONSE_STRING_FORMAT = "<html><body><h2><pre>%s %s </pre></h2><p><i>print by => '%s' : %d\n";

  private static final String HOSTNAME =
    parseContainerIdFromHostname(System.getenv().getOrDefault("HOSTNAME", "unknown"));

  static String parseContainerIdFromHostname(String hostname) {
    return hostname.replaceAll("greeter-v\\d+-", "").trim();
  }

  /**
   * Counter to help us see the lifecycle
   */
  private int count = 0;

  public String greet(String prefix) {
    count++;
    String val = "\n" +
   " _                                                                                           \n" +
   "(_)                                                                                          \n" +
   "(_)             _  _  _  _    _  _  _  _       _  _  _    _               _    _  _  _       \n" +
   "(_)            (_)(_)(_)(_)_ (_)(_)(_)(_)_  _ (_)(_)(_) _(_)_           _(_)_ (_)(_)(_) _    \n" +
   "(_)           (_) _  _  _ (_)(_)        (_)(_)         (_) (_)_       _(_) (_)         (_)   \n" +
   "(_)           (_)(_)(_)(_)(_)(_)        (_)(_)         (_)   (_)_   _(_)   (_)         (_)   \n" +
   "(_) _  _  _  _(_)_  _  _  _  (_)        (_)(_) _  _  _ (_)     (_)_(_)     (_) _  _  _ (_)   \n" +
   "(_)(_)(_)(_)(_) (_)(_)(_)(_) (_)        (_)   (_)(_)(_)          (_)          (_)(_)(_)      \n";
    return String.format(RESPONSE_STRING_FORMAT, val, "", HOSTNAME, count);
  }

  String eventGreet(String cloudEventJson) {
    count++;
    String greeterHost = String.format(RESPONSE_STRING_FORMAT, ""," Event ", HOSTNAME, count);
    JsonObject response = new JsonObject(cloudEventJson)
                            .put("host", greeterHost.replace("\n", "").trim())
                            .put("time", SDF.format(new Date()));
    LOGGER.info("Event Message Received \n {}", response.encodePrettily());
    return response.encode();
  }

}
