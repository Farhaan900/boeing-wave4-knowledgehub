package com.stackroute.nlpService;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class POSTagging {
    private String originalWord;
    private String posTag;
}
