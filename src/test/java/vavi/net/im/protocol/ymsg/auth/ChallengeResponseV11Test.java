/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ChallengeResponseV11Test. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 061108 nsano initial version <br>
 */
@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
class ChallengeResponseV11Test {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "ymsg.v11.username")
    String username;
    @Property(name = "ymsg.v11.password")
    String password;

    @BeforeEach
    void setup() throws IOException {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);
        }
    }

    @Test
    void test1() throws Exception {
        String challenge = "f*e/g&l/(y*f^t-m|(5^y|c-1&q*v%h*5%e/d*b|n/4^m/3&v%4&1&(e^3)*b%l*1%d))";
        String[] result = new ChallengeResponseV11().getResponses(username, password, challenge);
System.err.println("06: " + result[0]);
System.err.println("96: " + result[1]);
        assertEquals(result[0], "r=A7;R=Ci,Q=ep,w=72,G=l5,k=6A,U=7E,M=im,w=0E;O=hD;");
        assertEquals(result[1], "P=29,U=CB;F=jC;W=m1;Q=4F;W=D9;D=69,Z=4i,h=dp;Q=lj,");
    }
}

/* */
