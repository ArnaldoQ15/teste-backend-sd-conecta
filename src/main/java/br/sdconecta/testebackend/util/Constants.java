package br.sdconecta.testebackend.util;

public class Constants {

    // Exceptions
    public static final String EMAIL_IN_USE = "A user with this e-mail already exists.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String CRM_NOT_FOUND = "CRM not found.";
    public static final String WRONG_EMAIL_PASSWORD = "Wrong email or password.";
    public static final String INVALID_TOKEN = "Invalid token.";
    public static final String ADMIN_ONLY = "Only admins can persist, change and delete doctors.";
    public static final String UNAUTHORIZED_TOKEN = "You don't have enough permissions.";
    public static final String NOT_BLANK = "The field must not be blank.";
    public static final String EMAIL_INVALID = "The field must be sent in a valid format. Example: email@exemple.com";
    public static final String VERY_LONG = "The maximum allowed is 45 numbers.";
    public static final String MALFORMED_JSON = "Malformed JSON request.";
    public static final String INVALID_FIELDS = "One or more fields are invalid. Try again.";


    // Outros
    public static final String BEARER_TOKEN = "Bearer ";
    public static final Integer PAGE_PAGE = 0;
    public static final Integer PAGE_SIZE = 10;

}
