package com.hirehive.util;
import org.apache.commons.validator.routines.EmailValidator;
import java.io.IOException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONObject;

import static com.hirehive.constants.ProjectConstants.API_KEY;


public class GlobalMethods {

    public static boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
    public static boolean isEmailValid(String email) {
        try {
            String apiUrl = "https://emailvalidation.abstractapi.com/v1/?api_key=" + API_KEY + "&email=" + email;
            Content content = Request.Get(apiUrl).execute().returnContent();
            JSONObject jsonResponse = new JSONObject(content.asString());

            // The deliverability field is a string, so we need to compare it
            String deliverability = jsonResponse.getString("deliverability");

            // Check if the deliverability is "DELIVERABLE"
            return "DELIVERABLE".equalsIgnoreCase(deliverability);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
