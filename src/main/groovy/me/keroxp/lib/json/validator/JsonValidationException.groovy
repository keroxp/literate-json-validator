package me.keroxp.lib.json.validator

class JsonValidationException extends Exception {
    JsonValidationException() {
        super()
    }

    JsonValidationException(String message) {
        super(message)
    }

    JsonValidationException(String message, Throwable cause) {
        super(message, cause)
    }
}
