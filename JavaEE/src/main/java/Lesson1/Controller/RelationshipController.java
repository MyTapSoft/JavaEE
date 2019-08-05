package Lesson1.Controller;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Exceptions.UnauthorizedException;
import Lesson1.Model.RelationshipStatus;
import Lesson1.Service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class RelationshipController {
    private final RelationshipService service;


    @Autowired
    public RelationshipController(RelationshipService service) {
        this.service = service;
    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationship(@RequestParam String userIdTo, HttpSession session) throws UnauthorizedException, BadRequestException {
        isUserLogin(session);
        String userIdFrom = String.valueOf(session.getAttribute("userId"));
        service.addRelationship(userIdFrom, userIdTo);
        return new ResponseEntity<>("Request Send", HttpStatus.OK);
    }

    @RequestMapping(path = "/updateRelationship", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRelationship(HttpSession session,
                                                     @RequestParam(value = "userIdTo") String userIdTo,
                                                     @RequestParam(value = "certainChainStatus") String status) throws UnauthorizedException, BadRequestException {
        isUserLogin(session);
        String userIdFrom = String.valueOf(session.getAttribute("userId"));
        service.updateRelationship(userIdFrom, userIdTo, RelationshipStatus.valueOf(status));

        return new ResponseEntity<>("Request Send", HttpStatus.OK);

    }

    private void isUserLogin(HttpSession session) throws UnauthorizedException {
        if (session.getAttribute("loginStatus") == null) throw new UnauthorizedException("You have to login first");
    }

}
