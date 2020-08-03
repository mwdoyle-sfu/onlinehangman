package ca.cmpt213.a4.onlinehangman.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Request ID not found.")
public class GameNotFoundException extends RuntimeException {



}
