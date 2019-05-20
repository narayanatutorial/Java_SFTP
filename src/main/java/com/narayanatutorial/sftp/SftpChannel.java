/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narayanatutorial.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 *
 * @author rbns
 */
public class SftpChannel {

    public static void main(String args[]) {
        Session session = null;
        ChannelSftp sftpchannel = null;
        Channel channel = null;
        JSch jsch = null;
        String USER="TESTUSER";
        String HOST="127.0.0.1";
        String PORT="1253";
        String PASSWORD="TESTPASSWORD";
        String SourceFile="D:/sample.txt";
        String DestinationFile="/home/user/sample.txt";
        String DestinationPath="/home/user/sampledirectory/";
        try {
            jsch = new JSch();
            session = jsch.getSession(USER, HOST, Integer.parseInt(PORT));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PASSWORD);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            sftpchannel = (ChannelSftp) channel;
            if (sftpchannel.isConnected()) {
                System.out.println("SFTP Connected");
            } else {
                System.out.println("SFTP Not Connected");
            }
            //transfer file from local to remote
            sftpchannel.put(SourceFile, DestinationFile);
            //create folder in the remote location
            sftpchannel.mkdir(DestinationPath);


        } catch (Exception e) {
            e.printStackTrace();
        }finally{
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
