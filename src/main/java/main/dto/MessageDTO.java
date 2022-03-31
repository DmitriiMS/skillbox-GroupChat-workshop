package main.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MessageDTO {
    private String datetime;
    private String username;
    private String text;
}
