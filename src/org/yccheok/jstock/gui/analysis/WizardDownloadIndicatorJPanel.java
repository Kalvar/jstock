/*
 * JStock - Free Stock Market Software
 * Copyright (C) 2009 Yan Cheng Cheok <yccheok@yahoo.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.yccheok.jstock.gui.analysis;

import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;
import javax.swing.Icon;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yccheok.jstock.gui.Icons;
import org.yccheok.jstock.gui.IndicatorProjectManager;
import org.yccheok.jstock.gui.MainFrame;
import org.yccheok.jstock.gui.Utils;
import org.yccheok.jstock.internationalization.GUIBundle;

/**
 *
 * @author yccheok
 */
public class WizardDownloadIndicatorJPanel extends javax.swing.JPanel {

    /** Creates new form WizardDownloadIndicatorJPanel */
    public WizardDownloadIndicatorJPanel(IndicatorProjectManager indicatorProjectManager) {
        this.indicatorProjectManager = indicatorProjectManager;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/yccheok/jstock/data/gui"); // NOI18N
        jXHeader1.setDescription(bundle.getString("WizardDownloadlIndicatorJPanel_Description")); // NOI18N
        jXHeader1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32x32/ark2.png"))); // NOI18N
        jXHeader1.setTitle(bundle.getString("WizardDownloadlIndicatorJPanel_Title")); // NOI18N
        add(jXHeader1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText(bundle.getString("WizardDownloadlIndicatorJPanel_DownloadingIndicators...")); // NOI18N
        jLabel1.setForeground(Color.BLUE);
        jPanel1.add(jLabel1);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16x16/spinner.gif"))); // NOI18N
        jPanel1.add(jLabel2);

        jLabel3.setText(bundle.getString("WizardDownloadlIndicatorJPanel_ViewLog")); // NOI18N
        this.jLabel3.setVisible(false);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });
        jPanel1.add(jLabel3);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        MemoryLogJDialog memoryLogJDialog = new MemoryLogJDialog(MainFrame.getInstance(), true);
        memoryLogJDialog.setLocationRelativeTo(this);
        memoryLogJDialog.setLog(memoryLog);
        memoryLogJDialog.setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        // TODO add your handling code here:
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        // TODO add your handling code here:
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jLabel3MouseExited

    public void start() {
        if (this.downloadIndicatorTask == null) {
            this.downloadIndicatorTask = this.getDownloadIndicatorTask();
            this.downloadIndicatorTask.execute();
        }
    }

    public void cancel() {
        if (this.downloadIndicatorTask != null) {
            this.downloadIndicatorTask.cancel(true);
        }
        this.downloadIndicatorTask = null;
    }

    public boolean isNextFinishButtonEnabled() {
        if (this.downloadIndicatorTask == null) {
            return false;
        }
        return this.downloadIndicatorTask.isDone();
    }

    public void setIndicatorDownloadInfos(List<IndicatorDownloadManager.Info> indicatorDownloadInfos) {
        this.indicatorDownloadInfos = indicatorDownloadInfos;
    }

    private static class Status {
        public final String message;
        public final Icon icon;
        private Status(String message, Icon icon) {
            if (message == null || icon == null) {
                throw new IllegalArgumentException("Method arguments cannot be null");
            }
            this.message = message;
            this.icon = icon;
        }
        public static Status newInstance(String message, Icon icon) {
            return new Status(message, icon);
        }
    }

