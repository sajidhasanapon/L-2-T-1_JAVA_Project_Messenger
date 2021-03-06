package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SplashScreen
{

    private JDialog dialog;
    private JFrame frame;
    private JProgressBar progressBar;

    protected void initUI () throws MalformedURLException
    {
        showSplashScreen ();
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer> ()
        {

            @Override
            protected Void doInBackground () throws Exception
            {
                for ( int i = 0; i < 200; i++ )
                {
                    Thread.sleep ( 17 );  // Simulate loading
                    publish ( i );   // Notify progress
                }
                return null;
            }

            @Override
            protected void process ( List<Integer> chunks )
            {
                progressBar.setValue ( chunks.get ( chunks.size () - 1 ) );
            }

            @Override
            protected void done ()
            {
                hideSplashScreen ();
            }

        };
        worker.execute ();
    }

    protected void hideSplashScreen ()
    {
        dialog.setVisible ( false );
        dialog.dispose ();
    }

    protected void showSplashScreen () throws MalformedURLException
    {
        dialog = new JDialog ( ( Frame ) null );
        dialog.setModal ( false );
        dialog.setUndecorated ( true );
        JLabel background = new JLabel ( new ImageIcon (  "F:\\Project\\src\\client\\images\\main.jpg"  ) );
        background.setLayout ( new BorderLayout () );
        dialog.add ( background );

        progressBar = new JProgressBar ();
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.orange);
        background.add ( progressBar, BorderLayout.SOUTH );
        dialog.pack ();
        dialog.setLocationRelativeTo ( null );
        dialog.setVisible ( true );
    }

}