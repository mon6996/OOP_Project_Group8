package Game;

public class ChangePlanMessage {
    private String status;

    public ChangePlanMessage() {
    }

    public ChangePlanMessage(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
