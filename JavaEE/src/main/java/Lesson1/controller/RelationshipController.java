package Lesson1.controller;

import Lesson1.exceptions.BadRequestException;
import Lesson1.exceptions.globalHandler.ControllerAdviceExceptionHandler;
import Lesson1.exceptions.UnauthorizedException;
import Lesson1.model.RelationshipStatus;
import Lesson1.service.RelationshipService;
import org.apache.log4j.Logger;
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
    private final static Logger log = Logger.getLogger(ControllerAdviceExceptionHandler.class);


    @Autowired
    public RelationshipController(RelationshipService service) {
        this.service = service;
    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationship(@RequestParam String userIdTo, @RequestParam String desiredStatus, HttpSession session) throws UnauthorizedException, BadRequestException {
        if (RelationshipStatus.valueOf(desiredStatus) != RelationshipStatus.pending) {
            return updateRelationship(session, userIdTo, desiredStatus);
        }
        isUserLogin(session);
        String userIdFrom = String.valueOf(session.getAttribute("userId"));
        service.addRelationship(userIdFrom, userIdTo);
        log.info("New relationship added successfully. From user: " + userIdFrom + ". To user: " + userIdTo);
        return new ResponseEntity<>("Request Send", HttpStatus.OK);
    }

    @RequestMapping(path = "/updateRelationship", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRelationship(HttpSession session,
                                                     @RequestParam String userIdTo,
                                                     @RequestParam String desiredStatus) throws UnauthorizedException, BadRequestException {
        isUserLogin(session);
        String userIdFrom = String.valueOf(session.getAttribute("userId"));
        service.updateRelationship(userIdFrom, userIdTo, RelationshipStatus.valueOf(desiredStatus));
        log.info("Relationship updated successfully. From user: " + userIdFrom + ". To user: " + userIdTo);

        return new ResponseEntity<>("Request Send", HttpStatus.OK);

    }

    private void isUserLogin(HttpSession session) throws UnauthorizedException {
        if (session.getAttribute("loginStatus") == null) throw new UnauthorizedException("You have to login first");
    }


}
