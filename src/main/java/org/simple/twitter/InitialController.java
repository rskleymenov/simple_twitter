package org.simple.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @Autowired
    private HelloWorld helloWorld;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public SimpleModel getHelloWorld() {
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setMessage(helloWorld.sayHello());
        return simpleModel;
    }

}
