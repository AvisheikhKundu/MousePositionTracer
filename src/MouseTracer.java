
//Author@AvisheikhKundu

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class MouseTracer extends Thread implements ActionListener
{

    JFrame frm;
    Restriction ris;
    JButton start,stop,show;
    Vector store;
    JSlider jsb;
    JLabel lab;
    JToggleButton tog;
    Panel p1,p2;
    JProgressBar progress;
    public MouseTracer()
    {
        frm=new JFrame("Mouse Tracer");
        frm.setDefaultCloseOperation(3);
        start=new JButton("Start the tracing");//{
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();

        start.setToolTipText("Click to start tracing");
        stop=new JButton("Stop the tracing");
        stop.setVerticalAlignment(SwingConstants.BOTTOM);
        stop.setIconTextGap(80);
        //@author AVISHEIKH

        int minimum = 0;
        int maximum = 100;
        progress = new JProgressBar(minimum, maximum);


        // Overlay a string showing the percentage done
        progress.setStringPainted(true);

        show=new JButton("<html>"+"<i>"+"Show The </i><b><font size=5>Tracing"+"</b></html>");
        jsb=new JSlider(1,100,50);
        jsb.setInverted(true);
        lab=new JLabel("Change the Speed of Motion");
        lab.setTransferHandler(new TransferHandler("text"));


        tog=new JToggleButton("Restrict the Mouse");
        frm.setLayout(new FlowLayout());
        frm.setAlwaysOnTop(true);
        stop.setEnabled(false);
        show.setEnabled(false);
        jsb.setEnabled(false);
        lab.setEnabled(false);
        tog.setForeground(new Color(0,150,0));
        ris=new Restriction(frm);
        ris.start();
        ris.suspend();
        p1=new Panel();
        p1.add(start);
        p1.add(show);
        p1.add(stop);
        p1.add(tog);
        p1.add(lab);
        p1.add(jsb);
        p1.add(progress);
        progress.setValue(0);
        p1.setLayout(new GridLayout(3,1,2,2));
        frm.getContentPane().add(p1);

        frm.setSize(600,270);
        frm.setLocationRelativeTo(null);
        frm.pack();
        frm.setVisible(true);
        store=new Vector(10,5);
        start.addActionListener(this);
        stop.addActionListener(this);
        show.addActionListener(this);
        tog.addActionListener(this);


        start();
        suspend();


    }

    public void run()
    {


        while(true)
        {
            try{
                Thread.sleep(10);
            }catch(Exception ee){}
            store.addElement((MouseInfo.getPointerInfo().getLocation()));

        }
    }
    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getActionCommand().startsWith("Start"))
        {
            progress.setValue(0);
            store.removeAllElements();
            start.setEnabled(false);
            stop.setEnabled(true);
            show.setEnabled(false);
            jsb.setEnabled(false);
            lab.setEnabled(false);
            resume();
        }
        else if(ae.getActionCommand().startsWith("Stop"))
        {
            start.setEnabled(true);
            stop.setEnabled(false);
            show.setEnabled(true);
            jsb.setEnabled(true);
            lab.setEnabled(true);
            suspend();
        }
        else if(ae.getActionCommand().startsWith("<html>"))
        {
            start.setEnabled(false);
            stop.setEnabled(false);
            suspend();
            ShowMouseMove smm=new ShowMouseMove(this);
            smm.start();

        }
        else if(ae.getSource()==tog)
        {
            if(tog.isSelected())
            {
                tog.setForeground(new Color(200,0,0));
                tog.setText("Resum the Mouse");
                ris.resume();
            }
            else
            {
                tog.setForeground(new Color(0,150,0));
                tog.setText("Ristrict the Mouse");
                ris.suspend();
            }
        }
    }
    public static void main(String ag[])
    {
        new MouseTracer();
    }
}
class ShowMouseMove extends Thread
{
    MouseTracer mt;
    public ShowMouseMove(MouseTracer mt)
    {
        super();
        this.mt=mt;
    }
    public void run()
    {
        mt.start.setEnabled(false);
        mt.stop.setEnabled(false);
        mt.show.setEnabled(false);
        mt.jsb.setEnabled(false);
        mt.lab.setEnabled(false);
        Enumeration e=mt.store.elements();
        int size=mt.store.size();
        int counter=0;
        mt.progress.setMaximum(size);
        while(e.hasMoreElements())
        {
            try{
                mt.progress.setValue(counter);
                counter++;
                Thread.sleep(mt.jsb.getValue()/3);
                Robot r=new Robot();
                Point p=(Point)e.nextElement();
                r.mouseMove((int)p.getX(),(int)p.getY());
            }catch(Exception ee){}
        }
        mt.progress.setValue(size);
        mt.start.setEnabled(true);
        mt.stop.setEnabled(false);
        mt.show.setEnabled(true);
        mt.jsb.setEnabled(true);
        mt.lab.setEnabled(true);
    }
}

class Restriction extends Thread
{
    JFrame frm;
    int x3,y3;
    public Restriction(JFrame frm)
    {
        this.frm=frm;
    }
    public void run()
    {
        while(true)
        {
            try{
            
                int x1=frm.getLocation().x;
                int y1=frm.getLocation().y;
                int x2=frm.getWidth();
                int y2=frm.getHeight();
                int x=(int)MouseInfo.getPointerInfo().getLocation().getX();
                int y=(int)MouseInfo.getPointerInfo().getLocation().getY();


                if(x>=x1 && x<=(x1+x2) && y>=y1 && y<=(y1+y2))
                {	x3=x;
                    y3=y;
                }//	System.out.println("IN");
                else
                {
                    Robot r=new Robot();
                    r.mouseMove(x3-5,y3-5);
                }

                Thread.sleep(2);
            }catch(Exception tt){}
        }
    }
}
