package lib_group.library.exceptions.message;

public class ErrorMessage {

    public String exceptionPlace;
    public String exceptionMessage;

    public ErrorMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public ErrorMessage(String exceptionPlace, String exceptionMessage) {
        this.exceptionPlace = exceptionPlace;
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionPlace() {
        return exceptionPlace;
    }

    public void setExceptionPlace(String exceptionPlace) {
        this.exceptionPlace = exceptionPlace;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
