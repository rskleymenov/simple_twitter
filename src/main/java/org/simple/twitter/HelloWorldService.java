package org.simple.twitter;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldService implements HelloWorld {

    @Override
    public String sayHello() {
        return "Hi world!";
    }
}