    private SwingWorker<Void, Status> getDownloadIndicatorTask() {
        SwingWorker<Void, Status> worker = new SwingWorker<Void, Status>() {
            @Override
            protected void done() {
                // Inform wizard.
                WizardDownloadIndicatorJPanel.this.observable.setChanged();
                WizardDownloadIndicatorJPanel.this.observable.notifyObservers();
            }

            @Override
            protected void process(java.util.List<Status> statuses) {
                for (Status status : statuses) {
                    writeToMemoryLog(status.message);
                    jLabel1.setText(status.message);
                    jLabel2.setIcon(status.icon);
                    if (status.icon == Icons.ERROR || status.icon == Icons.WARNING) {
                        jLabel1.setForeground(Color.RED);
                        jLabel3.setVisible(true);
                    }
                    else
                    {
                        jLabel1.setForeground(Color.BLUE);
                        jLabel3.setVisible(false);
                    }
                }
            }

            @Override
            protected Void doInBackground() {
                if (isCancelled()) {
                    return null;
                }

                memoryLog.clear();

                final int expected = indicatorDownloadInfos.size();
                int actual = 0;

                for (IndicatorDownloadManager.Info info : indicatorDownloadInfos) {
                    final String message0 = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_DownloadingIndicator_template..."), info.projectName);
                    publish(Status.newInstance(message0, Icons.BUSY));
                    final Utils.InputStreamAndMethod inputStreamAndMethod = Utils.getResponseBodyAsStreamBasedOnProxyAuthOption(info.fileURL.toString());
                    if (inputStreamAndMethod.inputStream == null) {
                        final String fail_message0 = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_DownloadIndicatorFail_template"), info.projectName);
                        publish(Status.newInstance(fail_message0, Icons.BUSY));
                        inputStreamAndMethod.method.releaseConnection();
                        continue;
                    }
                    // Write to temp file.
                    OutputStream out = null;
                    File temp = null;
                    try {
                        // Create temp file.
                        temp = File.createTempFile(Utils.getJStockUUID(), info.projectName + ".zip");
                        // Delete temp file when program exits.
                        temp.deleteOnExit();

                        out = new FileOutputStream(temp);

                        // Transfer bytes from the ZIP file to the output file
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = inputStreamAndMethod.inputStream.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    }
                    catch (IOException ex) {
                        final String fail_message0 = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_DownloadIndicatorFail_template"), info.projectName);
                        publish(Status.newInstance(fail_message0, Icons.BUSY));
                        log.error(null, ex);
                        continue;
                    }
                    finally {
                        Utils.close(out);
                        Utils.close(inputStreamAndMethod.inputStream);
                        inputStreamAndMethod.method.releaseConnection();
                    }

                    // Check for zipfile check sum.
                    final String message1 = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_CheckingCheckSum_template..."), info.projectName);
                    publish(Status.newInstance(message1, Icons.BUSY));
                    final long checksum = org.yccheok.jstock.analysis.Utils.getChecksum(temp);
                    if (checksum != info.checksum) {
                        final String fail_message1 = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_CheckCheckSumFail_template"), info.projectName);
                        publish(Status.newInstance(fail_message1, Icons.BUSY));
                        continue;
                    }
                    
                    if (isCancelled()) {
                        // Early return.
                        return null;
                    }
                    
                    // Install.
                    final String message2 = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_InstallingIndicator_template..."), info.projectName);
                    publish(Status.newInstance(message2, Icons.BUSY));
                    if (indicatorProjectManager.install(temp)) {
                        actual++;
                    }
                    else {
                        final String fail_message2 = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_InstallIndicatorFail_template"), info.projectName);
                        publish(Status.newInstance(fail_message2, Icons.BUSY));
                    }
                }

                final String message = MessageFormat.format(GUIBundle.getString("WizardDownloadlIndicatorJPanel_NOutOfMIndicatorsInstalledSuccessfully_template"), actual, expected);
                if (actual == expected) {
                    publish(Status.newInstance(message, Icons.OK));
                }
                else {
                    if (actual > 0) {
                        publish(Status.newInstance(message, Icons.WARNING));
                    }
                    else {
                        publish(Status.newInstance(message, Icons.ERROR));
                    }
                }
                return null;
            }
        };
        return worker;
    }

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    private void writeToMemoryLog(String message) {
        // http://www.leepoint.net/notes-java/io/10file/sys-indep-newline.html
        // public static String newline = System.getProperty("line.separator");
        // When NOT to use the system independent newline characters
        // JTextArea lines should be separated by a single '\n' character, not the sequence that is used for file line separators in the operating system.
        // Console output (eg, System.out.println()), works fine with '\n', even on Windows.
        final DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        final String s = dateFormat.format(new Date()) + "\n" + message;
        this.memoryLog.add(s);
    }

    private volatile SwingWorker<Void, Status> downloadIndicatorTask = null;
    private final IndicatorProjectManager indicatorProjectManager;
    private List<IndicatorDownloadManager.Info> indicatorDownloadInfos;

    // setChanged must be called, before calling notifyObservers.
    // Cumbersome! Perhaps we still should stick back with our own Subject
    // Observer framework.
    private static final class ObservableEx extends java.util.Observable {
        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
    private final ObservableEx observable = new ObservableEx();
    
    private static final Log log = LogFactory.getLog(WizardDownloadIndicatorJPanel.class);

    private final List<String> memoryLog = new ArrayList<String>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private org.jdesktop.swingx.JXHeader jXHeader1;
    // End of variables declaration//GEN-END:variables

}
