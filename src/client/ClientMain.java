package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.util.ArrayList;
import java.util.Hashtable;

public class ClientMain extends Application
{

    Stage stage;
    String serverAddress = "127.0.0.1";
    NetworkUtil nuConnection;
    NetworkUtil nuGetMessage;
    String username;
    String password;
    ChatController chatController;
    NewAccountController newID;
    ConnectionController connectionController;
    ArrayList<String> onlineNowUsersList;
    Hashtable<String, ArrayList<String>> messages;
    ArrayList<String> myBlockList;
    ArrayList<String> blockedMeList;
    ArrayList<String> communicationList;


    public Stage getStage ()
    {
        return stage;
    }

    public void setStage ( Stage stage )
    {
        this.stage = stage;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername ( String username )
    {
        this.username = username;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword ( String password )
    {
        this.password = password;
    }

    @Override
    public void start ( Stage primaryStage ) throws Exception
    {
        stage = primaryStage;

        new SplashScreen ().initUI ();
        Thread.sleep ( 3000 );
        showLoginScreen ();
    }

    public void showLoginScreen ()
    {
        try
        {
            // XML Loading using FXMLLoader
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation ( getClass ().getResource ( "ClientConnector.fxml" ) );
            Parent root = loader.load ();

            // Loading the controller
            ConnectionController connectionController = loader.getController ();


            connectionController.setClientMain ( this );
            connectionController.setServerAddress ( this.serverAddress );
            //this.connectionController = connectionController;

            // Set the primary stage
            stage.setTitle ( "Meowssenger" );
            stage.setScene ( new Scene ( root, 590, 300 ) );
            stage.show ();
        }

        catch ( Exception e )
        {
            System.out.println ( "Prroblem in clientMain\\ClientMain\\showConnectionScreen" + e );
        }
    }

    public void showChatScreen ()
    {
        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation ( getClass ().getResource ( "ClientChatScreen.fxml" ) );

        try
        {
            Parent root = loader.load ();

            chatController = loader.getController ();
            chatController.setClientMain ( this );
            chatController.setUsername ( username );
            chatController.setServerAddress ( serverAddress );
            chatController.setStage ( stage );
            chatController.setRoot ( root );
            chatController.set ( onlineNowUsersList, messages, myBlockList, blockedMeList, communicationList );
            chatController.show ();
        }

        catch ( Exception e )
        {
            System.out.println ( "EXCEPTION : ClientMain/showChatScreen" );
            e.printStackTrace ();
        }
    }


    public void logout () throws Exception
    {
        showLoginScreen ();

        Platform.runLater ( () -> {
            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "Logging out" );
            alert.setHeaderText ( "Successfully Logged Out" );
            alert.setContentText ( "" );
            alert.showAndWait ();
        } );
    }

    public void invalidLogin ()
    {
        Platform.runLater ( () -> {

            Alert alert = new Alert ( Alert.AlertType.ERROR );
            alert.setTitle ( "Incorrect Credentials" );
            alert.setHeaderText ( "Incorrect Credentials" );
            alert.setContentText ( "The username and password you provided is not correct." );
            alert.showAndWait ();
        } );
    }

    public void closePrevious ( NetworkUtil nu )
    {
        Platform.runLater ( () -> {

            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "" );
            alert.setHeaderText ( "LOGGING OUT ..." );
            alert.setContentText ( "New login from another device. Logging out from here..." );
            alert.showAndWait ();

            nu.closeConnection ();
            showLoginScreen ();
        } );

    }

    public void welcomeBack ()
    {
        Platform.runLater ( () -> {

            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "" );
            alert.setHeaderText ( "Welcome back, " + username );
            alert.setContentText ( "" );
            alert.showAndWait ();

            showChatScreen ();
            nuGetMessage = new NetworkUtil ( serverAddress, 34343 );
            nuGetMessage.write ( username );
            new Threads.AcceptThread ( nuGetMessage, chatController );
        } );

    }

    public void signUp ()
    {
        Platform.runLater ( () -> {

            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "" );
            alert.setHeaderText ( "Hello, " + username );
            alert.setContentText ( "" );
            alert.showAndWait ();


            showChatScreen ();
            nuGetMessage = new NetworkUtil ( serverAddress, 34343 );
            System.out.println ( username );
            nuGetMessage.write ( username );
            new Threads.AcceptThread ( nuGetMessage, chatController );

        } );
    }

    public void newLogin ()
    {
        Platform.runLater ( () -> {

            Alert alert = new Alert ( Alert.AlertType.WARNING );
            alert.setTitle ( "" );
            alert.setHeaderText ( "Welcome back, " + username + "\nYou were logged in another device" );
            alert.setContentText ( "Previous Session Terminated" );
            alert.showAndWait ();

            showChatScreen ();

            nuGetMessage = new NetworkUtil ( serverAddress, 34343 );
            nuGetMessage.write ( username );
            new Threads.AcceptThread ( nuGetMessage, chatController );
        } );
    }

    public void occupied ()
    {
        newID.usernameConflict ();
    }


    public void updateOnlineUsersList ( String newUser )
    {
        onlineNowUsersList.add ( newUser );
        chatController.update ( newUser );
    }

    public void newAccount () throws Exception
    {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation ( getClass ().getResource ( "NewAccount.fxml" ) );
        Parent root = loader.load ();

        // Loading the controller
        NewAccountController newID = loader.getController ();
        //this.newID = newID;
        newID.setClientMain ( this );
        newID.setServerAddress ( this.serverAddress );
        this.newID = newID;

        // Set the primary stage
        stage.setTitle ( "Meowssenger" );
        stage.setScene ( new Scene ( root, 650, 400 ) );
        stage.show ();
    }

    public void set ( ArrayList<String> onlineNowUsersList, Hashtable<String, ArrayList<String>> messages, ArrayList<String> myBlockList, ArrayList<String> blockedMe, ArrayList<String> communicationList )
    {
        this.onlineNowUsersList = onlineNowUsersList;
        this.messages = messages;
        this.myBlockList = myBlockList;
        this.blockedMeList = blockedMe;
        this.communicationList = communicationList;
    }

    public void blockedMe ( String person )
    {
        chatController.blockedMe ( person );
    }

    public void unblockedMe ( String person )
    {
        chatController.unblockedMe ( person );
    }


    public static void main ( String[] args )
    {
        launch ( args );
    }
}
