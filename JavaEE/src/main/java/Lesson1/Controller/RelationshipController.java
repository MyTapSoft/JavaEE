package Lesson1.Controller;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RelationshipController {
    private RelationshipService service;

    @Autowired
    public RelationshipController(RelationshipService service) {
        this.service = service;
    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> addRelationship(@RequestParam(value = "userIdFrom") String userIdFrom,
                                                  @RequestParam(value = "userIdTo") String userIdTo) {
        try {
            service.addRelationship(userIdFrom, userIdTo);
        } catch (BadRequestException badRequest) {
            return new ResponseEntity<>(badRequest.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception otherError) {
            return new ResponseEntity<>(otherError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Request Send", HttpStatus.OK);
    }

    @RequestMapping(path = "/updateRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> updateRelationship(@RequestParam(value = "userIdFrom") String userIdFrom,
                                                     @RequestParam(value = "userIdTo") String userIdTo,
                                                     @RequestParam(value = "status") String status) {
        try {
            service.updateRelationship(userIdFrom, userIdTo, status);
        } catch (BadRequestException badRequest) {
            return new ResponseEntity<>(badRequest.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception otherError) {
            return new ResponseEntity<>(otherError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Request Send", HttpStatus.OK);

    }

    @RequestMapping(path = "/incomeRequests", method = RequestMethod.GET)
    public String incomeRequests(@RequestParam(value = "userId") String userId, Model model) {
        try {
            model.addAttribute("success", service.getIncomeRequests(userId));
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
        return "user-requests";

    }

    @RequestMapping(path = "/outcomeRequests", method = RequestMethod.GET)
    public String outcomeRequests(@RequestParam(value = "userId") String userId, Model model) {

        try {
            model.addAttribute("success", service.getOutcomeRequests(userId));
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
        return "user-requests";

    }
}
