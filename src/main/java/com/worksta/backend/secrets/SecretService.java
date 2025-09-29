package com.worksta.backend.secrets;

import org.springframework.stereotype.Service;

import java.util.HexFormat;

/**
 * Yes, this is the secret service.
 */
@Service
public class SecretService {

    public static final byte[] JWT_KEY_SECRET = HexFormat.of().parseHex(
            "befc60b5d75a947bd392677a69fa60fbdc23654ba587c91258fcb2da667886a7"
    );

}
