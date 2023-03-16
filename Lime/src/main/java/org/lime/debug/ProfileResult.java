package org.lime.debug;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResult {
    private String name;
    private long start;
    private long end;
}
