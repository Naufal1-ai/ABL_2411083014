import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class producerController {

    @Autowired
    private ProduserService produserService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam(name = "message") String message) {
        produserService.sendMessage(message);
        return "message sent" + message;
    }
}
