/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narayanatutorial.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author rbns
 */
public class SftpShellChannel {

    public static void main(String args[]) {
        Session session = null;
        ChannelSftp sftpchannel = null;
        Channel channel = null;
        JSch jsch = null;
        String USER = "TESTUSER";
        String HOST = "127.0.0.1";
        String PORT = "1523";
        String PASSWORD = "TESTPASSWORD";
        String SchellscriptPath = "/home/user/sample.sh";
        String Param1 = "", Param2 = "";
        try {
            jsch = new JSch();
            session = jsch.getSession(USER, HOST, Integer.parseInt(PORT));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PASSWORD);
            session.connect();
            channel = session.openChannel("shell");

            if (channel.isConnected()) {
                System.out.println("SFTP Shell Channel Connected");
            } else {
                System.out.println("SFTP Shell Channel Not Connected");
            }

            channel.setOutputStream(null);
            //connection timout
            channel.connect(1000000);

            OutputStream inputstream_for_the_channel = channel.getOutputStream();
            PrintStream commander = new PrintStream(inputstream_for_the_channel, true);
            // to execute shellscript
            commander.println("cd " + SchellscriptPath + " ; ./datapop_all.sh" + " " + Param1 + " " + Param2 + " ; exit");
            commander.flush();

            System.out.println("Exit Status:" + channel.getExitStatus());
           //To read shellscript output and writing into log file
            InputStream outputstream_from_the_channel = channel.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(outputstream_from_the_channel));
            String line = null;
            String LOGFILE = "D:/Debug.log";
            while ((line = br.readLine()) != null) {
                System.out.println("Line:" + line);
                FileWriter fw = new FileWriter(LOGFILE, true);
                BufferedWriter bw = new BufferedWriter(fw);
                try {
                    bw.write(line);
                    bw.newLine();
                } catch (Exception e) {
                    System.out.println("Exception while creating logs..." + e.getMessage());
                } finally {
                    if (bw != null) {
                        bw.close();
                    }
                    if (fw != null) {
                        fw.close();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isConnected()) {
                session.disconnect();
            }
            if (channel.isConnected()) {
                channel.disconnect();
            }
            if (!sftpchannel.isConnected()) {
                sftpchannel.disconnect();
            }
        }

    }
}
