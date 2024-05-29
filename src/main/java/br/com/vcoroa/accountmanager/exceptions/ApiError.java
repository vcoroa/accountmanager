package br.com.vcoroa.accountmanager.exceptions;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {

    private String message;
    private List<String> errors;
}