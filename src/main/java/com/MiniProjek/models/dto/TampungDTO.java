package com.MiniProjek.models.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TampungDTO <T>{

    private List<String> message = new ArrayList<>();
    private List<T> payload;

}
