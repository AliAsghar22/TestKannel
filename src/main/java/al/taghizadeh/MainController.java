package al.taghizadeh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by ali on 7/17/17.
 */
@Controller
@RequestMapping("/kannel")
public class MainController {
    private static  int totalReceived;
    Logger logger = LoggerFactory.getLogger(MainController.class);
    @GetMapping("/receive")
    public ResponseEntity receiveNewMessage(@RequestParam(value = "sender", required = false) String sender
            , @RequestParam(value = "receiver", required = false) String receiver
            , @RequestParam(value = "message", required = false) String message
            , @RequestParam(value = "timestamp", required = false) String timestamp
            , @RequestParam(value = "smscId", required = false) String smscId){
        logger.info("=====inbound message=====");
        logger.info("sender: " + sender);
        logger.info("receiver: " + receiver);
        logger.info("message: " + message);
        logger.info("timestamp: " + timestamp);
        logger.info("smscId: " + smscId);
        logger.info("=========================");
        return ResponseEntity.ok("");
    }

    @GetMapping("/dlr")
    public ResponseEntity receiveDeliveryMessage(@RequestParam("id") String id, @RequestParam("type") String type){
        if(type.equals("1")) {
            logger.info("======delivery message======");
            logger.info("message id: " + id);
            logger.info("type: " + type);
            totalReceived++;
            logger.info("total received: " + totalReceived);
            logger.info("============================");
        }
        return ResponseEntity.ok("");
    }
}
