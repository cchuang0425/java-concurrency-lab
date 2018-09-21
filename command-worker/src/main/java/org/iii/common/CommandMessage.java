package org.iii.common;

import java.util.UUID;

import lombok.Data;

@Data
public class CommandMessage<P> {
    private UUID id;
    private String name;
    private P param;
}
