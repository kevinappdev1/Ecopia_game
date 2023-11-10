package login.register.model;

public class GameInvitation {

    private String invitedUserName;

    private int invitedUserId;
    private int gameId;
    private int userId;
    private boolean isGameStarted;

    private int invitationId;

    public GameInvitation(String invitedUserName, int gameId, int userId, int invitedUserId, int invitationId) {
        this.invitedUserName = invitedUserName;
        this.gameId = gameId;
        this.userId = userId;
        this.invitedUserId = invitedUserId;
        this.invitationId = invitationId;
    }

    public String getInvitedUserName() {
        return invitedUserName;
    }

    public void setInvitedUserName(String invitedUserName) {
        this.invitedUserName = invitedUserName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(int invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public boolean getIsGameStarted() {
        return isGameStarted;
    }

    public void setIsGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }
}
