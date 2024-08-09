package com.hirehive.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class EmailDto {

    private String to;
    private String subject;
    private String text;

}
