package FormUI;

import BattleShipsLogic.Definitions.MineMoveResult;
import BattleShipsLogic.Definitions.MoveResults;
import BattleShipsLogic.GameObjects.GameManager;
import BattleShipsLogic.GameObjects.Player;
import BattleShipsLogic.GameObjects.Point;
import BattleShipsLogic.GameSettings.BattleShipGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Observer;

public abstract class BattleShipUI implements Observer{

    protected static final int LOAD_GAME = 1;
    protected static final int START_GAME = 2;
    protected static final int GET_GAME_STATUS = 3;
    protected static final int MAKE_A_MOVE = 4;
    protected static final int GET_STATISTICS = 5;
    protected static final int QUIT = 6;
    protected static final int EXIT_GAME = 7;
    protected static final int NANO_SECONDS_IN_SECOND = 1000000000;

    protected GameManager theGame;
    protected String UIloadingError;
    protected abstract void showWelcomeMessage();
    protected abstract void showGameLoadedMessage();
    protected abstract void showGameLoadFailedMessage();
    public abstract void StartGame();
    protected abstract void printGameStartsMessage();
    protected abstract void publishWinnerResults();
    protected abstract void printStatistics();
    protected abstract void showBoards(char[][] board, char[][] trackingboard, String attackPlayerName, String attackedPlayerName);
    protected abstract void showUsedMessage();
    protected abstract void showMineBadPositionMessage();
    protected abstract void showMinePositionWasHitMessage();
    protected abstract void showDrownedMessage();
    protected abstract void showHitAMineMessage();
    protected abstract void showMissMessage();
    protected abstract void showHitMessage();
    protected abstract void showBoard(char[][] board);
    protected abstract String getFilePath();

    public BattleShipUI() {
        theGame = new GameManager();
        theGame.addObserver(this);
        UIloadingError = "";
    }

    // Load game settings and initiate a game.
    protected Boolean loadGame() {
        UIloadingError = "";
        boolean isLoadedSuccessfully = true;
        String filePath = getFilePath();
        if (filePath == null){
            UIloadingError+="Please Select a File" + System.getProperty("line.separator");;
            isLoadedSuccessfully = false;
        }else {
            try {
                // Extract game settings.
                //File file = new File(GAME_SETTINGS_FILE_PATH);
                File file = new File(filePath);
                if (!file.exists()) {
                    UIloadingError += "File Does not Exist" + System.getProperty("line.separator");
                    isLoadedSuccessfully = false;
                } else {
                    JAXBContext jaxbContext = JAXBContext.newInstance(BattleShipGame.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    BattleShipGame gameSettings = (BattleShipGame) jaxbUnmarshaller.unmarshal(file);
                    isLoadedSuccessfully = theGame.LoadGame(gameSettings);
                }
            } catch (JAXBException e) {
                UIloadingError += "Bad XML file" + System.getProperty("line.separator");
                ;
                isLoadedSuccessfully = false;
            }
        }
        return isLoadedSuccessfully;
    }

    protected void showAMineWasSetMessage(){

    }

    protected void showNoMoreMineMessage(){

    }

    protected boolean isLegalChoice(int choice){
        boolean isLegalInput= false;
        switch(theGame.getStatus()){
            case INIT:
                isLegalInput = choice == LOAD_GAME || choice == EXIT_GAME;
                break;
            case LOADED:
                isLegalInput = choice == LOAD_GAME || choice == START_GAME || choice == EXIT_GAME;
                break;
            case RUN:
                isLegalInput = choice == GET_GAME_STATUS || choice == MAKE_A_MOVE || choice == GET_STATISTICS || choice == QUIT || choice == EXIT_GAME;
                break;
            case OVER:
                isLegalInput = choice == LOAD_GAME || choice == START_GAME || choice == EXIT_GAME;
                break;
        }
        return isLegalInput;
    }

    protected boolean isLegalPoint(Point p){
        return p.getX() >= 0 && p.getX() <  theGame.getBoarSize() && p.getY()>=0 && p.getY() < theGame.getBoarSize();
    }

    protected void showBoards(Player player) {
        Player otherPlayer = theGame.getPlayers()[0];
        if(theGame.getPlayers()[0] == player) {
            otherPlayer = theGame.getPlayers()[1];
        }
        //showBoardsTitles(player.getName().toString(), otherPlayer.getName().toString());
        showBoards(player.getPlayerPrimaryGrid(), otherPlayer.getPlayerTrackingGrid(), player.getName().toString(), otherPlayer.getName().toString());
    }

    protected MoveResults attackAPoint(Point pointToAttack) {
        return theGame.makeMove(pointToAttack);
    }

    protected void showMoveResults(MoveResults moveResults){
        switch (moveResults){
            case Hit:
                showHitMessage();
                break;
            case Miss:
                showMissMessage();
                swapPlayers();
                break;
            case Drowned:
                showDrownedMessage();
                break;
            case Used:
                showUsedMessage();
                break;
            case Mine:
                showHitAMineMessage();
                swapPlayers();
                break;
        }
    }

    protected boolean setMineInPosition(Point position){
        MineMoveResult res = theGame.SetMineInPosition(position);
        Boolean retVal = false;
        switch (res){
            case Success:
                theGame.updateStatistics();
                mineAddedSuccess();
                retVal = true;
                break;
            case WasHit:
                showMinePositionWasHitMessage();
                break;
            case NoMinesLeft:
                showNoMoreMineMessage();
                break;
            case MineOverLapping:
                showMineBadPositionMessage();
                break;
            case PositionIsTaken:
                showMinePositionIsTakenMessage();
                break;
        }
        return retVal;
    }

    protected abstract void showMinePositionIsTakenMessage();

    protected void mineAddedSuccess() {
        theGame.saveMove();
        showAMineWasSetMessage();
        swapPlayers();
        showBoards(theGame.getCurrentPlayer());
    }

    protected void swapPlayers() {
        if(theGame.getCurrentPlayer() == theGame.getPlayers()[0]){
            theGame.setCurrentPlayer(theGame.getPlayers()[1]);
        }
        else{
            theGame.setCurrentPlayer(theGame.getPlayers()[0]);
        }
    }

    protected String calcTime(int numberOfSeconds) {
        String secondsStr = (Integer.toString(numberOfSeconds%60));
        String minutesStr = (Integer.toString(numberOfSeconds/60));
        if(secondsStr.length()==1){
            secondsStr = '0' + secondsStr;
        }
        if(minutesStr.length()==1){
            minutesStr = '0' + minutesStr;
        }
        return minutesStr + ":" + secondsStr;
    }
}
