package Lesson1.Controller;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Exceptions.UnauthorizedException;
import Lesson1.Service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class RelationshipController {
    private RelationshipService service;

    @Autowired
    public RelationshipController(RelationshipService service) {
        this.service = service;
    }

    @RequestMapping(path = "/addRelationship/{userIdTo}", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationship(@PathVariable String userIdTo, HttpSession session) {

        try {
            isUserLogin(session);
            String userIdFrom = String.valueOf(session.getAttribute("userId"));
            service.addRelationship(userIdFrom, userIdTo);
        } catch (BadRequestException badRequest) {
            return new ResponseEntity<>(badRequest.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException unauthorized) {
            return new ResponseEntity<>(unauthorized.getMessage(), HttpStatus.UNAUTHORIZED);

        } catch (Exception otherError) {
            return new ResponseEntity<>(otherError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Request Send", HttpStatus.OK);
    }

    @RequestMapping(path = "/updateRelationship", method = RequestMethod.POST)
    public ResponseEntity<String> updateRelationship(HttpSession session,
                                                     @RequestParam(value = "userIdTo") String userIdTo,
                                                     @RequestParam(value = "status") String status) {

        try {
            isUserLogin(session);
            String userIdFrom = (String) session.getAttribute("userId");
            service.updateRelationship(userIdFrom, userIdTo, status);
        } catch (BadRequestException badRequest) {
            return new ResponseEntity<>(badRequest.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException unauthorized) {
            return new ResponseEntity<>(unauthorized.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception otherError) {
            return new ResponseEntity<>(otherError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Request Send", HttpStatus.OK);

    }

    @RequestMapping(path = "/incomeRequests", method = RequestMethod.GET)
    public String incomeRequests(HttpSession session, Model model) {
        try {
            isUserLogin(session);
            String userId = String.valueOf(session.getAttribute("userId"));
            model.addAttribute("success", service.getIncomeRequests(userId));
        } catch (UnauthorizedException unauthorized) {
            model.addAttribute("error", unauthorized);
            return "401";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
        return "user-requests";

    }

    @RequestMapping(path = "/outcomeRequests", method = RequestMethod.GET)
    public String outcomeRequests(HttpSession session, Model model) {
        try {
            isUserLogin(session);
            String userId = String.valueOf(session.getAttribute("userId"));
            model.addAttribute("success", service.getOutcomeRequests(userId));
        } catch (UnauthorizedException unauthorized) {
            model.addAttribute("error", unauthorized);
            return "401";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
        return "user-requests";

    }


    private void isUserLogin(HttpSession session) throws UnauthorizedException {
        if (session.getAttribute("loginStatus") != null) throw new UnauthorizedException("You have to login first");
    }

}
