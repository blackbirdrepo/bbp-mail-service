package com.bbp.mailservice.integration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"emailAddress", "emailType"})
public class EmailDto {

    private String emailAddress;

    private String emailType;

    private Map<String, Object> contentProperties;
}
